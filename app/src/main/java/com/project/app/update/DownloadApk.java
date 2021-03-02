package com.project.app.update;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.SPManager;

import java.io.File;

public class DownloadApk {
    private Uri mSaveFileUri;
    private static com.project.app.update.ApkInstallReceiver apkInstallReceiver;

    /**
     * 下载APK文件
     * @param context
     * @param url
     * @param title
     * @param appName
     */
    public static void downloadApk(Context context, String url, String title,final String appName) {
        long downloadId = SPManager.sGetLong(DownloadManager.EXTRA_DOWNLOAD_ID,-1L);
        File targetFile = new File(SPManager.sGetString(Constant.SP_DOWNLOAD_ROOT));

        if(targetFile.exists()){
            targetFile.delete();
        }

        if(downloadId != -1) {
            //存在downloadId
            com.project.app.update.DownLoadUtils downLoadUtils = com.project.app.update.DownLoadUtils.getInstance(context);
            //获取当前状态
            int status = downLoadUtils.getDownloadStatus(downloadId);

            if(DownloadManager.STATUS_SUCCESSFUL == status) {
                //状态为下载成功
                //获取下载路径URI
                Uri downloadUri = downLoadUtils.getDownloadUri(downloadId);

                if(null != downloadUri) {
                    //存在下载的APK，如果两个APK相同，启动更新界面。否之则删除，重新下载。
                    if(compare(getApkInfo(context,downloadUri.getPath()),context)) {
                        startInstall(context, downloadUri);
                        return;
                    } else {
                        //删除下载任务以及文件
                        downLoadUtils.getDownloadManager().remove(downloadId);
                    }
                }
                start(context, url, title,appName);
            }else if(DownloadManager.STATUS_FAILED == status) {
                start(context, url, title,appName);
            }else {
                if(againDownloadApk(context,url,appName)){
                    start(context,url,title,appName);
                }
            }
        } else {
            //不存在downloadId，没有下载过APK
            start(context, url, title,appName);
        }
    }

    //判断文件有没有删除,如果删除则重新下载
    private static boolean againDownloadApk(Context context, String url, String appName) {
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cancer/" + appName + ".apk";
        File targetFile = new File(targetPath);
        if(targetFile.exists()){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 开始下载
     * @param context
     * @param url
     * @param title
     * @param appName
     */
    private static void start(Context context, String url, String title,String appName) {
        if(hasSDKCard()) {
            long id = com.project.app.update.DownLoadUtils.getInstance(context).download(url,title, "下载完成后点击打开", appName);
            SPManager.sPutLong(DownloadManager.EXTRA_DOWNLOAD_ID,id);
        } else {
            Toast.makeText(context,"手机未安装SD卡，下载失败",Toast.LENGTH_LONG).show();
        }
    }

    public static void registerBroadcast(Context context) {
        apkInstallReceiver = new com.project.app.update.ApkInstallReceiver();
        context.registerReceiver(apkInstallReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public static void unregisterBroadcast(Context context) {
        if(null != apkInstallReceiver) {
            context.unregisterReceiver(apkInstallReceiver);
        }
    }

    /**
     * 跳转到安装界面
     * @param context
     * @param uri
     */
    private static void startInstall(Context context, Uri uri) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }

    /**
     * 获取APK程序信息
     * @param context
     * @param path
     * @return
     */
    private static PackageInfo getApkInfo(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if(null != pi) {
            return pi;
        }
        return null;
    }


    /**
     * 比较两个APK的信息
     * @param apkInfo
     * @param context
     * @return
     */
    private static boolean compare(PackageInfo apkInfo,Context context) {

        if(null == apkInfo) {
            return false;
        }
        String localPackageName = context.getPackageName();
        if(localPackageName.equals(apkInfo.packageName)) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(localPackageName, 0);
                //比较当前APK和下载的APK版本号
                if (apkInfo.versionCode > packageInfo.versionCode) {
                    //如果下载的APK版本号大于当前安装的APK版本号，返回true
                    return true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 是否存在SD卡
     */
    private static boolean hasSDKCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 删除已下载的文件
     */
    public static void removeFile(Context context) {
        String filePath = SPManager.sGetString(Constant.SP_DOWNLOAD_ROOT);

        if(null != filePath && !filePath.equals("")) {
            File downloadFile = new File(filePath);
            if(null != downloadFile && downloadFile.exists()) {
                //删除之前先判断用户是否已经安装了，安装了才删除。
                if(!compare(getApkInfo(context,filePath),context)) {
                    downloadFile.delete();
                    Log.e("----", "已删除");
                }
            }
        }
    }
}
