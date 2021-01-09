package com.project.app.fragment.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.adapter.CartMLikeAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.ClassifyListBean;
import com.project.app.contract.PayStatusContract;
import com.project.app.fragment.order.OrderDetailFragment;
import com.project.app.presenter.PayStatusPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class PayStatusFragment extends BaseMvpQmuiFragment<PayStatusPresenter> implements PayStatusContract.View {
    @BindView(R.id.tv_payDetail_dec)
    TextView tv_payDetail_dec;
    @BindView(R.id.tv_lookOtherOrder)
    TextView tv_lookOtherOrder;
    @BindView(R.id.tv_paySBackHome)
    TextView tv_paySBackHome;
    @BindView(R.id.rlv_cartMightLike)
    RecyclerView rlv_cartMightLike;
    private CartMLikeAdapter mAdapter;

    private final int mPageSize    = 20;
    private final int mCurrentPage = 6;
    private boolean isLoadMoreEnable = false;
    private boolean isPrepared = false;
    private String mOrderId;
    private String mOrderType;
    private final List<ClassifyListBean.ClassifyItem> mCategorys = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pay_status_success;
    }

    @Override
    public void initView() {
        initBundle();
        initWidget();
    }

    @OnClick({R.id.tv_lookOtherOrder,R.id.tv_paySBackHome})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.tv_lookOtherOrder:
                Bundle bundle = new Bundle();
                bundle.putString("orderId",mOrderId);
                bundle.putString("orderType",mOrderType);
                popBackStack();
                Intent intent = HolderActivity.of(getContext(), OrderDetailFragment.class,bundle);
                getContext().startActivity(intent);
                break;
            case R.id.tv_paySBackHome:
                EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_DEEPLINK_HOME));
                popBackStack();
                break;
        }
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if(bundle != null){
            mOrderId = bundle.getString("orderId");
            mOrderType = bundle.getString("orderType");
        }
    }

    private void initWidget() {
        String hint = getContext().getResources().getString(R.string.ps_hint_1);
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        mPresenter = new PayStatusPresenter();
        mPresenter.attachView(this);
        String email = SPManager.sGetString( "email");
        tv_payDetail_dec.setText(hint + email);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rlv_cartMightLike.setLayoutManager(manager);
        mAdapter = new CartMLikeAdapter(mCategorys);
        mAdapter.setAnimationEnable(true);
        mAdapter.setAnimationFirstOnly(false);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        rlv_cartMightLike.setAdapter(mAdapter);
        SPManager.sPutInt(Constant.SP_SAVE_CART_GOODS_NUM,0);
        EventBus.getDefault().post(new RefreshDataEvent(Constant.REFRESH_AFTER_PAY_CARDATA));
        EventBus.getDefault().post(new RefreshDataEvent(Constant.EVNET_CHANGE_CURRENT_CART_NUM));
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchCLikeList(mCurrentPage,mPageSize);
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
    public void fetchMightLikeData(ClassifyListBean result) {
        if(result != null){
            isPrepared = true;
            if(mCurrentPage == 6){
                if(DataUtil.idNotNull(result.getResults())){
                    mAdapter.setNewInstance(result.getResults());
                }
            }else{
                mAdapter.addData(result.getResults());
            }
            isLoadMoreEnable = result.isHasMorePages();
        }
    }

    @Override
    public void fetchFail(String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }

}
