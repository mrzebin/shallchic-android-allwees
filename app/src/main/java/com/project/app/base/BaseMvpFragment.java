package com.project.app.base;

import android.app.Fragment;

public abstract class BaseMvpFragment<T extends BasePresenter> extends Fragment {
    public T mPresenter;

    @Override
    public void onDestroy() {
        if(mPresenter != null){
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    public abstract void initView();
}
