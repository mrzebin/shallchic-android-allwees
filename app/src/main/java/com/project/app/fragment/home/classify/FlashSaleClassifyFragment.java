package com.project.app.fragment.home.classify;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.RecycerLoadMoreScrollView;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.project.app.R;
import com.project.app.activity.LoginActivity;
import com.project.app.adapter.FsChildAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.bean.GoodsDetailInfoBean;
import com.project.app.bean.HomeFLashSaleBean;
import com.project.app.contract.FlashSaleContract;
import com.project.app.presenter.FlashSalePresenter;
import com.project.app.ui.dialog.GDBuyPopupDialogUtil;
import com.project.app.ui.dialog.GDChooiceDialogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 *  闪购的分类
 */
public class FlashSaleClassifyFragment extends BaseMvpQmuiFragment<FlashSalePresenter> implements FlashSaleContract.View{
    @BindView(R.id.rlm_moreFs)
    RecycerLoadMoreScrollView rlm_moreFs;
    @BindView(R.id.rlv_fsGoods)
    RecyclerView rlv_fsGoods;

    private int mKindsType = 0;
    private int mCurrentPage = 1;
    private int mPageSize = 20;
    private boolean isHasMore = false;
    private FsChildAdapter mAdapter;
    private List<HomeFLashSaleBean.FlashSaleBean> mData;
    private GDBuyPopupDialogUtil mBuySuccessUtil;
    private GDChooiceDialogUtil mChooseGoodsUtil;

    public static QMUIFragment newInstance(int type) {
        FlashSaleClassifyFragment fragment = new FlashSaleClassifyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_flash_sale_classify;
    }

    @Override
    public void initView() {
        initData();
        initWidget();
    }

    private void initData() {
        Bundle bundle = getArguments();
        mKindsType = bundle.getInt("type");
        mPresenter = new FlashSalePresenter();
        mPresenter.attachView(this);
        mData = new ArrayList<>();
        mAdapter = new FsChildAdapter(mKindsType,mData);
    }

    private void initWidget() {
        mBuySuccessUtil = new GDBuyPopupDialogUtil(getContext(),true,true);
        rlv_fsGoods.setLayoutManager(new LinearLayoutManager(getContext()));
        rlv_fsGoods.setAdapter(mAdapter);

        mChooseGoodsUtil = new GDChooiceDialogUtil(getContext(), true, true, (count, isIncrease, skuUuid) -> {
            if(checkLogin()){
                mPresenter.operationAddGoods(count,isIncrease,skuUuid);
            }
        });

        mAdapter.setListener(new FsChildAdapter.IAddToCarListener() {
            @Override
            public void fastBuyTarget(HomeFLashSaleBean.FlashSaleBean item) {
                if(!TextUtils.isEmpty(item.getMainPhoto())){
                    mBuySuccessUtil.lazzyImageUrl(item.getMainPhoto());
                    mPresenter.fetchFSGoodsDetail(item.getUuid());
                }
            }
        });

        rlm_moreFs.setScrollViewListener(new RecycerLoadMoreScrollView.ScrollViewListener() {
            @Override
            public void onLoadMore() {
                if(isHasMore){
                    mCurrentPage++;
                    mPresenter.fetchFlashSale(mKindsType,mCurrentPage,mPageSize);
                }
            }
            @Override
            public void onScrollChanged(RecycerLoadMoreScrollView scrollView, int x, int y, int oldx, int oldy) {

            }
        });
    }

    private boolean checkLogin(){
        UserUtil userUtil = UserUtil.getInstance();
        if(!userUtil.isLogin()) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            getContext().startActivity(intent);
        }
        return userUtil.isLogin();
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchFlashSale(mKindsType,mCurrentPage,mPageSize);
    }

    @Override
    public void fetchBuyDataSuccess(CartBuyDataBean result) {
        SPManager.sPutInt(Constant.SP_SAVE_CART_GOODS_NUM,result.getSum());
        EventBus.getDefault().post(new RefreshDataEvent(Constant.EVNET_CHANGE_CURRENT_CART_NUM));
    }

    @Override
    public void fetchFlashSaleSuccess(HomeFLashSaleBean result) {
        if(result != null){
            if(mCurrentPage == 1){
                if(DataUtil.idNotNull(result.getResults())){
                    mAdapter.setNewInstance(result.getResults());
                    isHasMore = result.getHasMorePages();
                    if(!isHasMore){
                        mAdapter.addFooterView(getFootView()); 
                    }
                }else{
                    mAdapter.setEmptyView(getEmptyView());
                }
            }else{
                if(DataUtil.idNotNull(result.getResults())){
                    mAdapter.addData(result.getResults());
                    isHasMore = result.getHasMorePages();
                    if(!isHasMore){
                        mAdapter.addFooterView(getFootView());
                    }
                }
            }
        }
    }

    private View getEmptyView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_nodata,null);
        QMUIRoundButton qrb_refresh = view.findViewById(R.id.qrb_refreshOrder);
        qrb_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.fetchFlashSale(mKindsType,mCurrentPage,mPageSize);
            }
        });
        return view;
    }

    private View getFootView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_no_more,null);
        LinearLayout ll_endNoData = view.findViewById(R.id.ll_endNoData);
        ll_endNoData.setVisibility(View.VISIBLE);
        return view;
    }


    @Override
    public void fetchGoodsInfoSuccess(GoodsDetailInfoBean result) {
        mChooseGoodsUtil.syncData(result.getSkus(),result.getMainPhoto());
        mChooseGoodsUtil.show();
    }

    @Override
    public void addCartSuccess(String msg) {
        mBuySuccessUtil.show();
        mPresenter.fetchCartData();
    }

    @Override
    public void fetchFail(String failReason) {
        ToastUtil.showToast(failReason);
    }

    @Override
    public void startLoading() {
        startProgressDialog(getContext());
    }

    @Override
    public void stopLoading() {
        stopProgressDialog();
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
