package com.project.app.base;

public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseFragmentActivity {
    public P mPresenter;
    
    @Override
    protected void onDestroy() {
        if(mPresenter != null){
            mPresenter.detachView();
        }
        super.onDestroy();
    }
}
