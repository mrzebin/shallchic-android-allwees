package com.project.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.project.app.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;
import androidx.fragment.app.Fragment;


/**
 * [从本地选择图片以及拍照工具类，完美适配2.0-5.0版本]
 *
 **/
public class PhotoUtils {

    /**
     * 裁剪图片成功后返回
     **/
    public static final int INTENT_CROP = 2;
    /**
     * 拍照成功后返回
     **/
    public static final int INTENT_TAKE = 3;
    /**
     * 拍照成功后返回
     **/
    public static final int INTENT_SELECT = 4;
    public static final String CROP_FILE_NAME = "crop_file.jpg";
    public static final String RANDOM_FILE_NAME = "";
    //不需要裁剪图片
    public static final int NO_CROP = 0x1772;
    private final String tag = PhotoUtils.class.getSimpleName();
    private int mType;

    /**
     * PhotoUtils对象
     **/
    private OnPhotoResultListener onPhotoResultListener;


    public PhotoUtils(OnPhotoResultListener onPhotoResultListener) {
        this.onPhotoResultListener = onPhotoResultListener;
    }

    public PhotoUtils(OnPhotoResultListener onPhotoResultListener, int type) {
        this.onPhotoResultListener = onPhotoResultListener;
        mType = type;
    }

    /**
     * 拍照
     *
     * @param
     * @return
     */
    public void takePicture(Activity activity) {
        try {
            //每次选择图片吧之前的图片删除
            clearCropFile(buildUri(activity));
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, buildUri(activity));
            if (!isIntentAvailable(activity, intent)) {
                return;
            }
            activity.startActivityForResult(intent, INTENT_TAKE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拍照
     *
     * @param
     * @return
     */
    public void takePictureWithQ(Activity activity, Uri imgUri) {
        try {
            //每次选择图片吧之前的图片删除
            clearCropFile(imgUri);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
            if (!isIntentAvailable(activity, intent)) {
                return;
            }
            activity.startActivityForResult(intent, INTENT_TAKE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拍照
     *
     * @param
     * @return
     */
    public void takePictureWithQ(Fragment fragment, Uri imgUri) {
        try {
            //每次选择图片吧之前的图片删除
            clearCropFile(imgUri);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            if (!isIntentAvailable(fragment, intent)) {
                return;
            }
            fragment.startActivityForResult(intent, INTENT_TAKE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 拍照
     *
     * @param
     * @return
     */
    public void takePicture(Fragment fragment, Uri imgUri) {
        try {
            clearCropFile(buildUri(fragment.getActivity()));
//            clearCropFile(buildLocalFileUri());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);

            if (!isIntentAvailable(fragment.getActivity(), intent)) {
                return;
            }
            fragment.startActivityForResult(intent, INTENT_TAKE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拍照
     *
     * @param
     * @return
     */
    public void takePicture(Activity activity, Uri imgUri) {
        try {
            clearCropFile(buildUri(activity));
//            clearCropFile(buildLocalFileUri());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);

            if (!isIntentAvailable(activity, intent)) {
                return;
            }
            activity.startActivityForResult(intent, INTENT_TAKE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    /***
     * 选择一张图片
     * 图片类型，这里是image/*，当然也可以设置限制
     * 如：image/jpeg等
     *
     * @param activity Activity
     */
    @SuppressLint("InlinedApi")
    public void selectPicture(Activity activity) {
        try {
            clearCropFile(buildLocalFileUri());
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            if (!isIntentAvailable(activity, intent)) {
                return;
            }
            activity.startActivityForResult(intent, INTENT_SELECT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * 选择一张图片
     * 图片类型，这里是image/*，当然也可以设置限制
     * 如：image/jpeg等
     *
     * @param fragment Fragment
     */
    @SuppressLint("InlinedApi")
    public void selectPicture(Fragment fragment) {
        try {
            //每次选择图片吧之前的图片删除
            clearCropFile(buildLocalFileUri());
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            if (!isIntentAvailable(fragment.getActivity(), intent)) {
                return;
            }
            fragment.startActivityForResult(intent, INTENT_SELECT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建uri
     *
     * @param activity
     * @return
     */
    public Uri buildUri(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return createImageUri(activity);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return FileProvider.getUriForFile(activity,
                        activity.getPackageName() + activity.getResources().getString(R.string.rc_authorities_fileprovider)
                        , new File(Environment.getExternalStorageDirectory().getPath() + File.separator + CROP_FILE_NAME));
            } else {
                return Uri.fromFile(Environment.getExternalStorageDirectory()).buildUpon().appendPath(CROP_FILE_NAME).build();
            }
        }
    }

    /**
     * 创建保存图片的文件
     */
    public File createImageFile(Context context){
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".jpg";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri(Context activity) {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return activity.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }


    /**
     * 构建本地文件uri
     *
     * @return
     */
    private Uri buildLocalFileUri() {
        return Uri.fromFile(Environment.getExternalStorageDirectory()).buildUpon().appendPath(CROP_FILE_NAME).build();
    }


    /**
     * @param intent
     * @return
     */
    protected boolean isIntentAvailable(Activity activity, Intent intent) {
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * @param intent
     * @return
     */
    protected boolean isIntentAvailable(Fragment activity, Intent intent) {
        PackageManager packageManager = activity.getActivity().getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    private boolean corp(Activity activity, Uri uri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 200);
        cropIntent.putExtra("outputY", 200);
        cropIntent.putExtra("return-data", false);
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        Uri cropuri = buildLocalFileUri();
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, cropuri);
        if (!isIntentAvailable(activity, cropIntent)) {
            return false;
        } else {
            try {
                activity.startActivityForResult(cropIntent, INTENT_CROP);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }


    private boolean corp(Fragment fragment, Uri uri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 200);
        cropIntent.putExtra("outputY", 200);
        cropIntent.putExtra("return-data", false);
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        Uri cropuri = buildLocalFileUri();
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, cropuri);
        if (!isIntentAvailable(fragment.getActivity(), cropIntent)) {
            return false;
        } else {
            try {
                fragment.startActivityForResult(cropIntent, INTENT_CROP);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 返回结果处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data, Uri imgUri) {
        if (onPhotoResultListener == null) {
            Log.e(tag, "onPhotoResultListener is not null");
            return;
        }
        switch (requestCode) {
            case INTENT_TAKE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (imgUri != null) {
                        if (mType == NO_CROP) {
                            onPhotoResultListener.onPhotoResult(imgUri,INTENT_TAKE);
                            return;
                        }
                        if (corp(activity,imgUri)) {
                            return;
                        }
                        onPhotoResultListener.onPhotoCancel();
                    }
                } else {
                    if (new File(buildLocalFileUri().getPath()).exists()) {
                        if (mType == NO_CROP) {
                            //不需要裁剪
                            onPhotoResultListener.onPhotoResult(buildLocalFileUri(),INTENT_TAKE);
                            return;
                        }
                        if (corp(activity, buildUri(activity))) {
                            return;
                        }
                        onPhotoResultListener.onPhotoCancel();
                    }
                }
                break;
            //选择图片
            case INTENT_SELECT:
                if (data != null && data.getData() != null) {
                    Uri imageUri = data.getData();
                    //不需要裁剪
                    if (mType == NO_CROP) {
                        onPhotoResultListener.onPhotoResult(imageUri,INTENT_SELECT);
                        return;
                    }
                    if (corp(activity, imageUri)) {
                        return;
                    }
                }
                onPhotoResultListener.onPhotoCancel();
                break;
            //截图
            case INTENT_CROP:
                if (resultCode == Activity.RESULT_OK && new File(buildLocalFileUri().getPath()).exists()) {
                    onPhotoResultListener.onPhotoResult(buildLocalFileUri(),INTENT_CROP);
                }
                break;
        }
    }



    /**
     * 返回结果处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data, String realPath) {
        if (onPhotoResultListener == null) {
            Log.e(tag, "onPhotoResultListener is not null");
            return;
        }
        switch (requestCode) {
            case INTENT_TAKE:
                File realFile = new File(realPath);
                if(realFile.exists()){
                    if (mType == NO_CROP) {
                        onPhotoResultListener.onPhotoResult(Uri.fromFile(realFile),INTENT_TAKE);
                        return;
                    }
                }
                break;
            case INTENT_SELECT:
                if (data != null && data.getData() != null) {
                    Uri imageUri = data.getData();
                    if (mType == NO_CROP) {
                        onPhotoResultListener.onPhotoResult(imageUri,INTENT_SELECT);
                        return;
                    }
                    if (corp(activity, imageUri)) {
                        return;
                    }
                }
                onPhotoResultListener.onPhotoCancel();
                break;
            case INTENT_CROP:
                if (resultCode == Activity.RESULT_OK && new File(buildLocalFileUri().getPath()).exists()) {
                    onPhotoResultListener.onPhotoResult(buildLocalFileUri(),INTENT_CROP);
                }
                break;
        }
    }




    /**
     * 返回结果处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
        if (onPhotoResultListener == null) {
            Log.e(tag, "onPhotoResultListener is not null");
            return;
        }
        switch (requestCode) {
            //拍照
            case INTENT_TAKE:
                if (new File(buildLocalFileUri().getPath()).exists()) {
                    //不需要裁剪
                    if (mType == NO_CROP) {
                        onPhotoResultListener.onPhotoResult(buildLocalFileUri(),INTENT_TAKE);
                        return;
                    }
                    if (corp(fragment, buildUri(fragment.getActivity()))) {
                        return;
                    }
                    onPhotoResultListener.onPhotoCancel();
                }
                break;
            //选择图片
            case INTENT_SELECT:
                if (data != null && data.getData() != null) {
                    Uri imageUri = data.getData();
                    //不需要裁剪
                    if (mType == NO_CROP) {
                        onPhotoResultListener.onPhotoResult(imageUri,INTENT_SELECT);
                        return;
                    }
                    if (corp(fragment, imageUri)) {
                        return;
                    }
                }
                onPhotoResultListener.onPhotoCancel();
                break;

            //截图
            case INTENT_CROP:
                if (resultCode == Activity.RESULT_OK && new File(buildLocalFileUri().getPath()).exists()) {
                    onPhotoResultListener.onPhotoResult(buildLocalFileUri(),INTENT_CROP);
                }
                break;
        }
    }


    /**
     * 删除文件
     *
     * @param uri
     * @return
     */
    public boolean clearCropFile(Uri uri) {
        if (uri == null) {
            return false;
        }

        File file = new File(uri.getPath());
        if (file.exists()) {
            boolean result = file.delete();
            if (result) {
                Log.i(tag, "Cached crop file cleared.");
            } else {
                Log.e(tag, "Failed to clear cached crop file.");
            }
            return result;
        } else {
            Log.w(tag, "Trying to clear cached crop file but it does not exist.");
        }

        return false;
    }

    public OnPhotoResultListener getOnPhotoResultListener() {
        return onPhotoResultListener;
    }

    public void setOnPhotoResultListener(OnPhotoResultListener onPhotoResultListener) {
        this.onPhotoResultListener = onPhotoResultListener;
    }



    /**
     * [回调监听类]
     *
     * @author huxinwu
     * @version 1.0
     * @date 2015-1-7
     **/
    public interface OnPhotoResultListener {
        void onPhotoResult(Uri uri,int type);
        void onPhotoCancel();
    }

}
