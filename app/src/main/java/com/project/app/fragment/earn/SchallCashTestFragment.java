package com.project.app.fragment.earn;

import android.content.Context;
import android.view.KeyEvent;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.project.app.R;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.MeBindCPBean;
import com.project.app.bean.ScCashBean;
import com.project.app.contract.ScContract;
import com.project.app.presenter.ShareCashPresenter;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class SchallCashTestFragment extends BaseMvpQmuiFragment<ShareCashPresenter> implements ScContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.qtb_topbar)
    QMUITopBarLayout mTopbar;
    private Context mContext;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shall_cast_test;
    }

    @Override
    public void initView() {
        initTopbar();
        initWidget();
    }

    private void initWidget() {
        mContext = getContext();
        mPresenter = new ShareCashPresenter();
        mPresenter.attachView(this);

        mTopbar.setTitle("你好");
        mTopbar.addLeftBackImageButton().setOnClickListener(view -> popBackStack());
    }

    private void initTopbar() {

    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchScHistory();
    }

    @Override
    public void fetchCashSuccess(MeBindCPBean result) {

    }

    @Override
    public void fetchSuccess(ScCashBean result) {
        if(result != null){
            inflateDataToView();
        }
    }

    private void inflateDataToView() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void fetchFail(String failReason) {

    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
