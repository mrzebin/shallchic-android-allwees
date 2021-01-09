package com.project.app.update;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;


import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.SPManager;

import java.io.File;

public class DownLoadUtils {
    private Context mContext;
    private DownloadManager mDownloadManager;
    private static volatile DownLoadUtils instance;

    private DownLoadUtils(Context context) {
        this.mContext = context.getApplicationContext();
        mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    /**
     * 获取单例对象
     * @param context
     * @return
     */
    public static DownLoadUtils getInstance(Context context) {
        if(instance == null) {
            synchronized (DownLoadUtils.class) {
                if(instance == null) {
                    instance = new DownLoadUtils(context);
                    return instance;
                }
            }
        }
        return instance;
    }

    /**
     * 下载
     * @param uri
     * @param title
     * @param description
     * @param appName
     * @return downloadId
     */
    public long download(String uri, String title, String description,String appName) {
        //1.构建下载请求
        DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(uri));
        downloadRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        /**设置漫游状态下是否可以下载*/
        downloadRequest.setAllowedOverRoaming(false);
        /**如果我们希望下载的文件可以被系统的Downloads应用扫描到并管理，
         我们需要调用Request对象的setVisibleInDownloadsUi方法，传递参数true.*/
        downloadRequest.setVisibleInDownloadsUi(true);
        //文件保存位置
        //file:///storage/emulated/0/Android/data/your-package/files/Download/appName.apk
//        downloadRequest.setDestinationInExternalFilesDir(mContext, Environment.getExternalStorageDirectory().getAbsolutePath()+"/cancer", appName + ".apk");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            File rootDirFile = mContext.getExternalFilesDir("AllWees");
            if(!rootDirFile.exists()){
                rootDirFile.mkdirs();
            }
            downloadRequest.setDestinationUri(Uri.fromFile(new File(rootDirFile,appName)));
            SPManager.sPutString(Constant.SP_DOWNLOAD_ROOT,rootDirFile.getAbsolutePath() + "/" + appName);
        }else{
            downloadRequest.setDestinationUri(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/AllWees/"+ appName)));
            SPManager.sPutString(Constant.SP_DOWNLOAD_ROOT,Environment.getExternalStorageDirectory().getAbsolutePath()+"/AllWees/" + appName );
        }
        // 设置一些基本显示信息
        downloadRequest.setTitle(title);
        downloadRequest.setDescription(description);
        downloadRequest.setMimeType("application/vnd.android.package-archive");
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        return mDownloadManager.enqueue(downloadRequest);           //异步请求
    }

    //这里的fileName指文件名，不包含路径
    //relativePath 包含某个媒体下的子路径
//    private Uri insertFileIntoMediaStore (String fileName, String fileType,String relativePath) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//            return null;
//        }
//        ContentResolver resolver = mContext.getContentResolver();
//        //设置文件参数到ContentValues中
//        ContentValues values = new ContentValues();
//        //设置文件名
//        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
//        //设置文件描述，这里以文件名为例子
//        values.put(MediaStore.Downloads.DESCRIPTION, fileName);
//        //设置文件类型
//        values.put(MediaStore.Downloads.MIME_TYPE,"application/vnd.android.package-archive");
//        //注意RELATIVE_PATH需要targetVersion=29
//        //故该方法只可在Android10的手机上执行
//        values.put(MediaStore.Downloads.RELATIVE_PATH, relativePath);
//        //EXTERNAL_CONTENT_URI代表外部存储器
//        Uri external = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
//        //insertUri表示文件保存的uri路径
//        Uri insertUri  = resolver.insert(external, values);
//        return insertUri;
//    }


    /**
     * 获取文件下载路径
     * @param downloadId
     * @return
     */
    public String getDownloadPath(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = mDownloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                }
            } finally {
                c.close();
            }
        }
        return null;
    }

    /**
     * 获取文件保存的地址
     * @param downloadId
     * @return
     */
    public Uri getDownloadUri(long downloadId) {
        return mDownloadManager.getUriForDownloadedFile(downloadId);
    }

    public DownloadManager getDownloadManager() {
        return mDownloadManager;
    }

    /**
     * 获取下载状态
     * @param downloadId
     * @return
     */
    public int getDownloadStatus(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = mDownloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                }
            } finally {
                c.close();
            }
        }
        return -1;
    }

    /**
     * 判断下载管理程序是否可用
     * @return
     */
    public boolean canDownload() {
        try {
            int state = mContext.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 进入 启用/禁用 下载管理程序界面
     */
    public void skipToDownloadManager() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.setData(Uri.parse("package:" + packageName));
        mContext.startActivity(intent);
    }

}
