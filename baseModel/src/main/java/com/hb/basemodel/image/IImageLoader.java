package com.hb.basemodel.image;

import android.content.Context;
import android.widget.ImageView;

public interface IImageLoader {
    void displayImage(ImageView target, String url);

    void displayImage(ImageView target, String url, ImageConfig config);

    void loadImageData(Context context, String url, LoadListener loadListener);
}
