package com.project.app.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.SwipeBackLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.project.app.fragment.home.HomeFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public abstract class BaseQmuiFragment extends QMUIFragment{
    protected Context context;
    private final int mBindId = -1;

    public BaseQmuiFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
    }

    @Override
    protected int backViewInitOffset(Context context, int dragDirection, int moveEdge) {
        if (moveEdge == SwipeBackLayout.EDGE_TOP || moveEdge == SwipeBackLayout.EDGE_BOTTOM) {
            return 0;
        }
        return QMUIDisplayHelper.dp2px(context, 100);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public Object onLastFragmentFinish() {
        return new HomeFragment();
    }


    protected void injectDocToTopBar(QMUITopBar topBar) {

    }

    protected void injectDocToTopBar(QMUITopBarLayout topBar) {

    }


}
