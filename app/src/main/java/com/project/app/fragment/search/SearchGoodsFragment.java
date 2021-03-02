package com.project.app.fragment.search;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ToastUtil;
import com.hb.basemodel.view.DelEditView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.project.app.R;
import com.project.app.adapter.SearchGoodsAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.RecentSearchBean;
import com.project.app.bean.TrendingSearchBean;
import com.project.app.contract.SearchGoodsContract;
import com.project.app.presenter.SearchGoodsPresenter;
import com.project.app.ui.widget.GlobalClassicsFooter;
import com.project.app.ui.widget.GlobalClassicsHeader;
import com.project.app.ui.widget.RecentSearchsGroup;
import com.project.app.ui.widget.SearchTrendingGroup;
import com.project.app.utils.SoftKeyboardManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class SearchGoodsFragment extends BaseMvpQmuiFragment<SearchGoodsPresenter> implements SearchGoodsContract.View {
    @BindView(R.id.ll_delete)
    DelEditView ll_delete;
    @BindView(R.id.tv_cancelSearch)
    TextView tv_cancelSearch;
    @BindView(R.id.ll_searchHistory)
    LinearLayout ll_searchHistory;
    @BindView(R.id.rlv_searchGoods)
    RecyclerView rlv_searchGoods;
    @BindView(R.id.srf_search)
    SmartRefreshLayout srf_search;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.rl_searchNoData)
    RelativeLayout rl_searchNoData;
    @BindView(R.id.qmb_refresh)
    QMUIRoundButton qmb_refresh;
    @BindView(R.id.ll_trendingBranch)
    LinearLayout ll_trendingBranch;
    @BindView(R.id.ll_recentBranch)
    LinearLayout ll_recentBranch;
    @BindView(R.id.group_trending)
    SearchTrendingGroup group_trending;
    @BindView(R.id.group_recent)
    RecentSearchsGroup group_recent;

    private int mCurrentPage = 1;
    private int mPageSize    = 20;
    private int mMaxRecode   = 20;   //最多存储20个记录
    private String mSearchEmpty;
    private boolean isHasMore = false;
    private SearchGoodsAdapter mAapter;
    private String mKeyword;     //单个查询的关键字
    private List<String> keywordList = new ArrayList<>();
    private final List<ClassifyListBean.ClassifyItem> mGoods = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_goods;
    }

    @Override
    public void initView() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        mPresenter = new SearchGoodsPresenter();
        mPresenter.attachView(this);
        initWeight();
        initRecent();
    }

    /**
     * 提取历史搜索数据
     */
    private void initRecent() {
        String  searchHistoryJson =  SPManager.sGetString(Constant.SP_RECODE_SEARCH_KEYWORD);
        if(!TextUtils.isEmpty(searchHistoryJson)){
            RecentSearchBean searchBean = JsonUtils.deserialize(searchHistoryJson,RecentSearchBean.class);
            List<RecentSearchBean.SearchHistoryItem> items = searchBean.getHistoryItems();
            if(DataUtil.idNotNull(items)){      //历史记录不为空
                if(ll_searchHistory.getVisibility() == View.VISIBLE){
                    ll_recentBranch.setVisibility(View.VISIBLE);
                    Collections.reverse(items);
                    group_recent.addItemViews(items,RecentSearchsGroup.TV_MODE);
                    group_recent.setGroupClickListener(new RecentSearchsGroup.OnGroupItemClickListener() {
                        @Override
                        public void onGroupItemClick(String keyName) {
                            if(!TextUtils.isEmpty(keyName)){
                                startProgressDialog(getContext());
                                ll_delete.editText.setText(keyName);
                                mCurrentPage = 1;
                                mPresenter.searchGoods(true,mCurrentPage,mPageSize,keyName);
                            }
                        }
                    });
                }
            }
        }
    }

    private void initWeight() {
        mSearchEmpty = getContext().getString(R.string.toast_search_empty);
        srf_search.setRefreshHeader(new GlobalClassicsHeader(getContext()));
        srf_search.setRefreshFooter(new GlobalClassicsFooter(getContext()));
        srf_search.setHeaderHeight(60);
        srf_search.setFooterHeight(50);
        srf_search.setEnableLoadMoreWhenContentNotFull(false);

        mAapter = new SearchGoodsAdapter(mGoods);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        rlv_searchGoods.setLayoutManager(manager);
        rlv_searchGoods.setAdapter(mAapter);

        srf_search.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                String keytag = ll_delete.editText.getText().toString().trim();
                if(TextUtils.isEmpty(keytag)){
                    srf_search.finishRefresh();
                    return;
                }else{
                    mCurrentPage = 1;
                    mPresenter.searchGoods(false,mCurrentPage,mPageSize,keytag);
                }
            }
        });

        srf_search.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                srf_search.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(refreshLayout != null){
                            refreshLayout.finishLoadMore();
                        }
                    }
                },1000);

                if(isHasMore){
                  mCurrentPage++;
                    String keytag = ll_delete.editText.getText().toString().trim();
                    if(TextUtils.isEmpty(keytag)){
                        return;
                    }else{
                        mPresenter.searchGoods(false,mCurrentPage,mPageSize,keytag);
                    }
                }
            }
        });

        ll_delete.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT){
                    String keytag = ll_delete.editText.getText().toString().trim();
                    if(TextUtils.isEmpty(keytag)){
                        ToastUtil.showToast(mSearchEmpty);
                        return false;
                    }else{
                        mKeyword = keytag;
                        mCurrentPage = 1;
                        RecentSearchBean searchBean = null;
                        String  searchJson =  SPManager.sGetString(Constant.SP_RECODE_SEARCH_KEYWORD);
                        if(!TextUtils.isEmpty(searchJson)){
                            searchBean = JsonUtils.deserialize(searchJson,RecentSearchBean.class);
                            List<RecentSearchBean.SearchHistoryItem> searchItemList = searchBean.getHistoryItems();
                            if(searchItemList.size() >= mMaxRecode){    //最多20个,超过20则删除第一条记录
                                searchItemList.remove(0);
                            }
                            searchItemList.add(new RecentSearchBean.SearchHistoryItem(keytag));
                            searchBean.setHistoryItems(searchItemList);
                        }else{
                            searchBean = new RecentSearchBean();
                            List<RecentSearchBean.SearchHistoryItem> cellItem = new ArrayList<>();
                            cellItem.add(new RecentSearchBean.SearchHistoryItem(keytag));
                            searchBean.setHistoryItems(cellItem);
                        }
                        SPManager.sPutString(Constant.SP_RECODE_SEARCH_KEYWORD, JsonUtils.serialize(searchBean));
                        mPresenter.searchGoods(true,mCurrentPage,mPageSize,keytag);
                        SoftKeyboardManager.hideShowKeyboard(getContext());
                        return true;
                    }
                }
                return false;
            }
        });

        ll_delete.setListener(new DelEditView.DelObserveListener() {
            @Override
            public void editClearDataObserve() {
                searchInitStatus();
            }

            @Override
            public void editHasDataObserve() {
                tv_cancelSearch.setTextColor(getResources().getColor(R.color.theme_color));
            }
        });
    }

    /**
     * 初始化状态
     */
    private void searchInitStatus() {
        mCurrentPage = 1;
        SoftKeyboardManager.hideShowKeyboard(getContext());
        mAapter.setNewInstance(new ArrayList<ClassifyListBean.ClassifyItem>());     //清理搜索的数据
        tv_cancelSearch.setTextColor(getResources().getColor(R.color.color_999));
        iv_back.setVisibility(View.GONE);
        srf_search.setVisibility(View.GONE);
        rl_searchNoData.setVisibility(View.GONE);
        ll_searchHistory.setVisibility(View.VISIBLE);
        initRecent();    //刷新搜索的列表
    }

    /**
     * 显示搜索的结果
     */
    private void fullSearchStatus(){
        ll_searchHistory.setVisibility(View.GONE);
        rl_searchNoData.setVisibility(View.GONE);
        iv_back.setVisibility(View.VISIBLE);
        srf_search.setVisibility(View.VISIBLE);
    }

    /**
     * 空数据状态
     */
    public void emptySearchStatus(){
        srf_search.setVisibility(View.GONE);
        ll_searchHistory.setVisibility(View.GONE);
        iv_back.setVisibility(View.VISIBLE);
        rl_searchNoData.setVisibility(View.VISIBLE);
    }

    /**
     * 刷新按钮的状态
     */
    public void refreshSearchStatus(){
        String keytag = ll_delete.editText.getText().toString().trim();
        if(!TextUtils.isEmpty(keytag)){
            mCurrentPage = 1;
            mPresenter.searchGoods(true,mCurrentPage,mPageSize,keytag);
        }
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.trendingSearchs();
    }

    @OnClick({R.id.tv_cancelSearch,R.id.iv_back,R.id.qmb_refresh})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.tv_cancelSearch:
                if(TextUtils.isEmpty(ll_delete.editText.getText().toString().trim())){
                    popBackStack();
                }else{
                    ll_delete.clearText();
                }
                break;
            case R.id.iv_back:
                searchInitStatus();
                break;
            case R.id.qmb_refresh:
                startProgressDialog(getContext());
                refreshSearchStatus();
                break;
        }
    }

    @Override
    public void fetchTrendingSearchSuccess(List<TrendingSearchBean.SearchItem> result) {
        if(DataUtil.idNotNull(result)){
            ll_trendingBranch.setVisibility(View.VISIBLE);
            group_trending.addItemViews(result,SearchTrendingGroup.TV_MODE);
            group_trending.setGroupClickListener(new SearchTrendingGroup.OnGroupItemClickListener() {
                @Override
                public void onGroupItemClick(String keyName) {
                    if(!TextUtils.isEmpty(keyName)){
                        startProgressDialog(getContext());
                        ll_delete.editText.setText(keyName);
                        mCurrentPage = 1;
                        mPresenter.searchGoods(true,mCurrentPage,mPageSize,keyName);
                    }
                }
            });
        }else{
            ll_trendingBranch.setVisibility(View.GONE);
        }
    }

    @Override
    public void fetchTrendingSearchFail(String result) {
        ll_trendingBranch.setVisibility(View.GONE);
    }

    @Override
    public void fetchSearchTargetSuccess(ClassifyListBean result) {
        srf_search.finishRefresh();
        if(!keywordList.contains(mKeyword)){
            keywordList.add(mKeyword);
        }
        if(mCurrentPage == 1){
            if(DataUtil.idNotNull(result.getResults())){
                fullSearchStatus();
                mAapter.setNewInstance(result.getResults());
            }else{
                emptySearchStatus();
            }
            isHasMore = result.isHasMorePages();
            if(!isHasMore){
                srf_search.finishLoadMore();
            }
        }else{
            if(DataUtil.idNotNull(result.getResults())){
                mAapter.addData(result.getResults());
            }
            isHasMore = result.isHasMorePages();
            if(!isHasMore){
                srf_search.finishLoadMore();
            }
        }
    }

    @Override
    public void fetchSearchTargetFail(String result) {
        srf_search.finishRefresh();
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
        stopProgressDialog();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
