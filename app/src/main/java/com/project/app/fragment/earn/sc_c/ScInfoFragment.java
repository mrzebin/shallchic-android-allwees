package com.project.app.fragment.earn.sc_c;

import android.view.KeyEvent;

import com.project.app.R;
import com.project.app.base.BaseMvpQmuiFragment;

public class ScInfoFragment extends BaseMvpQmuiFragment {

    public static ScInfoFragment newInstance() {
        return new ScInfoFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sc_info;
    }

    @Override
    public void initView() {
        initTopbar();
    }

    private void initTopbar() {

    }

    @Override
    protected void lazyFetchData() {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }
}