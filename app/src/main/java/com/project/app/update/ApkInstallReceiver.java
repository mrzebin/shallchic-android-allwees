package com.project.app.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.webkit.MimeTypeMap;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.SPManager;
import com.project.app.R;

import java.io.File;

import androidx.core.content.FileProvider;

/**
 * create by hb
 * 下载完成安装的回调
 */
public class ApkInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            String apkPath     = SPManager.sGetString(Constant.SP_DOWNLOAD_ROOT);
            installApk(context,downloadApkId,apkPath);
        }
    }

    /**
     * 安装apk
     */
    private void installApk(Context context,long downloadId,String apkPath) {
        String applicationId = context.getResources().getString(R.string.application_id);
        long downId = SPManager.sGetLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
        if(downloadId == downId) {
            File apkFile = new File(apkPath);
            Uri uri = null;
            DownloadManager downManager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = downManager.getUriForDownloadedFile(downloadId);
            SPManager.sPutString("downloadApk",downloadUri.getPath());
            Intent install= new Intent(Intent.ACTION_VIEW);
            if (downloadUri != null) {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
                    install.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
                }else{
                    install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    uri = FileProvider.getUriForFile(context,
                            applicationId + ".FileProvider",
                            apkFile);
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    install.setDataAndType(uri, "application/vnd.android.package-archive");
                    //兼容8.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                        if (!hasInstallPermission) {
                            startInstallPermissionSettingActivity(context);
                            return;
                        }
                    }
                }
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            }
        }
    }

    private void startInstallPermissionSettingActivity(Context context) {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        String type = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        try {
            context.startActivity(intent);
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }


    private String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

}
