package com.project.app.fragment.account;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.basemodel.base.BaseUserInfo;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ToastUtil;
import com.hb.basemodel.view.DelEditView;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.project.app.R;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.AwsAccessTokenBean;
import com.project.app.bean.UserRegistInfoBean;
import com.project.app.contract.UpdateProfileContract;
import com.project.app.presenter.UpdateProfilePresenter;
import com.project.app.ui.dialog.DateSelectDialog;
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
import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 上传头像
 */
public class UpdateProfileFragment extends BaseMvpQmuiFragment<UpdateProfilePresenter> implements UpdateProfileContract.View {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_genderMale)
    ImageView iv_genderMale;
    @BindView(R.id.iv_genderFemale)
    ImageView iv_genderFemale;
    @BindView(R.id.iv_genderSecret)
    ImageView iv_genderSecret;
    @BindView(R.id.qrb_updateProfile_save)
    QMUIRoundButton qrb_updateProfile_save;
    @BindView(R.id.iv_updateProfile_avatar)
    QMUIRadiusImageView iv_updateProfile_avatar;
    @BindView(R.id.dev_updateProfile_firstName)
    DelEditView dev_updateProfile_firstName;
    @BindView(R.id.dev_updateProfile_lastName)
    DelEditView dev_updateProfile_lastName;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;

    private DateSelectDialog mDateDialog;
    public static final int  RESPONSE_FETCH_REGIST_INFO  = 0;
    public static final int  RESPONSE_UPDATE_REGIST_INFO = 1;
    private static final int AWS_UPDATE_AVATAR_SUCCESS   = 1;
    private static final int AWS_UPDATE_AVATAR_FAIL      = 2;
    private static final int AWS_UPLOAD_BEGING           = 3;

    private Context mContext;
    private List<ImageView> mImageStyles;  //1 男, 2 女, 3 保密
    private int mGenderIndex   = 1;          //默认选择男
    private int mBirthdayDay   = 1;
    private int mBirthdayMonth = 1;
    private Uri mImageUri;
    private PhotoUtils photoUtils;
    private final int compassSize = 100;
    private File mTargetFile;
    private FileManager fileManager;
    private ThreadLoopUtil mTreadLoopUtil;
    private String mCancel = "";
    private String mCamerFilePath;
    private String[] selectItemList = new String[]{};
    private final String[] mPermission_storage = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case AWS_UPDATE_AVATAR_SUCCESS:
                    String url = msg.getData().getString("awsurl");
                    ImageLoader.getInstance().displayImage(iv_updateProfile_avatar,url,R.mipmap.allwees_ic_default_goods);
                    url = FilterUpProfilePhotoUrl.filterS3SinglePhotoUrl(url);
                    UserUtil.getInstance().setUserAvatorUrl(url);
                    mPresenter.updateRegistAvatar(url);   //上传头像
                    break;
                case AWS_UPLOAD_BEGING:    //上传图片
                    beginUpload();
                    break;
                case AWS_UPDATE_AVATAR_FAIL:
                    String failReason = getContext().getString(R.string.refund_upload_to_fail);
                    ToastUtil.showToast(failReason);
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_update_profile;
    }

    @Override
    public void initView() {
        initTopbar();
        initWidget();
        initData();
    }

    private void initTopbar() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
    }

    private void initWidget() {
        mContext = getContext();
        mPresenter = new UpdateProfilePresenter();
        mPresenter.attachView(this);

        mImageStyles = new ArrayList<>();
        mImageStyles.add(iv_genderMale);
        mImageStyles.add(iv_genderFemale);
        mImageStyles.add(iv_genderSecret);
        fileManager = new FileManager(getContext());
        qrb_updateProfile_save.setChangeAlphaWhenPress(true);

        mDateDialog = new DateSelectDialog(getContext(), new DateSelectDialog.IDateSelectListener() {
            @Override
            public void sure(int sureMonth, int sureDay) {
                mBirthdayDay   = sureDay;
                mBirthdayMonth = sureMonth;
                StringBuffer sbDate = new StringBuffer();
                if(sureMonth < 10){
                    sbDate.append("0" + sureMonth);
                }else{
                    sbDate.append(sureMonth);
                }
                sbDate.append("-");
                if(sureDay <10){
                    sbDate.append("0" + sureDay);
                }else{
                    sbDate.append(sureDay);
                }
                tv_birthday.setText(sbDate.toString());
            }
            @Override
            public void cancel() {
                mDateDialog.dismiss();
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

    private void initData() {
        mCancel = getContext().getResources().getString(R.string.str_cancel);
        selectItemList = getContext().getResources().getStringArray(R.array.refund_method);
        BaseUserInfo meInfo = UserUtil.getInstance().getBaseUserInfo();
        if(!TextUtils.isEmpty(meInfo.getAvatar())){
            ImageLoader.getInstance().displayImage(iv_updateProfile_avatar,meInfo.getAvatar(),R.mipmap.allwees_ic_default_goods);
        }
    }

    private void beginUpload(){
        startLoading();
        mTreadLoopUtil = new ThreadLoopUtil(Constant.S3Type.getUpType(0));     //上传头像
        mTreadLoopUtil.startUploadFile(mTargetFile, new ThreadLoopUtil.CallbackListener() {
            @Override
            public void s3UploadSuccess(String result) {
                stopLoading();
                Message msg = mHandler.obtainMessage(AWS_UPDATE_AVATAR_SUCCESS);
                Bundle bundle = new Bundle();
                bundle.putString("awsurl",result);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
            @Override
            public void s3UploadFail(String result) {
                stopLoading();
                mHandler.sendEmptyMessage(AWS_UPDATE_AVATAR_FAIL);
            }
        });
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchRegistInfo();
        expireAwsToken();
    }

    /**
     * 判断存储的上传头像的token有没有过期
     */
    private void expireAwsToken() {
        String mAccessType = "avr";
        String awsJson = SPManager.sGetString(Constant.SP_AWS_ACCESS_TOKEN_AVR);
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

    @OnClick({R.id.iv_back,R.id.iv_updateProfile_avatar,R.id.qrb_updateProfile_save,R.id.ll_genderMale,R.id.ll_genderFemale,R.id.ll_genderSecret,R.id.tv_birthday})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
            case R.id.iv_updateProfile_avatar:
                showSimpleBottomSheetList(true, true, false,
                        getResources().getString(R.string.app_please_up_method), selectItemList.length,
                        false, false);
                break;
            case R.id.qrb_updateProfile_save:
                postUpdateServer();
                break;
            case R.id.ll_genderMale:
                mGenderIndex = 0;
                resetImageStyle();
                break;
            case R.id.ll_genderFemale:
                mGenderIndex = 1;
                resetImageStyle();
                break;
            case R.id.ll_genderSecret:
                mGenderIndex = 2;
                resetImageStyle();
                break;
            case R.id.tv_birthday:
                mDateDialog.show();
                break;
        }
    }

    public void resetImageStyle(){
        for(ImageView cell:mImageStyles){
            cell.setSelected(false);
        }
        mImageStyles.get(mGenderIndex).setSelected(true);
    }

    private void inflateUserInfo(UserRegistInfoBean result) {
        StringBuffer sb_birthday = new StringBuffer();

        if(!TextUtils.isEmpty(result.getFirstName())){
            dev_updateProfile_firstName.editText.setText(result.getFirstName());
        }else{
            String userFirstName = UserUtil.getInstance().getBaseUserInfo().getFirstName();
            if(!TextUtils.isEmpty(userFirstName)){
                dev_updateProfile_firstName.editText.setText(userFirstName);
            }
        }
        if(!TextUtils.isEmpty(result.getLastName())){
            dev_updateProfile_lastName.editText.setText(result.getLastName());
        }else{
            String userLastName  = UserUtil.getInstance().getBaseUserInfo().getLastName();
            if(!TextUtils.isEmpty(userLastName)){
                dev_updateProfile_lastName.editText.setText(userLastName);
            }
        }
        mBirthdayDay   = result.getBirthdayDay();
        mBirthdayMonth = result.getBirthdayMonth();
        String showMonth = "";
        String showDay   = "";

        if(mBirthdayMonth <10){
            showMonth = "0" + mBirthdayMonth;
        }
        if(mBirthdayDay <10){
            showDay = "0" + mBirthdayDay;
        }
        sb_birthday.append(showMonth + "-" + showDay);
        tv_birthday.setText(sb_birthday.toString());

        int sexModel = result.getGender();
        sexModel--;
        if(sexModel >=3){
            mGenderIndex = 2;
        }else if(sexModel <=0){
            mGenderIndex = 0;
        }else{
            mGenderIndex = sexModel;
        }
        resetImageStyle();   //设置账号密码
    }

    /**
     * 上传信息到服务器
     */
    private void postUpdateServer() {
        String updateFirstName = dev_updateProfile_firstName.editText.getText().toString().trim();
        String updateLastName  = dev_updateProfile_lastName.editText.getText().toString().trim();
        String updateBirthday  = tv_birthday.getText().toString().trim();

        if(TextUtils.isEmpty(updateFirstName)){
            String emptyFirstNameHint = mContext.getResources().getString(R.string.update_profile_hint_enter_first_name);
            ToastUtil.showToast(emptyFirstNameHint);
            return;
        }
        if(TextUtils.isEmpty(updateLastName)){
            String emptyFirstNameHint = mContext.getResources().getString(R.string.update_profile_hint_enter_last_name);
            ToastUtil.showToast(emptyFirstNameHint);
            return;
        }
        if(TextUtils.isEmpty(updateBirthday)){
            String emptyBirthdayHint = mContext.getResources().getString(R.string.update_profile_hint_enter_birthday);
            ToastUtil.showToast(emptyBirthdayHint);
            return;
        }
        mPresenter.updateRegistInfo(String.valueOf(mBirthdayDay),String.valueOf(mBirthdayMonth),updateFirstName,updateLastName,String.valueOf(mGenderIndex + 1));
    }

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
                            photoUtils.takePictureWithQ(com.project.app.fragment.account.UpdateProfileFragment.this, mImageUri);
                        } else {
                            photoUtils.takePicture(com.project.app.fragment.account.UpdateProfileFragment.this,mImageUri);
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
            photoUtils.selectPicture(com.project.app.fragment.account.UpdateProfileFragment.this);
        }
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
                    mRecodePosition = position;
                    choicePhotoByUser(position);
                });
        for(int i=0;i<itemCount;i++){
            builder.addItem(selectItemList[i]);
        }
        builder.build().show();
    }
    private int mRecodePosition = 0;

    @Override
    public void fetchRegistInfoSuccess(UserRegistInfoBean result) {
        if(result == null){
            return;
        }
        inflateUserInfo(result);
    }

    @Override
    public void updateRegistInfoSuccess(String result) {
        EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_MODIFY_USERINFO_SUCCESS));   //刷新用户信息
        ToastUtil.showToast(result);
    }

    @Override
    public void updateRegistAvatarSuccess() {
        EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_MODIFY_USERINFO_SUCCESS));
    }

    @Override
    public void fetchFail(int mode, String failMsg) {
        switch (mode){
            case RESPONSE_FETCH_REGIST_INFO:     //获取基本信息失败
                ToastUtil.showToast(failMsg);
                break;
            case RESPONSE_UPDATE_REGIST_INFO:
                ToastUtil.showToast(failMsg);
                break;
        }
    }

    /**
     * 获取上传头像token成功
     * @param result
     */
    @Override
    public void fetchUAccessTokenSuccess(AwsAccessTokenBean result) {
        if(result != null){
            result.setHistoryTime(System.currentTimeMillis());
            SPManager.sPutString(Constant.SP_AWS_ACCESS_TOKEN_AVR, JsonUtils.serialize(result));    //存储到头像token信息
        }
    }

    @Override
    public void fetchUAccessTokenFail(String result) {

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

    /**
     * 获取头像后进行回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.onDestoryView();
            mPresenter = null;
            System.gc();
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
