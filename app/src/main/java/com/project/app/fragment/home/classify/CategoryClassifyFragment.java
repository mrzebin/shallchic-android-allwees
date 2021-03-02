package com.project.app.fragment.home.classify;


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
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.project.app.R;
import com.project.app.adapter.CategoryClassifyAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.ClassifyListBean;
import com.project.app.contract.CategoryClassifyContract;
import com.project.app.presenter.CategoryClassifyPresenter;
import com.project.app.ui.widget.GlobalClassicsFooter;
import com.project.app.ui.widget.GlobalClassicsHeader;
import com.project.app.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 分类的详情
 */
public class CategoryClassifyFragment extends BaseMvpQmuiFragment<CategoryClassifyPresenter> implements CategoryClassifyContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.srl_category)
    SmartRefreshLayout srl_category;
    @BindView(R.id.rv_classify)
    RecyclerView rv_classify;
    @BindView(R.id.ll_inflateTp)
    LinearLayout ll_inflateTp;
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.emptyView)
    QMUIEmptyView emptyView;
    @BindView(R.id.ll_endNoData)
    LinearLayout ll_endNoData;

    private CategoryClassifyAdapter mAdapter;
    private final List<ClassifyListBean.ClassifyItem> mCategorys = new ArrayList<>();
    private String mNo;
    private String mCategoryName;
    private int mPageSize = 20;
    private int mCurrentPage = 1;
    private boolean isLoadMore = false;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_classify_category;
    }

    @Override
    public void initView() {
        initTapbar();
        mPresenter = new CategoryClassifyPresenter();
        mPresenter.attachView(this);
        srl_category.setRefreshHeader(new GlobalClassicsHeader(getContext()));
        srl_category.setRefreshFooter(new GlobalClassicsFooter(getContext()));
        srl_category.setHeaderHeight(60);
        srl_category.setFooterHeight(50);
        srl_category.setEnableLoadMore(false); //默认不让起有更多数据

        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        rv_classify.setLayoutManager(manager);
        mAdapter = new CategoryClassifyAdapter(mCategorys);
        mAdapter.setAnimationEnable(true);
        mAdapter.setAnimationFirstOnly(false);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        rv_classify.setAdapter(mAdapter);

        srl_category.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mCurrentPage = 1;
                isLoadMore = false;
                srl_category.finishRefresh(Constant.DEFAULT_SMARTLOADING_DELAY_TIMEOUT);
                mPresenter.fetchGoodsInfo(false,mCurrentPage,mPageSize,mNo);
            }
        });

        srl_category.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (isLoadMore) {
                    mCurrentPage++;
                    mPresenter.fetchGoodsInfo(false,mCurrentPage,mPageSize,mNo);
                    srl_category.finishLoadMore(Constant.DEFAULT_SMARTLOADING_DELAY_TIMEOUT);
                }else{
                    srl_category.finishLoadMore();
                }
            }
        });
    }

    private void initTapbar() {
        mNo = getArguments().getString("type");
        mCategoryName = getArguments().getString("name");

        tv_topTitle.setText(mCategoryName);
        StatusBarUtils.setStatusBarView(getActivity(),ll_inflateTp);
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
    }


    @Override
    protected void lazyFetchData() {
        mPresenter.fetchGoodsInfo(false,mCurrentPage,mPageSize,mNo);
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        lazyFetchData();
    }

    @Override
    public void fetchCategoryList(ClassifyListBean result) {
        if(emptyView.isShowing()){
            emptyView.setVisibility(View.GONE);
            srl_category.setVisibility(View.VISIBLE);
        }
        if(result != null){
            isLoadMore = result.isHasMorePages();
            if(mCurrentPage == 1){
                if(DataUtil.idNotNull(result.getResults())){
                   mAdapter.setNewInstance(result.getResults());
                }else{
                    mAdapter.setNewInstance(new ArrayList<ClassifyListBean.ClassifyItem>());    //注意这里要刷新数据
                    mAdapter.setEmptyView(getEmptyView());
                }
            }else{
                mAdapter.addData(result.getResults());
            }
            if(!isLoadMore){
                ll_endNoData.setVisibility(View.VISIBLE);
                srl_category.setEnableLoadMore(false);
            }else{
                ll_endNoData.setVisibility(View.GONE);
                srl_category.setEnableLoadMore(true);
            }
        }
    }

    @Override
    public void fetchFail(String failReason) {
        ToastUtil.showToast(failReason);
    }

    @Override
    public void fetchNetWorkState(boolean unknowNet) {
        if(!unknowNet){
            mAdapter.setEmptyView(getNoNetView());
        }
    }

    public View getEmptyView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_nogoods,null);
        LinearLayout ll_wrapNoGoods = view.findViewById(R.id.ll_wrapNoGoods);
        ll_wrapNoGoods.setVisibility(View.VISIBLE);
        QMUIRoundButton qrb_refreshAgain = view.findViewById(R.id.qrb_refreshAgain);
        qrb_refreshAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.fetchGoodsInfo(true,mCurrentPage,mPageSize,mNo);
            }
        });
        return view;
    }

    @OnClick({R.id.iv_back})
    public void onClickViewed(View view){
        if (view.getId() == R.id.iv_back) {
            popBackStack();
        }
    }

    private View getNoNetView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_no_net,null);
        QMUIRoundButton qrb_refreshData = view.findViewById(R.id.qrb_refreshNet);
        qrb_refreshData.setOnClickListener(view1 -> {
            if(UserUtil.getInstance().isLogin()){
                mPresenter.fetchGoodsInfo(true,mCurrentPage,mPageSize,mNo);
            }
        });
        return view;
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
