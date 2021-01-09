package com.project.app.fragment.address;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.adapter.AddressMaangerAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.AddressBean;
import com.project.app.contract.AddressManagerContract;
import com.project.app.fragment.home.ShippingAddressFragment;
import com.project.app.presenter.AddressManagerPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class AddressManagerFragment extends BaseMvpQmuiFragment<AddressManagerPresenter> implements AddressManagerContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.srl_addressBook)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.rlv_addressList)
    RecyclerView rlv_addressList;
    @BindView(R.id.btn_userA)
    Button btn_userA;
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    private String mType = "0"; //0购物车进入 1设置进入(方便通知刷新地址)
    private boolean isPrepared = false;
    private AddressMaangerAdapter mAdapter;
    private List<AddressBean.AddressItem> addressList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_address_manager;
    }

    @Override
    public void initView() {
        initBundle();
        initWidget();
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if(bundle != null){
            mType = bundle.getString("type");
        }
    }

    private void initWidget() {
        EventBus.getDefault().register(this);
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.allwees_theme_color,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);

        addressList = new ArrayList<>();
        mPresenter = new AddressManagerPresenter();
        mPresenter.attachView(this);

        rlv_addressList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AddressMaangerAdapter(addressList,Integer.valueOf(mType));
        mAdapter.addFooterView(getFooterView());
        mAdapter.setListener(new AddressMaangerAdapter.OpreationAddressListener() {
            @Override
            public void oprDelete(int position, String addressId) {
                showDeleteDialog(position,addressId);
            }
            @Override
            public void reRefreshCheck(int position, String addressId) {
                mPresenter.selectAddress(addressId);
            }
        });
        rlv_addressList.setAdapter(mAdapter);
        if(mType.equals("1")){
            btn_userA.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        isPrepared = false;
        mPresenter.fetchAddressList();
        initLoadStyle();
    }

    private View getFooterView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_new_address,null);
        view.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putInt("type",ShippingAddressFragment.INDEX_ACTION_ADD_ADDRESS_ME);
            Intent goEditAddress = HolderActivity.of(getContext(), ShippingAddressFragment.class,bundle);
            getContext().startActivity(goEditAddress);
        });
        return view;
    }

    @OnClick({R.id.btn_userA,R.id.iv_back})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.btn_userA:
            case R.id.iv_back:
                popBackStack();
                break;
        }
    }

    private void initLoadStyle(){
        if(!isPrepared){
            mSwipeRefresh.setRefreshing(true);
        }
        mSwipeRefresh.postDelayed(() -> {
            if(mSwipeRefresh != null){
                mSwipeRefresh.setRefreshing(false);
            }
        },Constant.DELAY_LOADING_TIME_OUT);
    }

    private void showDeleteDialog(int position,String addressId){
        if (addressList.size() == 1){
            String hint = getContext().getResources().getString(R.string.hint_please_keep_one);
            ToastUtil.showToast(hint);
            return;
        }

        String title = getContext().getResources().getString(R.string.hint_delete_address_item);
        String no    = getContext().getResources().getString(R.string.str_no);
        String yes   = getContext().getResources().getString(R.string.str_yes);

        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle(title)
                .setMessage("")
                .addAction(no, (dialog, index) -> dialog.dismiss())
                .addAction(0,yes, QMUIDialogAction.ACTION_PROP_NEGATIVE, (dialog, index) -> {
                    mPresenter.deleteSpecifyAddress(addressId);
                    dialog.dismiss();
                })
                .create().show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(RefreshDataEvent event){
        if(event.getmMsg().equals(Constant.EVENT_REFRESH_ADDRESS_LIST_DATA)){
            mPresenter.fetchAddressList();
        }
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchAddressList();
        initLoadStyle();
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
    public void fetchFail(String failReason) {
        ToastUtil.showToast(failReason);
    }

    @Override
    public void fetchAddressSuccess(AddressBean result) {
        if(result != null){
            isPrepared = true;
            if(DataUtil.idNotNull(result.getResults())){
                addressList = result.getResults();
                mAdapter.setNewInstance(result.getResults());
            }else{
                addressList.clear();
                mAdapter.setNewInstance(result.getResults());
            }
        }
    }

    @Override
    public void deleteAddressSuccess() {
        mPresenter.fetchAddressList();
        EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_ADD_ADDRESS_SURE));
    }

    @Override
    public void selectAddressSuccess() {
        mPresenter.fetchAddressList();
        EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_ADD_ADDRESS_SURE));
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
        EventBus.getDefault().unregister(this);
    }
}
