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
import com.project.app.fragment.AddMoreGoodsFragment;
import com.project.app.fragment.GoodsDetailFragment;
import com.project.app.fragment.WebExplorerFragment;
import com.project.app.fragment.address.AddressManagerFragment;
import com.project.app.fragment.home.OrderFragment;
import com.project.app.fragment.home.ShippingAddressFragment;
import com.project.app.fragment.home.classify.CategoryClassifyFragment;
import com.project.app.fragment.money.EarnFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentContainerView;



/**
 * 承载另外的业务
 */
@FirstFragments(
        value = {
                GoodsDetailFragment.class,
                WebExplorerFragment.class,
                OrderFragment.class,
                AddMoreGoodsFragment.class,
                CategoryClassifyFragment.class,
                AddressManagerFragment.class,
                ShippingAddressFragment.class,
                EarnFragment.class
        })
@DefaultFirstFragment(GoodsDetailFragment.class)
@LatestVisitRecord
public class HolderActivity extends BaseActivity {

    public static Intent of(@NonNull Context context,
                            @NonNull Class<? extends QMUIFragment> firstFragment) {
        return QMUIFragmentActivity.intentOf(context, HolderActivity.class, firstFragment);
    }

    public static Intent of(@NonNull Context context,
                            @NonNull Class<? extends QMUIFragment> firstFragment,
                            @Nullable Bundle fragmentArgs) {
        return QMUIFragmentActivity.intentOf(context, HolderActivity.class, firstFragment, fragmentArgs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.setStatusBarLightMode(HolderActivity.this);
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
