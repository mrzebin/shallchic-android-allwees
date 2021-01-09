package com.project.app.fragment.home;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.AppUtils;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.project.app.R;
import com.project.app.adapter.CategoryAdapter;
import com.project.app.base.BaseMvpController;
import com.project.app.bean.CategoryBean;
import com.project.app.bean.CategoryItem;
import com.project.app.contract.CategoryContract;
import com.project.app.presenter.CategoryPresenter;
import com.project.app.utils.FilterDataUtils;
import com.project.app.utils.ItemSpaceDecoration;
import com.project.app.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryController extends BaseMvpController<CategoryPresenter> implements CategoryContract.View,SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.qmTopBar)
    QMUITopBarLayout qmTopBar;
    @BindView(R.id.srf_category)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.rv_cGoods)
    RecyclerView rv_cGoods;
    @BindView(R.id.fl_inflate)
    FrameLayout fl_inflate;
    private boolean isPrepared = false;
    private CategoryAdapter mAdapter;
    private final List<CategoryItem> mCategorys = new ArrayList<>();
    private static final int REFRESH_LOADING_TIMEOUT = 3;

    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == REFRESH_LOADING_TIMEOUT) {
                if (mSwipeRefresh != null) {
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        }
    };

    public CategoryController(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.category_layout,this);
        ButterKnife.bind(this);
        initTopBar();
        initView();
    }

    private void initView() {
        mPresenter = new CategoryPresenter();
        mPresenter.attachView(this);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.allwees_theme_color,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);
        GridLayoutManager manager = new GridLayoutManager(getContext(),3);
        rv_cGoods.setLayoutManager(manager);
        mAdapter = new CategoryAdapter(mCategorys);
        ItemSpaceDecoration itemVerticalDecor = new ItemSpaceDecoration(getContext());
        itemVerticalDecor.setTop(20);
        rv_cGoods.addItemDecoration(itemVerticalDecor);
        rv_cGoods.setAdapter(mAdapter);
        loadData();
    }

    private void initTopBar() {
        StatusBarUtils.setStatusBarView(getContext(),fl_inflate);
        qmTopBar.setTitle(getContext().getString(R.string.category_title));
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    private void loadData(){
        String platform = Constant.DEVICE_RESOURCE;
        String device = SPManager.sGetString(Constant.SP_DEVICE_ID_FLAG);
        String version = AppUtils.getVersionName(getContext());
        mHandler.sendEmptyMessageDelayed(REFRESH_LOADING_TIMEOUT, Constant.DELAY_LOADING_TIME_OUT);
        mPresenter.fetchGoodsInfo(platform,device,version);
    }

    public void refreshNoNetState(String netType){
        if(netType.equals("NONE")){
            mAdapter.setEmptyView(getNoNetView());
        }else{
            if(!isPrepared){
                loadData();
            }
        }
    }

    private View getNoNetView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_no_net,null);
        QMUIRoundButton qrb_refreshData = view.findViewById(R.id.qrb_refreshNet);
        qrb_refreshData.setOnClickListener(view1 -> loadData());
        return view;
    }

    public View getEmptyView(){
        return LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_view,null);
    }

    public void onRefreshView() {

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
    public void fetchSuccess(CategoryBean result) {
        mSwipeRefresh.setRefreshing(false);
        if(DataUtil.idNotNull(result.getCategories())){
            isPrepared = true;
            mAdapter.setNewInstance(FilterDataUtils.filterData(result).getCategories());
        }else{
            mAdapter.setEmptyView(getEmptyView());
        }
    }

    @Override
    public void fetchFail(String failReason) {
        mSwipeRefresh.setRefreshing(false);
        ToastUtil.showToast(failReason);
    }

    @Override
    public void fetchNetWorkState(boolean isValidNet) {
        if(!isValidNet){
            mAdapter.setEmptyView(getNoNetView());
        }
    }
}
