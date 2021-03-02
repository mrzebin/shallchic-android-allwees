package com.project.app.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * carete by hb
 */
public class PackageCheckUtil {

    /**
     * 检查APP是否已经安装
     * @param context
     * @param packagename 需求检查的APP包名
     * @return  没有安装返回false，已经安装返回true
     */
    public static boolean isAppInstalled(Context context, String packagename) {

        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo == null) {
            //System.out.println("没有安装");
            return false;
        } else {
            //System.out.println("已经安装");
            return true;
        }
    }

    /**
     * 安装apk
     */
    private void install(Context context,String apkname) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String url = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Environment.DIRECTORY_DOWNLOADS + apkname;
            File file = new File(url);
            if(file.exists()) {
                intent.setDataAndType(Uri.fromFile(new File(url)), "application/vnd.android.package-archive");
                context.startActivity(intent);
            } else {
                //安装包已经删除请重新下载
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 打开已经安装好的apk
     * @param url  文件路径
     */
    private void openApk(Context context,String url) {
        try {
            PackageManager manager = context.getPackageManager();
            // 这里的是你下载好的文件路径
            PackageInfo info = manager.getPackageArchiveInfo(url, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                Intent intent = manager.getLaunchIntentForPackage(info.applicationInfo.packageName);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }





}
