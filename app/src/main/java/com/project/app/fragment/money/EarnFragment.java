package com.project.app.fragment.money;

import android.view.KeyEvent;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.project.app.R;
import com.project.app.base.BaseMvpQmuiFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class EarnFragment extends BaseMvpQmuiFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.qmui_topbar)
    QMUITopBarLayout mTopbar;
    @BindView(R.id.srf_earn)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.rv_earn)
    RecyclerView rv_earn;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_earn_method_share;
    }

    @Override
    public void initView() {
        initTopbar();
        initWidget();
    }

    private void initWidget() {
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.theme_color,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);
        rv_earn.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void initTopbar() {
        mTopbar.setTitle("Referral Program");
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        mTopbar.addLeftBackImageButton().setOnClickListener(view -> popBackStack());
    }

    @Override
    public void onRefresh() {

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
