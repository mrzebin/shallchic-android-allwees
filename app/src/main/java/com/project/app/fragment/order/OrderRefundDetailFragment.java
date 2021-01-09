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
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.project.app.R;
import com.project.app.adapter.RefundPostPhotosAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.OrderDetailBean;
import com.project.app.bean.RefundAccessTokenBean;
import com.project.app.contract.OrderRefundContract;
import com.project.app.presenter.OrderRefundPresenter;
import com.project.app.s3transferutility.Util;
import com.project.app.utils.FileManager;
import com.project.app.utils.FileUtil;
import com.project.app.utils.PhotoUtils;
import com.project.app.utils.ThreadLoopUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * refund 详情
 */
public class OrderRefundDetailFragment extends BaseMvpQmuiFragment<OrderRefundPresenter> implements OrderRefundContract.View {
    @BindView(R.id.rlv_chooseImg)
    RecyclerView rlv_chooseImg;
    @BindView(R.id.tv_refundType)
    TextView tv_refundType;
    @BindView(R.id.et_reNote)
    EditText et_reNote;
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    private String mOrderId;
    private String mItemUuid;
    private final String mAccessType = "rfd";
    private RefundPostPhotosAdapter mAdapter;
    private Context mContext;
    private Uri mImageUri;
    private static Util util;
    private FileManager fileManager;
    private PhotoUtils photoUtils;
    private String mCode = "2";
    private final int compassSize = 200;
    private String mCamerFilePath;
    private ThreadLoopUtil mTreadLoopUtil;
    private File mTargetFile;

    private static final int LUBAN_COMPASS_SUCCESS = 1;
    private static final int LUBAN_COMPASS_FAIL    = 2;
    private static final int AWS_UPLOAD_BEGING     = 3;
    private static final List<String> upRefunds = new ArrayList<>();
    private String mCancel = "";

    private final List<String> mRefundDatas = new ArrayList<>();
    private String[] selectItemList = new String[]{};
    private String[] mRefundType    = new String[]{};
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
        return R.layout.fragment_refund_detail;
    }

    @Override
    public void initView() {
        initTopbar();
        initWidget();
    }

    private void beginUpload(){
        startLoading();
        mTreadLoopUtil = new ThreadLoopUtil();
        mTreadLoopUtil.startUploadFile(mTargetFile, new ThreadLoopUtil.CallbackListener() {
            @Override
            public void s3UploadSuccess(String result) {
                stopLoading();
                Message msg = mHandler.obtainMessage(LUBAN_COMPASS_SUCCESS);
                Bundle bundle = new Bundle();
                bundle.putString("awsurl",result);
                msg.setData(bundle);
                upRefunds.add(result);
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
        mItemUuid = baba.getString("orderItemUuid");
        mOrderId  = baba.getString("orderUuid");
        mCancel = getContext().getResources().getString(R.string.str_cancel);
        selectItemList = getContext().getResources().getStringArray(R.array.refund_method);
        mRefundType = getContext().getResources().getStringArray(R.array.refund_reason);
    }

    private void initWidget() {
        mContext = getContext();
        mPresenter = new OrderRefundPresenter();
        mPresenter.attachView(this);
        GridLayoutManager manager = new GridLayoutManager(getContext(),4);
        rlv_chooseImg.setLayoutManager(manager);
        mAdapter = new RefundPostPhotosAdapter(mRefundDatas);
        rlv_chooseImg.setAdapter(mAdapter);
        mAdapter.setListener(new RefundPostPhotosAdapter.ChoiceCallbackListener() {
            @Override
            public void choicePhotoByFunction() {
                showSimpleBottomSheetList(true, true, false,
                        getResources().getString(R.string.app_please_up_method), selectItemList.length,
                        false, false);
            }
            @Override
            public void deletePhotoByFunction(int deleteIndex) {
                mAdapter.remove(deleteIndex);
                upRefunds.remove(deleteIndex-1);
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

    @OnClick({R.id.btn_postRefund,R.id.ll_choiceRefundType,R.id.iv_back})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.btn_postRefund:
                checkInputValid();
                break;
            case R.id.ll_choiceRefundType:
                showSelectRefundReason(true, true, false,
                        getResources().getString(R.string.app_please_choice_type), mRefundType.length,
                        false, false);
                break;
            case R.id.iv_back:
                popBackStack();
                break;
        }
    }

    private void checkInputValid() {
        String note = et_reNote.getText().toString().trim();
        if(TextUtils.isEmpty(note)){
            String hint = getContext().getString(R.string.str_nin);
            ToastUtil.showToast(hint);
            return;
        }
        mPresenter.submitRefundReasonToService(mItemUuid,mOrderId,upRefunds,mCode,note,0);
    }

    private void showSelectRefundReason(boolean gravityCenter,
                                           boolean addCancelBtn,
                                           boolean withIcon,
                                           CharSequence title,
                                           int itemCount,
                                           boolean allowDragDismiss,
                                           boolean withMark) {
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(mContext);
        builder.setGravityCenter(gravityCenter)
                .setSkinManager(QMUISkinManager.defaultInstance(mContext))
                .setTitle(title)
                .setAddCancelBtn(addCancelBtn)
                .setAllowDrag(allowDragDismiss)
                .setNeedRightMark(withMark)
                .setCancelText(mCancel)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    mCode = position + 2 + "";
                    tv_refundType.setText(mRefundType[position]);
                });
        for(int i=0;i<itemCount;i++){
            builder.addItem(mRefundType[i]);
        }
        builder.build().show();
    }

    private void showSimpleBottomSheetList(boolean gravityCenter,
                                           boolean addCancelBtn,
                                           boolean withIcon,
                                           CharSequence title,
                                           int itemCount,
                                           boolean allowDragDismiss,
                                           boolean withMark) {
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(mContext);
        builder.setGravityCenter(gravityCenter)
                .setSkinManager(QMUISkinManager.defaultInstance(mContext))
                .setTitle(title)
                .setAddCancelBtn(addCancelBtn)
                .setAllowDrag(allowDragDismiss)
                .setNeedRightMark(withMark)
                .setCancelText(mCancel)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    if(position == 0){
                        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(getActivity(),mPermission_storage,1);
                                return;
                            }
                            if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(getActivity(),mPermission_storage,1);
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
                                        photoUtils.takePictureWithQ(OrderRefundDetailFragment.this, mImageUri);
                                    } else {
                                        photoUtils.takePicture(OrderRefundDetailFragment.this,mImageUri);
                                    }
                                }
                            }
                        }
                    }else if(position == 1){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(getActivity(),mPermission_storage,1);
                                return;
                            }
                        }
                        photoUtils.selectPicture(OrderRefundDetailFragment.this);
                    }
                });
        for(int i=0;i<itemCount;i++){
            builder.addItem(selectItemList[i]);
        }
        builder.build().show();
    }

    private void fetchValidAwsToken(){
        String awsJson = SPManager.sGetString(Constant.SP_AWS_ACCESS_TOKEN_RFD);
        if(TextUtils.isEmpty(awsJson)){
            mPresenter.fetchUploadToken(mAccessType);
        }else{
            RefundAccessTokenBean bean = JsonUtils.deserialize(awsJson,RefundAccessTokenBean.class);
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
    public void postRefundSuccess(OrderDetailBean result) {
        String hint = getContext().getString(R.string.refund_success);
        ToastUtil.showToast(hint);
        popBackStack();
        EventBus.getDefault().postSticky(new RefreshDataEvent(Constant.EVENT_REFUND_SUCCESS));
    }

    @Override
    public void postRefundFail(String result) {
        ToastUtil.showToast(result);
    }

    @Override
    public void fetchUAccessTokenSuccess(RefundAccessTokenBean result) {
        if(result != null){
            result.setHistoryTime(System.currentTimeMillis());
            SPManager.sPutString(Constant.SP_AWS_ACCESS_TOKEN_RFD, JsonUtils.serialize(result));
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
//                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//                        photoUtils.onActivityResult(getActivity(), requestCode, resultCode, data,mImageUri);     //使用uri路径回调
//                    }else{
//                        photoUtils.onActivityResult(getActivity(),requestCode,resultCode,data,mCamerFilePath);    //使用真实路径回调
//                    }
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
