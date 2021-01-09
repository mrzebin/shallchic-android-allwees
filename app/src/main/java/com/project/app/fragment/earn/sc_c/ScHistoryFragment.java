package com.project.app.fragment.earn.sc_c;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.RecycerLoadMoreScrollView;
import com.project.app.R;
import com.project.app.adapter.ScHistoryAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.ScCashBean;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class ScHistoryFragment extends BaseMvpQmuiFragment{
    @BindView(R.id.rlv_historyCs)
    RecyclerView rlv_historyCs;
    @BindView(R.id.rlm_moreCsHistory)
    RecycerLoadMoreScrollView rlm_moreCsHistory;

    private Context mContext;
    private ScHistoryAdapter mAdapter;
    private final List<ScCashBean.CashItem> mData = new ArrayList<>();

    public static ScHistoryFragment newInstance() {
        return new ScHistoryFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sc_history;
    }

    @Override
    public void initView() {
        initWidgetView();
    }

    private void initWidgetView() {
        mContext = getContext();
        rlv_historyCs.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ScHistoryAdapter(mData);
        rlv_historyCs.setAdapter(mAdapter);

        rlm_moreCsHistory.setScrollViewListener(new RecycerLoadMoreScrollView.ScrollViewListener() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onScrollChanged(RecycerLoadMoreScrollView scrollView, int x, int y, int oldx, int oldy) {

            }
        });
    }

    @Override
    protected void lazyFetchData() {

    }

    public void syncData(List<ScCashBean.CashItem> results,int position) {
        if(position == 1){
            if(DataUtil.idNotNull(results)){
                mAdapter.setNewInstance(results);
            }else{
                mAdapter.setEmptyView(getEmptyView());
            }
        }else{
            mAdapter.addData(results);
        }
    }

    public View getEmptyView(){
        return LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_no_cs_history,null);
    }

}
