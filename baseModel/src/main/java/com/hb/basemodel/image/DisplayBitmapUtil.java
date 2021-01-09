package com.hb.basemodel.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class DisplayBitmapUtil {
    private final Context context;

    public DisplayBitmapUtil(Context context) {
        this.context = context;
    }

    /**
     * 给ImageView设置bitmap 这里主要实现压缩
     */
    public void displayBitmap(final View target, final Bitmap src) {
        getViewSize(target, new OnSizeReadyListener() {
            @Override
            public void onSizeReady(int[] size) {
                if (target instanceof ImageView) {
                    ((ImageView) target).setImageBitmap(compress(src, size));
                } else {
                    target.setBackground(new BitmapDrawable(context.getResources(), src));
                }
            }
        });
    }

    private Bitmap compress(Bitmap src, int[] size) {
        int requestWidth = size[0];
        int requestHeight = size[1];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        src.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bitmapDta = baos.toByteArray();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bitmapDta, 0, baos.size(), options);
        int realWidth = options.outWidth;
        int realHeight = options.outHeight;
        int simple = (int) Math.max((float) realWidth / (float) requestWidth, (float) realHeight / (float) requestHeight);
        options.inJustDecodeBounds = false;
        options.inSampleSize = simple;
        return BitmapFactory.decodeByteArray(bitmapDta, 0, baos.size(), options);
    }

    private void getViewSize(final View target, final OnSizeReadyListener listener) {
        final int[] size = new int[2];
        ViewGroup.LayoutParams params = target.getLayoutParams();
        int width = -1;
        int height = -1;
        if (params != null) {
            width = params.width;
            height = params.height;
        }
        if (width > 0 && height > 0) {
            size[0] = width;
            size[1] = height;
            if (listener != null) {
                listener.onSizeReady(size);
                return;
            }
        }
        target.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                size[0] = target.getMeasuredWidth();
                size[1] = target.getMeasuredHeight();
                if (listener != null) {
                    listener.onSizeReady(size);
                }
                return true;
            }
        });
    }

    private interface OnSizeReadyListener {
        void onSizeReady(int[] size);
    }
}
