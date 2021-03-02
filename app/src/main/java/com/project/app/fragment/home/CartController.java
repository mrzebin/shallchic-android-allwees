package com.project.app.fragment.home;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ToastUtil;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.adapter.CartMLikeAdapter;
import com.project.app.adapter.CartOrderAdapter;
import com.project.app.base.BaseMvpController;
import com.project.app.bean.AddressBean;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.bean.CartCouponsBaean;
import com.project.app.bean.CartItemReqBean;
import com.project.app.bean.CategoryBean;
import com.project.app.bean.ChionWrapperBean;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.PayOrderBean;
import com.project.app.config.AppsFlyConfig;
import com.project.app.contract.CartContract;
import com.project.app.fragment.AddMoreGoodsFragment;
import com.project.app.fragment.WebExplorerFragment;
import com.project.app.fragment.address.AddressManagerFragment;
import com.project.app.fragment.home.classify.DeliverDoubleFragment;
import com.project.app.fragment.pay.PayStatusFragment;
import com.project.app.fragment.pay.PaymentFragment;
import com.project.app.presenter.CartPresenter;
import com.project.app.ui.dialog.CountSelectDialog;
import com.project.app.ui.dialog.CouponListDialog;
import com.project.app.ui.widget.GlobalClassicsFooter;
import com.project.app.ui.widget.GlobalClassicsHeader;
import com.project.app.utils.AppsFlyEventUtils;
import com.project.app.utils.LocaleUtil;
import com.project.app.utils.MathUtil;
import com.project.app.utils.StatusBarUtils;
import com.project.app.utils.StringUtils;
import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartController extends BaseMvpController<CartPresenter> implements CartContract.View, SwipeRefreshLayout.OnRefreshListener, CountSelectDialog.BuyCallbackListener, CouponListDialog.CouponCallBack {
    @BindView(R.id.qmTopBar)
    QMUITopBarLayout qmTopBar;
    @BindView(R.id.srl_cart)
    SmartRefreshLayout srl_cart;
    @BindView(R.id.fl_inflate)
    FrameLayout fl_inflate;
    @BindView(R.id.rlv_cartMightLike)
    RecyclerView rlv_cartMightLike;
    @BindView(R.id.ll_endNoData)
    LinearLayout ll_endNoData;
    @BindView(R.id.qmui_emptyCart)
    QMUIRoundButton qmui_emptyCart;
    @BindView(R.id.qmui_checkOut)
    QMUIRoundButton qmui_checkOut;
    @BindView(R.id.ll_cartCheckout)
    LinearLayout ll_cartCheckout;
    @BindView(R.id.ll_cartLike)
    LinearLayout ll_cartLike;
    @BindView(R.id.ll_cartInfo)
    LinearLayout ll_cartInfo;
    @BindView(R.id.ll_cartPartLike)
    LinearLayout ll_cartPartLike;
    @BindView(R.id.tv_buyOriginCP)
    TextView tv_buyOriginCP;
    @BindView(R.id.tv_buyShipCP)
    TextView tv_buyShipCp;
    @BindView(R.id.tv_freeShipHint)
    TextView tv_freeShipHint;
    @BindView(R.id.tv_totalCp)
    TextView tv_totalCp;
    @BindView(R.id.rlv_carts)
    RecyclerView rlv_carts;
    @BindView(R.id.tv_goRefundWeb)
    TextView tv_goRefundWeb;
    @BindView(R.id.et_inputPromo)
    EditText et_inputPromo;
    @BindView(R.id.ll_cIncreaseG)
    LinearLayout ll_cIncreaseG;
    @BindView(R.id.tv_deductMore)
    TextView tv_deductMore;
    @BindView(R.id.ll_skipAManager)
    LinearLayout ll_skipAManager;
    @BindView(R.id.tv_retriveAddress)
    TextView tv_retriveAddress;
    @BindView(R.id.rl_freeCash)
    RelativeLayout rl_freeCash;
    @BindView(R.id.tv_cashAmount)
    TextView tv_cashAmount;
    @BindView(R.id.rl_scBalance)
    RelativeLayout rl_scBalance;
    @BindView(R.id.cb_applyCash)
    CheckBox cb_applyCash;
    @BindView(R.id.tv_carCashMoney)
    TextView tv_carCashMoney;
    @BindView(R.id.tv_buyAmtCouponP)
    TextView tv_buyAmtCouponP;
    @BindView(R.id.rl_amtCoupon)
    RelativeLayout rl_amtCoupon;
    @BindView(R.id.tv_invalidCouponCode)
    TextView tv_invalidCouponCode;
    @BindView(R.id.tv_rightArrow)
    TextView tv_rightArrow;
    @BindView(R.id.tv_useCouponCode)
    TextView tv_useCouponCode;
    @BindView(R.id.ll_freeOneWrapper)
    LinearLayout ll_freeOneWrapper;
    @BindView(R.id.rl_deductionPrice)
    RelativeLayout rl_deductionPrice;
    @BindView(R.id.tv_deductionPrice)
    TextView tv_deductionPrice;
    @BindView(R.id.emptyView)
    QMUIEmptyView emptyView;
    @BindView(R.id.tv_cart_mightLike_header)
    TextView tv_cart_mightLike_header;

    private int VIEW_MODE    = -1;        //切换视图的状态 0为like 1为购物车
    private int mPageSize    = 20;
    private int mCurrentPage = 6;
    private int mUserCouponCode  = 0;      //0位默认操作 1为使用couponCode操作
    private boolean isLoadMore;
    private boolean isPrepared = false;    //判断预加载mightlike有没有加载
    private CartMLikeAdapter mAdapter;
    private CartOrderAdapter mOrderAdapter;
    private CartBuyDataBean  mCarBuyInfo;
    private final List<ClassifyListBean.ClassifyItem> mCategorys = new ArrayList<>();
    private final List<CartBuyDataBean.ProductItem> mCartProducts = new ArrayList<>();
    private final String mUserCouponId = "";
    private String mFreeShipping  = "";
    private String mUserAddressId = "";
    private String mAddressUuid   = "";  //选择地址的uuid
    private String mDefaultPhoneNum = ""; //绑定的手机号码
    private AddressBean mAddressManager;
    private CountSelectDialog mCountSelectDialog;
    private CouponListDialog mCouponDialog;
    private CartItemReqBean mRequestOrderFrom;

    public CartController(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.cart_layout,this);
        ButterKnife.bind(this,view);
        initTopBar();
        initView();
        initData();
    }

    private void initTopBar() {
        qmTopBar.setTitle(getContext().getString(R.string.cart_title));
        StatusBarUtils.setStatusBarView(getContext(),fl_inflate);
    }

    private void initView() {
        mPresenter = new CartPresenter();
        mPresenter.attachView(this);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rlv_cartMightLike.setLayoutManager(manager);
        mAdapter = new CartMLikeAdapter(mCategorys);
        mAdapter.setAnimationEnable(true);
        rlv_cartMightLike.setAdapter(mAdapter);
        qmui_checkOut.setChangeAlphaWhenPress(true);
        mCountSelectDialog = new CountSelectDialog(getContext(),this);
        mCouponDialog      = new CouponListDialog(getContext(),this);
        mFreeShipping      = getContext().getResources().getString(R.string.str_free);

        srl_cart.setRefreshHeader(new GlobalClassicsHeader(getContext()));
        srl_cart.setRefreshFooter(new GlobalClassicsFooter(getContext()));
        srl_cart.setHeaderHeight(60);
        srl_cart.setFooterHeight(50);
        srl_cart.setEnableLoadMore(false); //默认不让起有更多数据

        QMUIRVItemSwipeAction swipeAction = new QMUIRVItemSwipeAction(true, new QMUIRVItemSwipeAction.Callback() {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                List<CartBuyDataBean.ProductItem> datas = mCarBuyInfo.getItems();
                CartBuyDataBean.ProductItem productItem = datas.get(position);
                mPresenter.deleteItemBuyGoods(productItem.product.getUuid(),productItem.sku.getUuid(),"delete");
                mOrderAdapter.remove(viewHolder.getAdapterPosition());
            }

            @Override
            public int getSwipeDirection(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return QMUIRVItemSwipeAction.SWIPE_LEFT;
            }

            @Override
            public void onClickAction(QMUIRVItemSwipeAction swipeAction, RecyclerView.ViewHolder selected, QMUISwipeAction action) {
                super.onClickAction(swipeAction, selected, action);
                int position = selected.getAdapterPosition();
                List<CartBuyDataBean.ProductItem> datas = mCarBuyInfo.getItems();
                CartBuyDataBean.ProductItem productItem = datas.get(position);
                mPresenter.deleteItemBuyGoods(productItem.product.getUuid(),productItem.sku.getUuid(),"delete");
                mOrderAdapter.remove(selected.getAdapterPosition());
            }
        });

        swipeAction.attachToRecyclerView(rlv_carts);
        rlv_carts.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        mOrderAdapter = new CartOrderAdapter(getContext(),mCartProducts);
        rlv_carts.setAdapter(mOrderAdapter);

        //解决滑动焦点冲突
        rlv_carts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                srl_cart.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        mOrderAdapter.setListener((count, skuuid, itemUuid, isFree) -> {
            mCountSelectDialog.referModifyData(count,skuuid,itemUuid,isFree);
            mCountSelectDialog.show();
        });

        cb_applyCash.setOnCheckedChangeListener((compoundButton, isClicked) -> {
            if(isClicked && mCarBuyInfo != null){
                mPresenter.balanceAmtByCash(mCarBuyInfo.getUserAmtCash());
            }else{
                mPresenter.balanceAmtByCash(0.0);
            }
        });

        srl_cart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mCurrentPage = 6;
                isLoadMore = false;
                srl_cart.finishRefresh(Constant.DEFAULT_SMARTLOADING_DELAY_TIMEOUT);
                onLoadData();
            }
        });

        srl_cart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                srl_cart.finishLoadMore(Constant.DEFAULT_SMARTLOADING_DELAY_TIMEOUT);
                if (isLoadMore) {
                    mCurrentPage++;
                    mPresenter.fetchCLikeList(mCurrentPage, mPageSize);
                }
            }
        });
        onLoadData();
    }

    private void initData() {
        SPManager.sPutInt(Constant.SP_SAVE_CART_GOODS_NUM, Constant.DIGIT_ZERO);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshDataEvent event){
        if(event.getmMsg().equals(Constant.EVENT_PAY_SUCCESS)){
            mPresenter.fetchCLikeList(mCurrentPage,mPageSize);
        }
    }

    private View getNoNetView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_no_net,null);
        QMUIRoundButton qrb_refreshData = view.findViewById(R.id.qrb_refreshNet);
        qrb_refreshData.setOnClickListener(view1 -> {
            if(UserUtil.getInstance().isLogin()){

            }
        });
        return view;
    }

    @OnClick({R.id.qmui_emptyCart,R.id.ll_cIncreaseG,R.id.tv_goRefundWeb,R.id.qrb_applyPromo,R.id.qmui_checkOut,R.id.ll_skipAManager,R.id.tv_choiceCouponCode,R.id.ll_freeOneWrapper})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.qmui_emptyCart:
                mHomeControlListener.startIntent();
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_CART_CONTINUE_SHOPPING);
                break;
            case R.id.ll_cIncreaseG:
                HolderActivity.startFragment(getContext(),AddMoreGoodsFragment.class);
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_CART_ADD_MORE);
                break;
            case R.id.tv_goRefundWeb:
                Bundle bundle = new Bundle();
                String skipTitle = "Return Policy";
                String siteUrl = "";
                if(LocaleUtil.getInstance().getLanguage().equals("en")){
                    siteUrl = UrlConfig.RETURN_POLICY_EN;
                }else{
                    siteUrl = UrlConfig.RETURN_POLICY;
                }
                bundle.putString("type","1");
                bundle.putString("webUrl", siteUrl);
                bundle.putString("title",skipTitle);
                HolderActivity.startFragment(getContext(),WebExplorerFragment.class,bundle);
                break;
            case R.id.qrb_applyPromo:
                mUserCouponCode = 1;
                postPromoCode();
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_CART_COUPON_APPLY);
                break;
            case R.id.qmui_checkOut:
                if(mAddressManager != null) {
                    if (!DataUtil.idNotNull(mAddressManager.getResults())) {
                        Bundle goShipping = new Bundle();
                        goShipping.putInt("type", ShippingAddressFragment.INDEX_ACTION_ADD_ADDRESS_USER);
                        HolderActivity.startFragment(getContext(),ShippingAddressFragment.class,goShipping);
                    } else {
                        if (DataUtil.idNotNull(mCarBuyInfo.getItems())) {
                            mPresenter.requestOrderPay(getProductKeyValue());
                        }
                    }
                }
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_CART_CHECKOUT);
                break;
            case R.id.ll_skipAManager:
                Bundle skipAm = new Bundle();
                skipAm.putString("type","0");
                HolderActivity.startFragment(getContext(),AddressManagerFragment.class,skipAm);
                break;
            case R.id.tv_choiceCouponCode:
                if(mCouponDialog != null){
                    mCouponDialog.reloadingCouponList();
                }
                break;
            case R.id.ll_freeOneWrapper:
                HolderActivity.startFragment(getContext(),DeliverDoubleFragment.class);
                break;
        }
    }

    private void postPromoCode() {
        String hint_couponEmpty = getResources().getString(R.string.cart_hint_coupon_empty);
        String promoCode = et_inputPromo.getText().toString().trim();
        if(TextUtils.isEmpty(promoCode)){
            ToastUtil.showToast(hint_couponEmpty);
            return;
        }
        mPresenter.applyCouponCode(promoCode);
    }

    public CartItemReqBean getProductKeyValue(){
        CartItemReqBean reqBean = new CartItemReqBean();
        if(mCarBuyInfo != null){
            List<CartBuyDataBean.ProductItem> productItems = mCarBuyInfo.getItems();
            List<CartItemReqBean.Items> cartItem = new ArrayList<>();

            for(int i=0;i<productItems.size();i++){
                CartBuyDataBean.ProductItem item = productItems.get(i);
                CartItemReqBean.Items productItem = new CartItemReqBean.Items();
                productItem.setQuantity(item.getQuantity());
                productItem.setSkuUuid(item.getSku().getUuid());
                productItem.setProductUuid(item.getProduct().getUuid());
                cartItem.add(productItem);
            }
            if(!TextUtils.isEmpty(mAddressUuid)){
                reqBean.setUuid(mAddressUuid);
            }
            reqBean.setShippingAddressUuid(mUserAddressId);
            reqBean.setUserCouponUuid(mUserCouponId);
            reqBean.setItems(cartItem);
            if(cb_applyCash.isChecked()){
                reqBean.setAmtCash(mCarBuyInfo.getUserAmtCash());
            }else{
                reqBean.setAmtCash(0.0);
            }
            reqBean.setPlatform(Constant.APP_SOURCE_TYPE);
            reqBean.setType("0");
            reqBean.setXplatform("ANDROID");
        }
        mRequestOrderFrom = reqBean;
        return reqBean;
    }

    /**
     * app退出时刷新数据
     */
    public void syncExitApp(){
        boolean isLogin = UserUtil.getInstance().isLogin();
        if(!isLogin){
            ll_cartPartLike.setVisibility(VISIBLE);
            ll_cartInfo.setVisibility(GONE);
            ll_cartCheckout.setVisibility(GONE);
            mPresenter.fetchCLikeList(mCurrentPage,mPageSize);
        }
    }

    public void onRefreshView() {
        onLoadData();
    }

    /**
     * 支付成功后刷新购物车数据
     */
    public void onRefreshAfterPay(){
        ll_cartPartLike.setVisibility(VISIBLE);
        ll_cartInfo.setVisibility(GONE);
        ll_cartCheckout.setVisibility(GONE);
        mPresenter.fetchCLikeList(mCurrentPage,mPageSize);
    }

    //刷新地址信息
    public void onRefreshAddress(){
        boolean isLogin = UserUtil.getInstance().isLogin();
        if(isLogin){
            mPresenter.fetchAddressList();
        }
    }

    @Override
    public void onRefresh() {
        isPrepared      = false;
        mCurrentPage    = 6;
        mUserCouponCode = 0;
        cb_applyCash.setChecked(false);
        onLoadData();
    }

    private void onLoadData(){
        boolean isLogin = UserUtil.getInstance().isLogin();
        if(!isLogin){
            if(!isPrepared){
                mPresenter.fetchCLikeList(mCurrentPage,mPageSize);
            }
        }else{
            mPresenter.fetchCartData();
            mPresenter.fetchAddressList();
//            mPresenter.fetchCouponList();
        }
    }

    private void inflateDataToView() {
        SPManager.sPutInt(Constant.SP_SAVE_CART_GOODS_NUM,mCarBuyInfo.getSum());
        EventBus.getDefault().post(new RefreshDataEvent(Constant.EVNET_CHANGE_CURRENT_CART_NUM));
        if(DataUtil.idNotNull(mCarBuyInfo.getItems())){
            isPrepared = false;
            VIEW_MODE = 1;
            srl_cart.setEnableLoadMore(false);
            switchTwoViewState();
            String sysLanguate = LocaleUtil.getInstance().getLanguage();
            if(sysLanguate.equals("ar")){
                tv_rightArrow.setText("←");
            }else{
                tv_rightArrow.setText("→");
            }

            tv_buyOriginCP.setText(MathUtil.Companion.formatPrice(mCarBuyInfo.getOriginalAmtProduct()));
            tv_totalCp.setText(MathUtil.Companion.formatPrice(mCarBuyInfo.getAmt()));
            mOrderAdapter.setData(mCarBuyInfo.getItems());
            modifyFreeShipping();

            if(mCarBuyInfo.getShowCanGetOneFree()){           //显示买一送一
                ll_freeOneWrapper.setVisibility(VISIBLE);
            }else{
                ll_freeOneWrapper.setVisibility(GONE);
            }

            if(mCarBuyInfo.getAmtCash() >0){
                rl_freeCash.setVisibility(VISIBLE);
                tv_cashAmount.setText("-" + MathUtil.Companion.formatPrice(mCarBuyInfo.getAmtCash()));
                cb_applyCash.setChecked(mCarBuyInfo.getAmtCash() > 0);
            }else{
                rl_freeCash.setVisibility(GONE);
            }

            if(mCarBuyInfo.getDeduction()>0){
                rl_deductionPrice.setVisibility(VISIBLE);
                tv_deductionPrice.setText("-" + MathUtil.Companion.formatPrice(mCarBuyInfo.getDeduction()));
            }else{
                rl_deductionPrice.setVisibility(GONE);
            }
            if(mCarBuyInfo.getAmtProductCoupon() >0){
                if(!TextUtils.isEmpty(mCarBuyInfo.getPromoCode())){
                    tv_useCouponCode.setText(mCarBuyInfo.getPromoCode());
                }
                rl_amtCoupon.setVisibility(VISIBLE);
                tv_buyAmtCouponP.setText("-" + MathUtil.Companion.formatPrice(mCarBuyInfo.getAmtProductCoupon()));
                tv_invalidCouponCode.setVisibility(GONE);
            }else{
                rl_amtCoupon.setVisibility(GONE);
                if(mUserCouponCode == 1){
                    tv_invalidCouponCode.setVisibility(VISIBLE);
                }
            }
            if(mCarBuyInfo.getShowCashBalance() >0){
                rl_scBalance.setVisibility(VISIBLE);
                tv_carCashMoney.setText(MathUtil.Companion.formatPrice(mCarBuyInfo.getShowCashBalance()));
            }else{
                rl_scBalance.setVisibility(GONE);
            }
            if(mCarBuyInfo.getDeductMore() >0){
                tv_deductMore.setText(" " + MathUtil.Companion.formatPrice(mCarBuyInfo.getDeductMore()) + " ");
                ll_cIncreaseG.setVisibility(VISIBLE);     //显示差价,如果大于0显示 小于0不显示
            }else{
                ll_cIncreaseG.setVisibility(GONE);
            }
        }else{
            VIEW_MODE = 0;
            switchTwoViewState();
            if(!isPrepared){
                mPresenter.fetchCLikeList(mCurrentPage,mPageSize);
            }
        }
    }

    /**
     * 加载成功后隐藏
     */
    private void loadFinish(){
        if(emptyView.getVisibility() == VISIBLE){
            emptyView.setVisibility(GONE);
        }
        srl_cart.setVisibility(VISIBLE);
    }

    /**
     * 切换俩种视图的状态0为商品列表,1为购物车信息
     */
    private void switchTwoViewState(){
        if(VIEW_MODE == -1){
            return;
        }
        if(VIEW_MODE == 0){
            ll_cartPartLike.setVisibility(VISIBLE);    //显示猜你喜欢列表
            ll_cartInfo.setVisibility(GONE);
            ll_cartCheckout.setVisibility(GONE);
        }else if(VIEW_MODE == 1){
            ll_cartInfo.setVisibility(VISIBLE);        //显示购物车信息列表
            ll_cartCheckout.setVisibility(VISIBLE);
            ll_cartPartLike.setVisibility(GONE);
        }
    }

    @Override
    public void fetchMightLikeData(ClassifyListBean result) {
        VIEW_MODE = 0;
        loadFinish();
        switchTwoViewState();
        if(result != null){
            isPrepared = true;
            if(mCurrentPage == 6){
                if(DataUtil.idNotNull(result.getResults())){
                    tv_cart_mightLike_header.setVisibility(VISIBLE);
                    mAdapter.setNewInstance(result.getResults());
                }
            }else{
                mAdapter.addData(result.getResults());
            }
            isLoadMore = result.isHasMorePages();
            srl_cart.finishLoadMore();
            if(!isLoadMore){
                ll_endNoData.setVisibility(View.VISIBLE);
                srl_cart.setEnableLoadMore(false);
            }else{
                ll_endNoData.setVisibility(View.GONE);
                srl_cart.setEnableLoadMore(true);
            }
        }
    }

    @Override
    public void fetchAddressSuccess(AddressBean addressList) {
        if(addressList != null){
            loadFinish();
            mAddressManager = addressList;
            String addressName;
            boolean hasDefault = false;

            if(DataUtil.idNotNull(addressList.getResults())){
                ll_skipAManager.setVisibility(VISIBLE);
                for(AddressBean.AddressItem item:addressList.getResults()){
                    if(item.isDefault()){
                        hasDefault = true;
                        mAddressUuid   = item.getUuid();
                        mUserAddressId = item.getUuid();
                        mDefaultPhoneNum = item.getPhone();
                        if(TextUtils.isEmpty(item.getAddressLine1())){
                            addressName = item.getAddressLine2();
                        }else{
                            addressName = item.getAddressLine1();
                        }
                        if(addressName.contains("#")){
                            tv_retriveAddress.setText(StringUtils.filterValidAddress(addressName));
                        }else{
                            tv_retriveAddress.setText(addressName);
                        }
                        break;
                    }
                }
                if(!hasDefault){
                    AddressBean.AddressItem defaultItem = addressList.getResults().get(0);
                    mAddressUuid   = defaultItem.getUuid();
                    mUserAddressId = defaultItem.getUuid();
                    mDefaultPhoneNum = defaultItem.getPhone();
                    if(TextUtils.isEmpty(defaultItem.getAddressLine1())){
                        addressName = defaultItem.getAddressLine2();
                    }else{
                        addressName = defaultItem.getAddressLine1();
                    }
                    if(addressName.contains("#")){
                        tv_retriveAddress.setText(StringUtils.filterValidAddress(addressName));
                    }else{
                        tv_retriveAddress.setText(addressName);
                    }
                }
            }else{
                ll_skipAManager.setVisibility(GONE);
            }
        }else{
            ll_skipAManager.setVisibility(GONE);
        }
    }

    /**
     * 生成订单成功
     * @param result
     */
    @Override
    public void fetchCreateOrderSuccess(PayOrderBean result) {
       if(result == null){
           return;
       }
       boolean isCoupon = result.getAmtPayedOfCoupon();
       if(isCoupon){                                                                   //支付成功后跳转到支付页面
            Bundle bundle = new Bundle();
            bundle.putString("orderId",result.getUuid());
            HolderActivity.startFragment(getContext(),PayStatusFragment.class,bundle);
       }else{
           String countryRegion = LocaleUtil.getInstance().getRegion();
           Bundle payBunlde       = new Bundle();
           payBunlde.putString("region",countryRegion);
           payBunlde.putString("phoneNum",mDefaultPhoneNum);
           payBunlde.putString("orderPayResult", JsonUtils.serialize(result));
           payBunlde.putString("orderForm", JsonUtils.serialize(mRequestOrderFrom));
           HolderActivity.startFragment(getContext(),PaymentFragment.class,payBunlde);
       }
    }

    //获取支付的h5
    @Override
    public void fetchWebPayH5(ChionWrapperBean result) {
        if(result != null){
            String approvalURL = result.getApprovalURL();
            if(!TextUtils.isEmpty(approvalURL)){
                Bundle bundle = new Bundle();
                bundle.putString("webUrl",approvalURL);
                bundle.putString("type","0");
                bundle.putString("payUuid",result.getOrderUuid());
                HolderActivity.startFragment(getContext(),WebExplorerFragment.class,bundle);
            }
        }
    }

    @Override
    public void fetchBuyDataFail(String msg) {
        mPresenter.fetchCLikeList(mCurrentPage,mPageSize);
    }

    @Override
    public void fetchBuyDataSuccess(CartBuyDataBean result) {
        loadFinish();
        if(result != null){
            this.mCarBuyInfo = result;
            inflateDataToView();
        }
    }

    @Override
    public void fetchApplyCouponSuccess(CartBuyDataBean result) {
        if(result != null){
            this.mCarBuyInfo = result;
            inflateDataToView();
        }
    }

    @Override
    public void modifyBuyCountSuccess(CartBuyDataBean result) {
        if(result != null){
            this.mCarBuyInfo = result;
            inflateDataToView();
        }
    }

    @Override
    public void deleteSuccess(CartBuyDataBean result) {
        mTipDialogUtil.showTipSuccess();
        if(result != null){
            this.mCarBuyInfo = result;
            inflateDataToView();
        }
    }

    @Override
    public void modifyBuyCountFail(String result){

    }

    @Override
    public void fetchCouponListSuccess(CartCouponsBaean result) {

    }

    @Override
    public void onOkBuyClick(int buyCount,boolean isIncr,String skuUuid, String itemUuid) {
        mPresenter.modifyBuyCount(buyCount,isIncr,skuUuid,itemUuid);
    }

    @Override
    public void fetchSuccess(CategoryBean result) {

    }

    @Override
    public void fetchFail(String failReason) {
        ToastUtil.showToast(failReason);
        loadFinish();
    }

    private void modifyFreeShipping(){
        if(mCarBuyInfo.getAmtShipping() >0){
            tv_buyShipCp.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
            tv_buyShipCp.setText(MathUtil.Companion.formatPrice(mCarBuyInfo.getAmtShipping()));
            tv_freeShipHint.setText("");
        }else{
            if(mCarBuyInfo.getOriginalAmtShipping() >0){
                tv_freeShipHint.setText(mFreeShipping);
                tv_buyShipCp.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_buyShipCp.setText(MathUtil.Companion.formatPrice(mCarBuyInfo.getOriginalAmtShipping()));
            }else{
                tv_buyShipCp.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
                tv_buyShipCp.setText("");
                tv_freeShipHint.setText(mFreeShipping);
            }
        }
    }

    @Override
    public void doCashSuccess(String result) {

    }

    @Override
    public void deleteFail(String result) {
        mTipDialogUtil.showTopFail();
    }

    @Override
    public void startLoading(){
            showProgressDialog();
    }

    @Override
    public void stopLoading() {
        hideProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {

    }
}
