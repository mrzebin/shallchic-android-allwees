package com.project.app.fragment.notify;

import android.view.KeyEvent;
import android.view.View;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.project.app.R;
import com.project.app.base.BaseMvpQmuiFragment;

import butterknife.OnClick;

public class NotifyCenterFragment extends BaseMvpQmuiFragment{

    @Override
    public int getLayoutId() {
        return R.layout.fragment_notify_center;
    }

    @Override
    public void initView() {
        initWidget();
    }

    private void initWidget() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
    }

    @OnClick({R.id.iv_back})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
        }
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
