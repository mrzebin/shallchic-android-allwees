package com.project.app.adapter;

import com.qmuiteam.qmui.arch.QMUIFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class BaseFragmentAdapter2 extends FragmentStateAdapter {
    List<QMUIFragment> mFragments;

    public BaseFragmentAdapter2(@NonNull FragmentActivity fragmentActivity,List<QMUIFragment> fragments) {
        super(fragmentActivity);
        this.mFragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }
}
