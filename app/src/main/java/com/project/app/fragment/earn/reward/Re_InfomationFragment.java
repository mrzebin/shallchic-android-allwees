package com.project.app.fragment.earn.reward;

import android.view.KeyEvent;

import com.project.app.R;
import com.project.app.base.BaseMvpQmuiFragment;

public class Re_InfomationFragment extends BaseMvpQmuiFragment {

    public static Re_InfomationFragment newInstance() {
        return new Re_InfomationFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_reward_infomation;
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