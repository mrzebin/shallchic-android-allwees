package com.project.app.fragment.order;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.project.app.R;
import com.project.app.adapter.RefundPostPhotosAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.AwsAccessTokenBean;
import com.project.app.contract.OrderReviewContract;
import com.project.app.presenter.OrderReviewPresenter;
import com.project.app.ui.dialog.OrderReviewRemindUtil;
import com.project.app.utils.FileManager;
import com.project.app.utils.FileUtil;
import com.project.app.utils.FilterUpProfilePhotoUrl;
import com.project.app.utils.PhotoUtils;
import com.project.app.utils.ThreadLoopUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * create by hb
 * review详情
 */
public class OrderReviewDetailFragment extends BaseMvpQmuiFragment<OrderReviewPresenter> implements OrderReviewContract.View {
    @BindView(R.id.iv_reviewGoodsPhoto)
    QMUIRadiusImageView iv_reviewGoodsPhoto;
    @BindView(R.id.tv_reviewGoodsName)
    TextView tv_reviewGoodsName;
    @BindView(R.id.rlv_chooseImg)
    RecyclerView rlv_chooseImg;
    @BindView(R.id.et_reNote)
    EditText et_reNote;
    @BindView(R.id.iv_starLevelOne)
    ImageView iv_starLevelOne;
    @BindView(R.id.iv_starLevelTwo)
    ImageView iv_starLevelTwo;
    @BindView(R.id.iv_starLevelThree)
    ImageView iv_starLevelThree;
    @BindView(R.id.iv_starLevelFour)
    ImageView iv_starLevelFour;
    @BindView(R.id.iv_starLevelFive)
    ImageView iv_starLevelFive;

    private String mOrderId;
    private String mOrderItemUuid;
    private String mGoodsName;
    private String mGoodsAvatarUrl;
    private final String mAccessType = "rev";
    private RefundPostPhotosAdapter mAdapter;
    private Context mContext;
    private Uri mImageUri;
    private FileManager fileManager;
    private PhotoUtils photoUtils;
    private final int compassSize = 100;
    private String mCamerFilePath;
    private ThreadLoopUtil mTreadLoopUtil;
    private File mTargetFile;
    private int mStarLevel = 5; //默认是五颗星
    private int mMaxPhotos = 9;
    private static final int LUBAN_COMPASS_SUCCESS = 1;
    private static final int LUBAN_COMPASS_FAIL    = 2;
    private static final int AWS_UPLOAD_BEGING     = 3;
    private final List<String> upReviewPortraits = new ArrayList<>();
    private final List<ImageView> mStarList = new ArrayList<>();
    private OrderReviewRemindUtil mReviewRemindUtil;

    private final List<String> mRefundDatas = new ArrayList<>();
    private final String[] mPermission_storage = new String[]{
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    };
    {
        mRefundDatas.add("-1");
    }

    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LUBAN_COMPASS_SUCCESS:
                    List<String> cells = new ArrayList<>();
                    cells.add(mTargetFile.getAbsolutePath());
                    mAdapter.addData(cells);
                    break;
                case LUBAN_COMPASS_FAIL:

                    break;
                case AWS_UPLOAD_BEGING:
                    beginUpload();
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_review_order;
    }

    @Override
    public void initView() {
        initTopbar();
        initWidget();
    }

    private void beginUpload(){
        startLoading();
        mTreadLoopUtil = new ThreadLoopUtil(Constant.S3Type.getUpType(1));
        mTreadLoopUtil.startUploadFile(mTargetFile, new ThreadLoopUtil.CallbackListener() {
            @Override
            public void s3UploadSuccess(String result) {
                stopLoading();
                Message msg = mHandler.obtainMessage(LUBAN_COMPASS_SUCCESS);
                Bundle bundle = new Bundle();
                bundle.putString("awsurl",result);
                msg.setData(bundle);
                upReviewPortraits.add(result);
                mHandler.sendMessage(msg);
            }
            @Override
            public void s3UploadFail(String result) {
                stopLoading();
                String failReason = getContext().getString(R.string.refund_upload_to_fail);
                ToastUtil.showToast(failReason);
            }
        });
    }

    private void initTopbar() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        Bundle baba = getArguments();
        mOrderItemUuid = baba.getString("orderItemUuid");
        mOrderId  = baba.getString("orderUuid");
        mGoodsName = baba.getString("orderGoodsName");
        mGoodsAvatarUrl = baba.getString("orderGoodsUrl");
    }

    private void initWidget() {
        mContext = getContext();
        mPresenter = new OrderReviewPresenter();
        mPresenter.attachView(this);
        mStarList.add(iv_starLevelOne);
        mStarList.add(iv_starLevelTwo);
        mStarList.add(iv_starLevelThree);
        mStarList.add(iv_starLevelFour);
        mStarList.add(iv_starLevelFive);
        userRating();
        fileManager = new FileManager(getContext());
        GridLayoutManager manager = new GridLayoutManager(getContext(),4);
        rlv_chooseImg.setLayoutManager(manager);
        mAdapter = new RefundPostPhotosAdapter(mRefundDatas);
        rlv_chooseImg.setAdapter(mAdapter);
        if(!TextUtils.isEmpty(mGoodsName)){
            tv_reviewGoodsName.setText(mGoodsName);
        }
        if(!TextUtils.isEmpty(mGoodsAvatarUrl)){
            ImageLoader.getInstance().displayImage(iv_reviewGoodsPhoto,mGoodsAvatarUrl,R.mipmap.allwees_ic_default_goods);
        }
        mAdapter.setListener(new RefundPostPhotosAdapter.ChoiceCallbackListener() {
            @Override
            public void choicePhotoByFunction() {
                if(mAdapter.getItemCount() >= mMaxPhotos){
                    ToastUtil.showToast(getResources().getString(R.string.review_order_limit_photos_length));
                    return;
                }
                if(upReviewPortraits.size() >0){
                    upReviewPortraits.clear();
                }
                mReviewRemindUtil.show();
            }
            @Override
            public void deletePhotoByFunction(int deleteIndex) {
                mAdapter.remove(deleteIndex);
                upReviewPortraits.remove(deleteIndex-1);
            }
        });
        mReviewRemindUtil = new OrderReviewRemindUtil(getContext(),true,true);
        mReviewRemindUtil.setMListener(new OrderReviewRemindUtil.IReviewListener(){
            @Override
            public void fromAlbum() {
                choicePhotoByUser(1);
            }

            @Override
            public void takePhoto() {
                choicePhotoByUser(0);
            }
        });

        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener(){
            @Override
            public void onPhotoResult(Uri uri, int selectType) {
                File uploadFile = null;
                if(selectType == PhotoUtils.INTENT_SELECT){
                    String filePath = FileUtil.getFilePathByUri(mContext, uri);
                    uploadFile = new File(filePath);
                }else if(selectType == PhotoUtils.INTENT_TAKE){
                    if (!TextUtils.isEmpty(uri.toString())) {
                        uploadFile =  new File(uri.getPath());
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                            if (!uploadFile.exists()) {
                                uploadFile = new File(fileManager.getRealPathFromUri(uri));
                            }
                        }
                    }
                }
                if(uploadFile != null){
                    if(!uploadFile.exists()){
                        return;
                    }
                    Luban.with(mContext)
                            .load(uploadFile)
                            .setTargetDir(uploadFile.getParent())
                            .ignoreBy(compassSize)
                            .putGear(3)
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {

                                }
                                @Override
                                public void onSuccess(File file) {
                                    mTargetFile = file;
                                    mHandler.sendEmptyMessage(AWS_UPLOAD_BEGING);
                                }
                                @Override
                                public void onError(Throwable e) {

                                }
                            }).launch();
                }
            }
            @Override
            public void onPhotoCancel() {

            }
        }, PhotoUtils.NO_CROP);
    }

    /**
     * 评星
     */
    private void userRating(){
        for(int i=0;i<mStarList.size();i++){
            mStarList.get(i).setSelected(false);
        }

        for(int i=1;i <= mStarLevel;i++){
            mStarList.get(i-1).setSelected(true);
        }
    }

    @OnClick({R.id.btn_postRefund,R.id.iv_back,R.id.iv_starLevelOne,R.id.iv_starLevelTwo,R.id.iv_starLevelThree,R.id.iv_starLevelFour,R.id.iv_starLevelFive})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.btn_postRefund:
                checkInputValid();
                break;
            case R.id.iv_back:
                popBackStack();
                break;
            case R.id.iv_starLevelOne:
                mStarLevel = 1;
                userRating();
                break;
            case R.id.iv_starLevelTwo:
                mStarLevel = 2;
                userRating();
                break;
            case R.id.iv_starLevelThree:
                mStarLevel = 3;
                userRating();
                break;
            case R.id.iv_starLevelFour:
                mStarLevel = 4;
                userRating();
                break;
            case R.id.iv_starLevelFive:
                mStarLevel = 5;
                userRating();
                break;
        }
    }

    private void checkInputValid() {
        String note = et_reNote.getText().toString().trim();
        mPresenter.submitReviewReasonToService(mOrderId,mOrderItemUuid,mStarLevel,note,  FilterUpProfilePhotoUrl.filterS3PhotoUrl(upReviewPortraits));
    }

    private void choicePhotoByUser(int position){
        if(position == 0){
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    this.requestPermissions(mPermission_storage,1);
                    return;
                }
                if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    this.requestPermissions(mPermission_storage,1);
                    return;
                }
                Intent catureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(catureIntent.resolveActivity(getActivity().getPackageManager()) != null){
                    File photoFile;
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        mImageUri = photoUtils.buildUri(getActivity());
                    }else{
                        photoFile = photoUtils.createImageFile(mContext);
                        if (photoFile != null) {
                            mCamerFilePath = photoFile.getAbsolutePath();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                                mImageUri = FileProvider.getUriForFile(getActivity(), mContext.getPackageName() + ".FileProvider", photoFile);
                            } else {
                                mImageUri = Uri.fromFile(photoFile);
                            }
                        }
                    }
                    if(mImageUri != null){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            photoUtils.takePictureWithQ(this, mImageUri);
                        } else {
                            photoUtils.takePicture(this,mImageUri);
                        }
                    }
                }
            }
        }else if(position == 1){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    this.requestPermissions(mPermission_storage,1);
                    return;
                }
            }
            photoUtils.selectPicture(this);
        }
    }

    private int mRecodePosition = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(allPermissionsGranted()){
            choicePhotoByUser(mRecodePosition);
        }
    }

    public boolean allPermissionsGranted(){
        for(String permission:mPermission_storage){
            if( ContextCompat.checkSelfPermission(getContext(),permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    private void fetchValidAwsToken(){
        String awsJson = SPManager.sGetString(Constant.SP_AWS_ACCESS_TOKEN_REV);
        if(TextUtils.isEmpty(awsJson)){
            mPresenter.fetchUploadToken(mAccessType);
        }else{
            AwsAccessTokenBean bean = JsonUtils.deserialize(awsJson, AwsAccessTokenBean.class);
            if(bean != null){
                long previousTime = bean.getHistoryTime();
                long currentTime  = System.currentTimeMillis();
                long durationTime = bean.getDurationSeconds();
                if(currentTime-previousTime >= durationTime*1000){
                    mPresenter.fetchUploadToken(mAccessType);
                }
            }
        }
    }

    @Override
    protected void lazyFetchData() {
        fetchValidAwsToken();
    }

    @Override
    public void startLoading() {
        startProgressDialog(getContext());
    }

    @Override
    public void stopLoading() {
        stopProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void postReviewReasonSuccess(String result) {
        String hint = getContext().getString(R.string.review_order_review_success);
        ToastUtil.showToast(hint);
        EventBus.getDefault().postSticky(new RefreshDataEvent(Constant.EVENT_REFRESH_ORDER_STATE));
        popBackStack();
    }

    @Override
    public void postReviewReasonFail(String result) {
        ToastUtil.showToast(result);
    }

    @Override
    public void fetchUAccessTokenSuccess(AwsAccessTokenBean result) {
        if(result != null){
            result.setHistoryTime(System.currentTimeMillis());
            SPManager.sPutString(Constant.SP_AWS_ACCESS_TOKEN_REV, JsonUtils.serialize(result));
        }
    }

    @Override
    public void fetchUAccessTokenFail(String result) {
        ToastUtil.showToast(result);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1){
            switch (requestCode) {
                case PhotoUtils.INTENT_CROP:
                case PhotoUtils.INTENT_TAKE:
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        photoUtils.onActivityResult(getActivity(), requestCode, resultCode, data,mImageUri);     //使用uri路径回调
                    }else{
                        photoUtils.onActivityResult(getActivity(),requestCode,resultCode,data,mCamerFilePath);    //使用真实路径回调
                    }
                    break;
                case PhotoUtils.INTENT_SELECT:
                    photoUtils.onActivityResult(this,requestCode,resultCode,data);
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }

}
