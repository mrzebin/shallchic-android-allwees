package com.project.app.fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.utils.DataUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.project.app.R;
import com.project.app.adapter.AddMoreAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.AddMoreListBean;
import com.project.app.bean.CategoryBean;
import com.project.app.contract.AddMoreGoodsContract;
import com.project.app.presenter.AddMoreGoodsPresenter;
import com.project.app.ui.widget.GlobalClassicsFooter;
import com.project.app.ui.widget.GlobalClassicsHeader;
import com.project.app.utils.HomeTitlesUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class AddMoreGoodsFragment extends BaseMvpQmuiFragment<AddMoreGoodsPresenter> implements AddMoreGoodsContract.View{
    @BindView(R.id.v_inflater)
    LinearLayout v_inflater;
    @BindView(R.id.srl_addMore)
    SmartRefreshLayout srl_addMore;
    @BindView(R.id.rlv_goodsContainer)
    RecyclerView rlv_goodsContainer;
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.emptyView)
    QMUIEmptyView emptyView;

    private boolean isPrepared     = false;
    private boolean isHasMore      = false;
    private int mCurrentPage       = 5;
    private final int mPageSize    = 20;
    private String mCategoryNo     = "P05";
    private AddMoreAdapter mAdapter;
    private final List<AddMoreListBean.ClassifyItem> mMoreLists = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_more_goods;
    }

    @Override
    public void initView() {
        mPresenter = new AddMoreGoodsPresenter();
        mPresenter.attachView(this);
        initTopbar();
        initWidget();
        initSmartRefreshLayout();
    }

    private void initSmartRefreshLayout() {
        srl_addMore.setRefreshHeader(new GlobalClassicsHeader(getContext()));
        srl_addMore.setRefreshFooter(new GlobalClassicsFooter(getContext()));
        srl_addMore.setHeaderHeight(60);
        srl_addMore.setFooterHeight(50);
        srl_addMore.setEnableLoadMore(false); //默认不让起有更多数据

        srl_addMore.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isPrepared   = false;
                mCurrentPage = 5;
                isHasMore    = false;
                srl_addMore.setEnableLoadMore(isHasMore);
                srl_addMore.finishRefresh(Constant.DEFAULT_SMARTLOADING_DELAY_TIMEOUT);
                mPresenter.fetchGoodsList(mCategoryNo,mCurrentPage,mPageSize);
            }
        });

        srl_addMore.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (isHasMore) {
                    mCurrentPage++;
                    if (UserUtil.getInstance().isLogin()) {
                        mPresenter.fetchGoodsList(mCategoryNo,mCurrentPage,mPageSize);
                        srl_addMore.finishLoadMore(Constant.DEFAULT_SMARTLOADING_DELAY_TIMEOUT);
                    }
                }else{
                    srl_addMore.finishLoadMore();
                }
            }
        });
    }

    private void initWidget() {
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        rlv_goodsContainer.setLayoutManager(manager);
        mAdapter = new AddMoreAdapter(mMoreLists);
        mAdapter.setAnimationEnable(true);
        mAdapter.setAnimationFirstOnly(false);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        rlv_goodsContainer.setAdapter(mAdapter);
        CategoryBean categoryBean = HomeTitlesUtils.getAppCategorys();
        if(DataUtil.idNotNull(categoryBean.getCategories())){
            mCategoryNo = categoryBean.getCategories().get(0).getNo();
        }
    }

    @OnClick({R.id.iv_back})
    public void onClickViewed(View view){
        if (view.getId() == R.id.iv_back) {
            popBackStack();
        }
    }

    private void initTopbar() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
    }

    @Override
    protected void lazyFetchData() {
        if(!isPrepared){
            mPresenter.fetchGoodsList(mCategoryNo,mCurrentPage,mPageSize);
        }
    }

    private View getNoEndView(){
        View endView = LayoutInflater.from(getContext()).inflate(R.layout.layout_footer_no_more,null);
        return endView;
    }

    private View getEmptyView(){
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_nogoods,null);
        QMUIRoundButton qrb_refreshAgain = emptyView.findViewById(R.id.qrb_refreshAgain);
        qrb_refreshAgain.setChangeAlphaWhenPress(true);
        qrb_refreshAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoading();
                lazyFetchData();
            }
        });
        return emptyView;
    }

    @Override
    public void fetchSuccess(AddMoreListBean result) {
        emptyView.setVisibility(View.GONE);
        srl_addMore.setVisibility(View.VISIBLE);

        srl_addMore.finishLoadMore();
        if(result != null){
            isHasMore  = result.isHasMorePages();
            if(mCurrentPage == 5){
                if(DataUtil.idNotNull(result.getResults())){
                    isPrepared = true;
                    mAdapter.setNewInstance(result.getResults());
                    if(!isHasMore){
                        if(!mAdapter.hasFooterLayout()){
                            mAdapter.addFooterView(getNoEndView());
                        }
                    }
                }else{
                    mAdapter.setEmptyView(getEmptyView());
                }
            }else{
                if(DataUtil.idNotNull(result.getResults())){
                    mAdapter.addData(result.getResults());
                    if(!isHasMore){
                        if(!mAdapter.hasFooterLayout()){
                            mAdapter.addFooterView(getNoEndView());
                        }
                    }
                }
            }
            srl_addMore.setEnableLoadMore(isHasMore);
        }
    }

    @Override
    public void fetchFail(String failReason) {
        if(emptyView.getVisibility() == View.VISIBLE){
            emptyView.setVisibility(View.GONE);
            srl_addMore.setVisibility(View.VISIBLE);
        }
        mAdapter.setEmptyView(getEmptyView());
    }

    @Override
    public void loadMatchViewStatus(int loadStatus) {
        emptyView.show(false);
        emptyView.setVisibility(View.GONE);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.onDestoryView();
            mPresenter = null;
            System.gc();
        }
    }
}
