package com.project.app.fragment.home;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.AppUtils;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ToastUtil;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.adapter.CategoryAdapter;
import com.project.app.adapter.CategoryMenuAdapter;
import com.project.app.base.BaseMvpController;
import com.project.app.bean.CategoryAllBean;
import com.project.app.bean.CategoryBean;
import com.project.app.config.AppsFlyConfig;
import com.project.app.contract.CategoryContract;
import com.project.app.fragment.search.SearchGoodsFragment;
import com.project.app.presenter.CategoryPresenter;
import com.project.app.utils.AppsFlyEventUtils;
import com.project.app.utils.ItemSpaceDecoration;
import com.project.app.utils.LocaleUtil;
import com.project.app.utils.StatusBarUtils;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryController extends BaseMvpController<CategoryPresenter> implements CategoryContract.View{
    @BindView(R.id.rv_cGoods)
    RecyclerView rv_cGoods;
    @BindView(R.id.rlv_categoryMenu)
    RecyclerView rlv_categoryMenu;
    @BindView(R.id.fl_inflate)
    FrameLayout fl_inflate;
    @BindView(R.id.emptyView)
    QMUIEmptyView emptyView;
    @BindView(R.id.ll_category)
    LinearLayout ll_category;
    @BindView(R.id.ll_wrapNoGoods)  //判断有没有商品
    LinearLayout ll_wrapNoGoods;
    @BindView(R.id.cl_noNet)        //没有网络
            ConstraintLayout cl_noNet;
    @BindView(R.id.ll_goSearch)
    LinearLayout ll_goSearch;

    private boolean isInit = false;
    private boolean isFromClick = false;  //防止来回调
    private CategoryAdapter mAdapter;
    private CategoryMenuAdapter mMenuAdapter;
    private LinearLayoutManager mMenuManager;
    private LinearLayoutManager mGoodsManager;

    private final List<CategoryAllBean.CategoryItem> mCategorys = new ArrayList<>();
    private final List<CategoryAllBean.CategoryItem> mCategoryMenu = new ArrayList<>();
    private Handler mHandler = new Handler();

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
        mGoodsManager = new LinearLayoutManager(getContext());
        mMenuManager  = new LinearLayoutManager(getContext());
        String localLanguate = LocaleUtil.getInstance().getLanguage();

        rv_cGoods.setLayoutManager(mGoodsManager);
        rlv_categoryMenu.setLayoutManager(mMenuManager);

        mMenuAdapter = new CategoryMenuAdapter(mCategoryMenu,localLanguate);
        mAdapter     = new CategoryAdapter(getContext(),mCategorys,localLanguate);
        rlv_categoryMenu.setAdapter(mMenuAdapter);

        ItemSpaceDecoration itemVerticalDecor = new ItemSpaceDecoration(getContext());
        itemVerticalDecor.setTop(15);
        rv_cGoods.addItemDecoration(itemVerticalDecor);
        rv_cGoods.setAdapter(mAdapter);

        rv_cGoods.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(isFromClick){
                        changePosition();
                    }
                    isFromClick = false;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isFromClick){
                    return;
                }
                changePosition();
            }
        });

        mMenuAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                mMenuAdapter.setmSelectIndex(position);
                isFromClick = true;
                LinearSmoothScroller topScroller = new LinearSmoothScroller(getContext()){
                    @Override
                    protected int getHorizontalSnapPreference() {
                        return SNAP_TO_START;
                    }

                    @Override
                    protected int getVerticalSnapPreference() {
                        return SNAP_TO_START;
                    }

                    @Override
                    protected void onStop() {
                        super.onStop();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isFromClick = false;
                            }
                        },500);
                    }
                };
                //此方法可以置顶,但是没有滑动效果
                mGoodsManager.scrollToPositionWithOffset(position,0);
                topScroller.setTargetPosition(position);
//                mGoodsManager.startSmoothScroll(topScroller);
            }
        });
        loadData();
    }

    private void changePosition() {
       int firstPosition = mGoodsManager.findFirstVisibleItemPosition();
        mMenuAdapter.setmSelectIndex(firstPosition);
        rlv_categoryMenu.scrollToPosition(mMenuAdapter.mSelectIndex);   //此方法无置顶效果

//       if(mMenuAdapter.mSelectIndex != firstPosition){
//           mMenuAdapter.setmSelectIndex(firstPosition);
//           rlv_categoryMenu.scrollToPosition(mMenuAdapter.mSelectIndex);   //此方法无置顶效果
////           rlv_categoryMenu.scrollToPosition(firstPosition);   //此方法无置顶效果
//       }
    }

    private void initTopBar() {
        StatusBarUtils.setStatusBarView(getContext(),fl_inflate);
    }

    public void onRefreshView() {
        if(!isInit){
            loadData();
        }
    }

    private void loadData(){
        String platform = Constant.DEVICE_RESOURCE;
        String device = SPManager.sGetString(Constant.SP_DEVICE_ID_FLAG);
        String version = AppUtils.getVersionName(getContext());
        mPresenter.fetchCateofyAll(platform,device,version);
    }

    private View getNoNetView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_no_net,null);
        QMUIRoundButton qrb_refreshData = view.findViewById(R.id.qrb_refreshNet);
        qrb_refreshData.setOnClickListener(view1 -> loadData());
        return view;
    }

    public View getEmptyView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_nodata,null);
        QMUIRoundButton qrb_refreshData = view.findViewById(R.id.qrb_refreshOrder);
        qrb_refreshData.setOnClickListener(view1 ->loadData());
        return view;
    }

    /**
     * 有效的视图显示
     */
    private void swtichValidView(){
        ll_category.setVisibility(VISIBLE);
        ll_wrapNoGoods.setVisibility(GONE);
        emptyView.setVisibility(GONE);
        cl_noNet.setVisibility(GONE);
    }

    /**
     * 无效的视图显示
     */
    private void switchNoGoodsView(){
        ll_category.setVisibility(GONE);
        ll_wrapNoGoods.setVisibility(VISIBLE);
        emptyView.setVisibility(GONE);
        cl_noNet.setVisibility(GONE);
    }

    /**
     * 显示无网络
     */
    private void switchNoNetView(){
        ll_category.setVisibility(GONE);
        ll_wrapNoGoods.setVisibility(GONE);
        emptyView.setVisibility(GONE);
        cl_noNet.setVisibility(VISIBLE);
    }

    @OnClick({R.id.qrb_refreshAgain,R.id.qrb_refreshNet,R.id.ll_goSearch})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.qrb_refreshAgain:
                startLoading();
                loadData();
                break;
            case R.id.qrb_refreshNet:
                startLoading();
                loadData();
                break;
            case R.id.ll_goSearch:
                HolderActivity.startFragment(getContext(),SearchGoodsFragment.class);
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_SEARCH);
                break;
        }
    }

    @Override
    public void startLoading() {
        showProgressDialog();
    }

    @Override
    public void stopLoading() {
        hideProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void fetchSuccess(CategoryBean result) {
        if(DataUtil.idNotNull(result.getCategories())){
            isInit = true;
        }else{
            mAdapter.setEmptyView(getEmptyView());
        }
    }

    @Override
    public void fetchCategoryAllSuccess(CategoryAllBean result) {
        if(result != null){
             if(DataUtil.idNotNull(result.getCategories())){
                isInit = true;
                swtichValidView();
                mAdapter.setNewInstance(filterData(result.getCategories()));
                mMenuAdapter.setNewInstance(filterData(result.getCategories()));
             }else{
                 switchNoGoodsView();
             }
        }
    }

    private List<CategoryAllBean.CategoryItem> filterData(List<CategoryAllBean.CategoryItem> categories) {
        List<CategoryAllBean.CategoryItem> tempDatas = new ArrayList<>();
        for(CategoryAllBean.CategoryItem item:categories){
            if(!TextUtils.isEmpty(item.getName())){
                tempDatas.add(item);
            }
        }
        return tempDatas;
    }

    @Override
    public void fetchFail(String failReason) {
        ToastUtil.showToast(failReason);
    }

    @Override
    public void fetchNetWorkState(boolean isValidNet) {
        if(!isValidNet){
            mAdapter.setEmptyView(getNoNetView());
        }
    }

    @Override
    public void fetchExceptModel(int model) {
        if(model == Constant.EXCEPT_STATE_NETEXCEPT){     //显示没有网络
            switchNoNetView();
        }
    }

}
