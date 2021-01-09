package com.project.app.fragment.earn.reward;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.TextView;

import com.hb.basemodel.utils.DataUtil;
import com.project.app.R;
import com.project.app.adapter.RedeemAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.MeBindCPBean;
import com.project.app.bean.RedeemBean;
import com.project.app.config.AppEnvironmentResConfig;
import com.project.app.contract.RedeemContract;
import com.project.app.presenter.RedeemPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class Re_RedeemFragment extends BaseMvpQmuiFragment<RedeemPresenter> implements RedeemContract.View {
    @BindView(R.id.rlv_redeem)
    RecyclerView rlv_redeem;
    @BindView(R.id.tv_fullPoint)
    TextView tv_fullPoint;
    private Context mContext;
    private final int mCurrentPage = 1;
    private final int mPageSize    = 20;
    private RedeemAdapter mAdapter;
    private final List<RedeemBean.RedeemItem> mDatas = new ArrayList<>();
    private HashMap<String,String> mResMap = new HashMap<>();

    public static Re_RedeemFragment newInstance() {
        return new Re_RedeemFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_reward_redeem;
    }

    @Override
    public void initView() {
        mPresenter = new RedeemPresenter();
        mPresenter.attachView(this);
        initWidget();
        initRes();
    }

    private void initWidget() {
        mContext = getContext();
        rlv_redeem.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new RedeemAdapter(mDatas);
        rlv_redeem.setAdapter(mAdapter);
        mAdapter.setListener(uuid -> mPresenter.upUsePointChangeCoupon(uuid));
    }

    private void initRes() {
        AppEnvironmentResConfig config = AppEnvironmentResConfig.getInstance(getActivity());
        mResMap = config.initReward();
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchRedeemInfo(mCurrentPage,mPageSize);
        mPresenter.fetchBindPointInfo();
    }

    public static String doubleTrans(double d){
        if(Math.round(d)-d==0){
            return String.valueOf((long)d);
        }
        return String.valueOf(d);
    }

    @Override
    public void upChagePointSuccess() {

    }

    @Override
    public void fetchPointSuccess(MeBindCPBean result) {
        if(result != null){
            tv_fullPoint.setText(doubleTrans(result.getValue()) + mResMap.get("str_points"));
        }
    }

    @Override
    public void fetchRedeemInfoSuccess(RedeemBean result) {
        if(result != null){
            if(DataUtil.idNotNull(result.getData())){
                mAdapter.setNewInstance(result.getData());
            }
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