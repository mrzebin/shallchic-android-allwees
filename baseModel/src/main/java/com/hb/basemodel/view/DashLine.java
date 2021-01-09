package com.hb.basemodel.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hb.basemodel.R;

import androidx.annotation.Nullable;

public class DashLine extends View {
    private Paint mPaint;

    public DashLine(Context context) {
        super(context);
    }

    public DashLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.color_999));
        mPaint.setStrokeWidth(1);
        mPaint.setPathEffect(new DashPathEffect(new float[] {10, 10}, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerY = getHeight() / 2;
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.drawLine(0, centerY, getWidth(), centerY, mPaint);
    }
}
