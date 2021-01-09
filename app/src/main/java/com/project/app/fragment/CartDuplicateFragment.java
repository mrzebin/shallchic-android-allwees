package com.project.app.fragment;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.project.app.R;
import com.project.app.base.BaseMvpQmuiFragment;

import butterknife.BindView;

/**
 * 购物车分支
 */
public class CartDuplicateFragment extends BaseMvpQmuiFragment {
    @BindView(R.id.qmui_topbar)
    QMUITopBarLayout mTopbar;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_cart_duplicate;
    }

    @Override
    public void initView() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        initTabBar();
    }

    @Override
    protected void lazyFetchData() {

    }

    private void initTabBar() {
        mTopbar.addLeftBackImageButton().setOnClickListener(view -> popBackStack());
        mTopbar.setTitle("Cart");

    }


}
