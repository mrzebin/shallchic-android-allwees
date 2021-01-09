package com.project.app.fragment.pay;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.project.app.R;
import com.project.app.base.BaseMvpQmuiFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加信用卡
 */
public class PmInformationAddCard extends BaseMvpQmuiFragment {
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pm_add_card_information;
    }

    @Override
    public void initView() {
        initWidget();
    }

    @OnClick({R.id.iv_back})
    public void onClickViewed(View view){
        if (view.getId() == R.id.iv_back) {
            popBackStack();
        }
    }


    private void initWidget() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());


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
