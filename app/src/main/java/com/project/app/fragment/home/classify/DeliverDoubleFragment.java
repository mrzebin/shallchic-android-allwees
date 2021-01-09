package com.project.app.fragment.home.classify;


import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.RecycerLoadMoreScrollView;
import com.hb.basemodel.utils.SPManager;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.project.app.R;
import com.project.app.activity.LoginActivity;
import com.project.app.adapter.DeliverDoubleAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.ActionFreeOneBean;
import com.project.app.contract.ActionDeliverDoubleContract;
import com.project.app.presenter.ActionDeliverDoublePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

/**
 *  买一送一
 */
public class DeliverDoubleFragment extends BaseMvpQmuiFragment<ActionDeliverDoublePresenter> implements ActionDeliverDoubleContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.srl_deliverDouble)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.rlv_moreDeliverDouble)
    RecycerLoadMoreScrollView rlm_loadMore;
    @BindView(R.id.rv_actionGetDoubleGoods)
    RecyclerView rv_goodItems;
    @BindView(R.id.iv_activeDeliverTow)
    QMUIRadiusImageView iv_head;
    @BindView(R.id.tv_buyNum)
    TextView tv_buyNum;
    @BindView(R.id.cl_comeMyCart)
    ConstraintLayout cl_comeMyCart;
    @BindView(R.id.v_hasCart)
    View v_hasCart;
    @BindView(R.id.ll_endNoData)
    LinearLayout ll_endNoData;

    private int mCurrentPage = 0;
    private int mPageSize = 20;
    private boolean isHasMore = false;
    private List<ActionFreeOneBean.ClassifyItem> mData = new ArrayList<>();
    private DeliverDoubleAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_deliver_double;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        initCarData();
        initWidget();
    }

    private void initWidget() {
        mPresenter = new ActionDeliverDoublePresenter();
        mPresenter.attachView(this);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.allwees_theme_color,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);

        mAdapter = new DeliverDoubleAdapter(mData);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        rv_goodItems.setLayoutManager(manager);
        rv_goodItems.setAdapter(mAdapter);

        rlm_loadMore.setScrollViewListener(new RecycerLoadMoreScrollView.ScrollViewListener() {
            @Override
            public void onLoadMore() {
                if(isHasMore){
                    mCurrentPage++;
                    mPresenter.fetchActionFreeOne(mCurrentPage,mPageSize);
                }
            }
            @Override
            public void onScrollChanged(RecycerLoadMoreScrollView scrollView, int x, int y, int oldx, int oldy) {

            }
        });
    }

    private void initCarData() {
        changeCartNum();
    }

    private void changeCartNum() {
        int cartSkuNum = SPManager.sGetInt(Constant.SP_SAVE_CART_GOODS_NUM);
        String buyNum = "";
        if(cartSkuNum > 0){
            tv_buyNum.setVisibility(View.VISIBLE);
            v_hasCart.setVisibility(View.VISIBLE);
            if (cartSkuNum >99){
                buyNum = cartSkuNum + "+";
            }else{
                buyNum = String.valueOf(cartSkuNum);
            }
            tv_buyNum.setText(String.valueOf(buyNum));
        }else{
            tv_buyNum.setVisibility(View.GONE);
            v_hasCart.setVisibility(View.GONE);
        }
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
    public void onRefresh() {
        mCurrentPage = 1;
        mPresenter.fetchActionFreeOne(mCurrentPage,mPageSize);
        mSwipeRefresh.setRefreshing(true);
        mSwipeRefresh.postDelayed(() -> {
            if(mSwipeRefresh != null){
                mSwipeRefresh.setRefreshing(false);
            }
        }, Constant.DELAY_LOADING_TIME_OUT);
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchActionFreeOne(mCurrentPage,mPageSize);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshDataEvent event){
        if (event.getmMsg().equals(Constant.EVNET_CHANGE_CURRENT_CART_NUM)){
            changeCartNum();
        }
    }

    @OnClick({R.id.iv_back,R.id.iv_shipCart_fs})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
            case R.id.iv_shipCart_fs:
                if(checkLogin()){
                    clearHolderStack();
                    EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_BANNER_SKIP_CART));
                }
                break;
        }
    }

    @Override
    public void fetchActionFreeOneSuccess(ActionFreeOneBean result) {
        String coverHeadUrl = result.getBuyAndFreeUrl();
        if(!TextUtils.isEmpty(coverHeadUrl)){
            iv_head.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(iv_head,coverHeadUrl + Constant.mGlobalThumbnailStyle,R.mipmap.allwees_ic_default_goods);
        }else{
            iv_head.setVisibility(View.GONE);
        }
        if(mCurrentPage == 1){
            if(DataUtil.idNotNull(result.getResults().getResults())){
                mAdapter.setNewInstance(result.getResults().getResults());
                isHasMore = result.getResults().isHasMorePages();
                if(!isHasMore){
                    ll_endNoData.setVisibility(View.VISIBLE);
                }
            }
        }else{
            if(DataUtil.idNotNull(result.getResults().getResults())){
                mAdapter.addData(result.getResults().getResults());
                isHasMore = result.getResults().isHasMorePages();
                if(!isHasMore){
                    ll_endNoData.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void fetchActionFreeOneFail(String msg) {

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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
