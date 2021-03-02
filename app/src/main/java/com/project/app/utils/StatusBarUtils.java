package com.project.app.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.project.app.R;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

public class StatusBarUtils {

    public static final int MIN_API = 19;
    public static final int DEFAULT_COLOR = 0;
    public static final float DEFAULT_ALPHA = 0;//Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 0.2f : 0.3f;

    /**
     * 单独在标题栏的位置增加view，高度为状态栏的高度
     * Sets status bar view.
     *
     * @param context the activity
     * @param view    the view
     */
    public static void setStatusBarView(Context context, View... view) {
        setStatusBarView(context, getStatusBarHeight(context), view);
    }

    /**
     * 单独在标题栏的位置增加view，高度为fixHeight的高度
     * Sets status bar view.
     *
     * @param context   the activity
     * @param fixHeight the fix height
     * @param view      the view
     */
    public static void setStatusBarView(Context context, int fixHeight, View... view) {
        if (context == null) {
            return;
        }
        if (fixHeight < 0) {
            fixHeight = 0;
        }
        for (View v : view) {
            if (v == null) {
                continue;
            }
            Integer fitsHeight = (Integer) v.getTag(R.id.immersion_fits_layout_overlap);
            if (fitsHeight == null) {
                fitsHeight = 0;
            }
            if (fitsHeight != fixHeight) {
                v.setTag(R.id.immersion_fits_layout_overlap, fixHeight);
                ViewGroup.LayoutParams lp = v.getLayoutParams();
                if (lp == null) {
                    lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                }
                lp.height = fixHeight;
                v.setLayoutParams(lp);
            }
        }
    }

    /**
     * Gets status bar height.
     * 或得状态栏的高度
     *
     * @param context the activity
     * @return the status bar height
     */
    public static int getStatusBarHeight(@NonNull Context context) {
        return QMUIStatusBarHelper.getStatusbarHeight(context);
    }

    /** 增加View的paddingTop,增加的值为状态栏高度 */
    public static void setPadding(Context context, View view) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }
    /** 增加View的paddingTop,增加的值为状态栏高度 (智能判断，并设置高度)*/
    public static void setPaddingSmart(Context context, View view) {
        if (Build.VERSION.SDK_INT >= MIN_API) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp != null && lp.height > 0) {
                lp.height += getStatusBarHeight(context);//增高
            }
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    //<editor-fold desc="沉侵">
    public static void immersive(Activity activity) {
        immersive(activity, DEFAULT_COLOR, DEFAULT_ALPHA);
    }

    public static void immersive(Activity activity, int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        immersive(activity.getWindow(), color, alpha);
    }

    public static void immersive(Activity activity, int color) {
        immersive(activity.getWindow(), color, 1f);
    }

    public static void immersive(Window window) {
        immersive(window, DEFAULT_COLOR, DEFAULT_ALPHA);
    }

    public static void immersive(Window window, int color) {
        immersive(window, color, 1f);
    }

    public static void immersive(Window window, int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mixtureColor(color, alpha));

            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setTranslucentView((ViewGroup) window.getDecorView(), color, alpha);
        }
    }

    /**
     * 创建假的透明栏
     */
    public static void setTranslucentView(ViewGroup container, int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        int mixtureColor = mixtureColor(color, alpha);
        View translucentView = container.findViewById(android.R.id.custom);
        if (translucentView == null && mixtureColor != 0) {
            translucentView = new View(container.getContext());
            translucentView.setId(android.R.id.custom);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(container.getContext()));
            container.addView(translucentView, lp);
        }
        if (translucentView != null) {
            translucentView.setBackgroundColor(mixtureColor);
        }
    }

    public static int mixtureColor(int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        int a = (color & 0xff000000) == 0 ? 0xff : color >>> 24;
        return (color & 0x00ffffff) | (((int) (a * alpha)) << 24);
    }
}
