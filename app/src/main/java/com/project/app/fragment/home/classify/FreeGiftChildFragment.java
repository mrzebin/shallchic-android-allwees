package com.project.app.fragment.home.classify;


import android.os.Bundle;
import android.view.KeyEvent;

import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.RecycerLoadMoreScrollView;
import com.project.app.R;
import com.project.app.adapter.FreeGiftAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.FreeGiftBean;
import com.project.app.contract.HomeFreeGiftKindsContract;
import com.project.app.presenter.HomeFreeGiftKindsPresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 分类的详情
 */
public class FreeGiftChildFragment extends BaseMvpQmuiFragment<HomeFreeGiftKindsPresenter> implements HomeFreeGiftKindsContract.View {
    @BindView(R.id.rlv_gift_son)
    RecyclerView rlv_gift_son;
    @BindView(R.id.rlv_moreGift)
    RecycerLoadMoreScrollView rlv_moreGift;

    private FreeGiftAdapter mAdapter;
    private final List<FreeGiftBean.ClassifyItem> mCategorys = new ArrayList<>();
    private String mNo;
    private int mCurrentPage = 1;
    private final int mPageSize = 20;
    private boolean isHasMoreAble = false;
    private String mRequestUrl = "";

    public static FreeGiftChildFragment newInstance(String no) {
        Bundle bundle = new Bundle();
        bundle.putString("type",no);
        FreeGiftChildFragment fragment = new FreeGiftChildFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gift_kinds;
    }

    @Override
    public void initView() {
        initTapbar();
        mPresenter = new HomeFreeGiftKindsPresenter();
        mPresenter.attachView(this);

        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        rlv_gift_son.setLayoutManager(manager);
        mAdapter = new FreeGiftAdapter(mCategorys);
        rlv_gift_son.setAdapter(mAdapter);
    }

    private void initTapbar() {
        mNo = getArguments().getString("type");
        if(mNo.equals("0")){
            mRequestUrl = BaseUrlConfig.getRootHost() + UrlConfig.FREE_GIFTS_BLOW_FIVE;
        }else if(mNo.equals("1")){
            mRequestUrl = BaseUrlConfig.getRootHost() + UrlConfig.FREE_GIFTS_INFLUS;
        }
    }

    public void resetLoadData() {
        mCurrentPage = 1;
        mPresenter.fetchFreeGift(mCurrentPage,mPageSize,mRequestUrl);
    }

    public void addMoreLevel() {
        if(isHasMoreAble){
            mCurrentPage++;
            mPresenter.fetchFreeGift(mCurrentPage,mPageSize,mRequestUrl);
        }
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchFreeGift(mCurrentPage,mPageSize,mRequestUrl);
    }

    @Override
    public void fetchFreeGiftSuccess(FreeGiftBean result) {
        if(result != null){
            if(mCurrentPage == 1){
                if(DataUtil.idNotNull(result.getResults())){
                    mAdapter.setNewInstance(result.getResults());
                }
            }else{
                if(DataUtil.idNotNull(result.getResults())){
                    mAdapter.addData(result.getResults());
                }
            }
            isHasMoreAble = result.isHasMorePages();
        }
    }

    @Override
    public void fetchFail(String failReason) {

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
