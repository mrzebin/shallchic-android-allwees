package com.project.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment;
import com.qmuiteam.qmui.arch.annotation.FirstFragments;
import com.qmuiteam.qmui.arch.annotation.LatestVisitRecord;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.project.app.base.BaseActivity;
import com.project.app.fragment.GoodsDetailFragment;
import com.project.app.fragment.account.AccountSetFragment;
import com.project.app.fragment.account.ChangePasswordFragment;
import com.project.app.fragment.account.UpdateProfileFragment;
import com.project.app.fragment.home.SettingFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentContainerView;


/**
 *  个人修改信息的类
 */
@FirstFragments(
        value = {
                SettingFragment.class,
                AccountSetFragment.class,
                ChangePasswordFragment.class,   //修改密码
                UpdateProfileFragment.class
        })
@DefaultFirstFragment(GoodsDetailFragment.class)
@LatestVisitRecord
public class SettingHolderActivity extends BaseActivity {
    private static long mPreviousTime = 0;
    private static long mIntervalTime = 500;   //500毫秒不能重复点击

    public static Intent of(@NonNull Context context,
                            @NonNull Class<? extends QMUIFragment> firstFragment) {
        return QMUIFragmentActivity.intentOf(context, com.project.app.activity.SettingHolderActivity.class, firstFragment);
    }

    public static Intent of(@NonNull Context context,
                            @NonNull Class<? extends QMUIFragment> firstFragment,
                            @Nullable Bundle fragmentArgs) {
        return QMUIFragmentActivity.intentOf(context, com.project.app.activity.SettingHolderActivity.class, firstFragment, fragmentArgs);
    }


    public static void startFragment(@NonNull Context context,
                                     @NonNull Class<? extends QMUIFragment> firstFragment) {
        long currentTime = System.currentTimeMillis();
        if(currentTime - mPreviousTime > mIntervalTime){
            mPreviousTime = currentTime;
            Intent intent =  QMUIFragmentActivity.intentOf(context, HolderActivity.class, firstFragment);
            context.startActivity(intent);
        }
    }

    public static void startFragment(@NonNull Context context,
                                     @NonNull Class<? extends QMUIFragment> firstFragment,
                                     @Nullable Bundle fragmentArgs) {
        long currentTime = System.currentTimeMillis();
        if(currentTime - mPreviousTime > mIntervalTime){
            mPreviousTime = currentTime;
            Intent intent =  QMUIFragmentActivity.intentOf(context, HolderActivity.class, firstFragment, fragmentArgs);
            context.startActivity(intent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.setStatusBarLightMode(com.project.app.activity.SettingHolderActivity.this);
    }

    @Override
    protected QMUIFragmentActivity.RootView onCreateRootView(int fragmentContainerId) {
        return new CustomRootView(this, fragmentContainerId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    static class CustomRootView extends QMUIFragmentActivity.RootView {
        private final FragmentContainerView fragmentContainer;

        public CustomRootView(Context context, int fragmentContainerId) {
            super(context, fragmentContainerId);
            fragmentContainer = new FragmentContainerView(context);
            fragmentContainer.setId(fragmentContainerId);
            addView(fragmentContainer, new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        @Override
        public FragmentContainerView getFragmentContainerView() {
            return fragmentContainer;
        }

    }
}
