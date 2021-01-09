package com.project.app.adapter;

import android.view.ViewGroup;

import com.qmuiteam.qmui.arch.QMUIFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * create by zb
 * 通用的FragmentAdapter
 */
public class BaseFragmentAdapter extends FragmentPagerAdapter {
    private final FragmentManager fm;
    final List<QMUIFragment> fragmentList;
    private List<String> mTitles;

    public BaseFragmentAdapter(FragmentManager fm, List<QMUIFragment> fragmentList) {
        super(fm);
        this.fm = fm;
        this.fragmentList = fragmentList;
    }

    public BaseFragmentAdapter(FragmentManager fm, List<QMUIFragment> fragmentList, List<String> mTitles) {
        super(fm);
        this.fm = fm;
        this.fragmentList = fragmentList;
        this.mTitles = mTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles == null ? "" : mTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        if(fragmentList == null) return 0;
        return fragmentList.size();
    }

    /**
     * 这边并没有创建销毁过程，只创建一次
     * @param container
     * @param position
     * @return
     */
    @Override
    public QMUIFragment instantiateItem(ViewGroup container, int position) {
        QMUIFragment fragment = (QMUIFragment) super.instantiateItem(container, position);
        fm.beginTransaction().show(fragment).commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = fragmentList.get(position);
        fm.beginTransaction().hide(fragment).commitAllowingStateLoss();
    }


    public List<QMUIFragment> getFragmentsList() {
        return fragmentList;
    }
}
