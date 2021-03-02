package com.project.app.adapter;

import com.qmuiteam.qmui.arch.QMUIFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LoginStateAdapter extends FragmentStateAdapter {
    private List<QMUIFragment> sparseFragments = new ArrayList<>();

    public LoginStateAdapter(List<QMUIFragment> fragments, @NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.sparseFragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return sparseFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return sparseFragments.size();
    }
}
