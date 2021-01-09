package com.project.app.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CropCountryBitmap extends View {
    private static Paint mPaint;
    private String num;

    public CropCountryBitmap(Context context) {
        super(context);
        init();
    }

    public CropCountryBitmap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }





}
