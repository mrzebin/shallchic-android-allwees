package com.project.app.utils;

import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * 动画工具
 */
public class RotateAnimUtils {

    public static void rotateArrow(ImageView arrow, boolean flag) {
        float pivotX = arrow.getWidth() / 2f;
        float pivotY = arrow.getHeight() / 2f;
        float fromDegrees = 0f;
        float toDegrees = 0f;
        // flag为true则向上
        if (flag) {
            fromDegrees = 0f;
            toDegrees   = 180f;
        } else {
            fromDegrees = 180f;
            toDegrees   = 360f;
        }
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
        animation.setDuration(200);
        animation.setFillAfter(true);
        arrow.startAnimation(animation);
    }


}
