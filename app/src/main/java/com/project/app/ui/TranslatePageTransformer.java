package com.project.app.ui;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class TranslatePageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_TRANSLATE = 0.9F;

    @Override
    public void transformPage(@NonNull View page, float position) {
        float scale = Math.max(MIN_TRANSLATE,1 - Math.abs(position));

        if (position < -1.0f) {
            page.setTranslationX(MIN_TRANSLATE);
        } else if (position <= 0.0f) {
            page.setTranslationX(scale);
        } else if (position <= 1.0f) {
            page.setTranslationX(scale);
        } else {
            page.setTranslationX(MIN_TRANSLATE);
        }

    }
}
