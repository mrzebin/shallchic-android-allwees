package com.project.app.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.hb.basemodel.config.Constant;

public class WhatsAppUtil {

    public static void chatInWhatsApp(Context mContext, String mobileNum) {
        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + mobileNum));
            intent.setPackage(Constant.PACKAGE_NAME_WAHTS);
            mContext.startActivity(intent);
        } catch (Exception e) { //  没有安装WhatsApp
            e.printStackTrace();
        }
    }
}
