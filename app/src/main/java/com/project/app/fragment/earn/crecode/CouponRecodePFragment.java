package com.project.app.fragment.earn.crecode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.RecycerLoadMoreScrollView;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.project.app.R;
import com.project.app.adapter.CouponUseAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.RewardDashbBean;
import com.project.app.contract.PresenterCouponContract;
import com.project.app.presenter.RecodeCPresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 优惠券的使用界面
 */
public class CouponRecodePFragment extends BaseMvpQmuiFragment<RecodeCPresenter> implements PresenterCouponContract.View {
    @BindView(R.id.rlv_useCoupon)
    RecyclerView rlv_useCoupon;
    @BindView(R.id.rlv_moreAvailableC)
    RecycerLoadMoreScrollView rlv_moreAvailableC;
    private CouponUseAdapter mAdapter;

    private int mType;
    private int mCurrentPage = 1;
    private final int mPageSize = 20;
    private String mRequestUrl;
    private final boolean isHasMore = false;
    private List<RewardDashbBean.CouponItem> mDatas;

    public static QMUIFragment newInstance(Bundle bundle) {
        CouponRecodePFragment fragment = new CouponRecodePFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_coupon_c_presenter;
    }

    @Override
    public void initView() {
        initWidget();
    }

    private void initWidget() {
        Bundle bundle = getArguments();
        mType = bundle.getInt("type");
        if(mType == 0 ){
            mRequestUrl = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_RECODE_DASHBOARD_A_URL;
        }else if(mType == 1){
            mRequestUrl = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_RECODE_DASHBOARD_U_URL;
        }else if(mType == 2){
            mRequestUrl = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_RECODE_DASHBOARD_H_URL;
        }
        mPresenter = new RecodeCPresenter();
        mPresenter.attachView(this);
        mDatas = new ArrayList<>();
        rlv_useCoupon.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CouponUseAdapter(mDatas);
        rlv_useCoupon.setAdapter(mAdapter);

        rlv_moreAvailableC.setScrollViewListener(new RecycerLoadMoreScrollView.ScrollViewListener() {
            @Override
            public void onLoadMore() {
                if(isHasMore){
                    mCurrentPage++;
                    mPresenter.fetchUseCouponHistory(mCurrentPage,mPageSize,mRequestUrl);
                }
            }

            @Override
            public void onScrollChanged(RecycerLoadMoreScrollView scrollView, int x, int y, int oldx, int oldy) {

            }
        });
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchUseCouponHistory(mCurrentPage,mPageSize,mRequestUrl);
    }

    public View getEmptyCouponView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_coupon_order,null);
        QMUIRoundButton qrb_refreshOrder = view.findViewById(R.id.qrb_refreshOrder);
        qrb_refreshOrder.setOnClickListener(view1 -> {
            startProgressDialog(getContext());
            mPresenter.fetchUseCouponHistory(mCurrentPage,mPageSize,mRequestUrl);
        });
        return view;
    }

    @Override
    public void fetchSuccess(RewardDashbBean result) {
        stopProgressDialog();
        if(mCurrentPage == 1){
            if(DataUtil.idNotNull(result.getResults())){
                mAdapter.setNewInstance(result.getResults());
            }else{
                mAdapter.setEmptyView(getEmptyCouponView());
            }
        }else{
            if(DataUtil.idNotNull(result.getResults())){
                mAdapter.addData(result.getResults());
            }
        }
    }

    @Override
    public void fetchFail(String failReason) {
        ToastUtil.showToast(failReason);
        stopProgressDialog();
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
}
