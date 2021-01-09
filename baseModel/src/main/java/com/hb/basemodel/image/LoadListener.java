package com.hb.basemodel.image;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public interface LoadListener {
    void onLoadSuccess(Bitmap bitmap, Drawable drawable);

    void onLoadFailed(Drawable errorDrawable);
}
