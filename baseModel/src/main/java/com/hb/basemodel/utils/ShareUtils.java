package com.hb.basemodel.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;


public class ShareUtils {

    private static final String EMAIL_ADDRESS = "";

    public static void shareText(Context context, String title, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, title));
    }

    public static void shareImage(Context context, String title ,Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(intent, title));
    }

    public static void sendEmail(Context context, String title) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + EMAIL_ADDRESS));
        context.startActivity(Intent.createChooser(intent, title));
    }

    public static void sendMoreImage(Context context, ArrayList<Uri> imageUris, String title) {
        Intent mulIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        mulIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        mulIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(mulIntent, "多图文件分享"));
    }
}