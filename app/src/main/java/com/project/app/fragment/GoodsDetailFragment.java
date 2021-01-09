package com.project.app.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.ShareUtils;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.project.app.R;
import com.project.app.activity.LoginActivity;
import com.project.app.adapter.DetailCoverAdapter;
import com.project.app.adapter.GoodsRelativeAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.GoodsDetailInfoBean;
import com.project.app.bean.GoodsRelationBean;
import com.project.app.config.AppsFlyConfig;
import com.project.app.contract.GoodsDetailContract;
import com.project.app.presenter.GoodsDetailPresenter;
import com.project.app.ui.dialog.GDBuyPopupDialogUtil;
import com.project.app.ui.dialog.GDChooiceDialogUtil;
import com.project.app.ui.dialog.GDGalleryDialogUtil;
import com.project.app.ui.dialog.GDetailDeliveryUtil;
import com.project.app.ui.dialog.GDetailDescriptionUtil;
import com.project.app.ui.dialog.RefundDialogUtil;
import com.project.app.utils.AppsFlyEventUtils;
import com.project.app.utils.MathUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.OnClick;

public class GoodsDetailFragment extends BaseMvpQmuiFragment<GoodsDetailPresenter> implements GoodsDetailContract.View {
    @BindView(R.id.iv_shoppingCart)
    ImageView iv_shoppingCart;
    @BindView(R.id.qmui_buy)
    QMUIRoundButton qmui_buy;
    @BindView(R.id.tv_gd_orignPrice)
    TextView tv_gd_orignPrice;
    @BindView(R.id.tv_gd_couponPrice)
    TextView tv_gd_couponPrice;
    @BindView(R.id.rlv_someGoods)
    RecyclerView rlv_someGoods;
    @BindView(R.id.iv_switchFavorite)
    ImageView iv_switchFavorite;
    @BindView(R.id.vp_goodsCover)
    ViewPager2 vp_goodsCover;
    @BindView(R.id.iv_noneCover)
    ImageView iv_noneCover;
    @BindView(R.id.tv_tagPading)
    TextView tv_tagPading;
    @BindView(R.id.ll_refund)
    LinearLayout ll_refund;
    @BindView(R.id.ll_descrite)
    LinearLayout ll_descrite;
    @BindView(R.id.tv_dGoodsName)
    TextView tv_dGoodsName;
    @BindView(R.id.ll_choiceCAndS)
    LinearLayout ll_choiceCAndS;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;
    @BindView(R.id.v_hasCart)
    View v_hasCart;
    @BindView(R.id.tv_alBuyCount)
    TextView tv_alBuyCount;

    private String mUuid         = "";
    private int mMaxPage         = 0;
    private int mIncreaseNum     = 0;    //购买后的增量
    private int mBmIndex         = 0;    //商品视图选中的图片序列
    private boolean isLoved      = false;
    private boolean isCanChoice  = false;
    private ArrayList<String> mCoverList;
    private ArrayList<GoodsRelationBean> mGoodsRList;
    private GoodsDetailPresenter mPresenter;
    private DetailCoverAdapter mCoverAdapter;
//    private GoodsDetailCoverAdapter mCoverAdapter;
    private GoodsRelativeAdapter mRelativeApdater;
    private GoodsDetailInfoBean mGoodsInfoBase;
    private RefundDialogUtil mRefundDialogUtil;
    private GDetailDescriptionUtil mGdetailDecUtil;
    private GDetailDeliveryUtil mGdtailDeliveryUtil;
    private GDChooiceDialogUtil mGdChooiceDialogUtil;
    private GDBuyPopupDialogUtil mGdBuyPopDialogUtil;
    private GDGalleryDialogUtil mGdGalleryDialogUtil;

    public static GoodsDetailFragment newInstance(Bundle bundle) {
        GoodsDetailFragment fragment = new GoodsDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_goods_detail;
    }

    @Override
    public void initView() {
        mPresenter = new GoodsDetailPresenter();
        mPresenter.attachView(this);
        mUuid = getArguments().getString("uuid");
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        initWidget();
    }

    private void displayBuyGoodsNum(){
        String buyNum = "";
        int cartSkuNum = SPManager.sGetInt(Constant.SP_SAVE_CART_GOODS_NUM);
        cartSkuNum = cartSkuNum + mIncreaseNum;
        if(cartSkuNum > 0){
            v_hasCart.setVisibility(View.VISIBLE);
            tv_alBuyCount.setVisibility(View.VISIBLE);
            if (cartSkuNum >99){
                buyNum = cartSkuNum + "+";
            }else{
                buyNum = String.valueOf(cartSkuNum);
            }
            tv_alBuyCount.setText(String.valueOf(buyNum));
        }else{
            v_hasCart.setVisibility(View.GONE);
            tv_alBuyCount.setVisibility(View.GONE);
        }
    }

    private void initWidget() {
        mCoverList       = new ArrayList<>();
        mGoodsRList      = new ArrayList<>();
        mRelativeApdater = new GoodsRelativeAdapter(mGoodsRList);
        tv_gd_orignPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        rlv_someGoods.setLayoutManager(manager);
        rlv_someGoods.setHasFixedSize(true);
        rlv_someGoods.setAdapter(mRelativeApdater);
        mGdGalleryDialogUtil = new GDGalleryDialogUtil(getContext(),true,true);

        mGdChooiceDialogUtil = new GDChooiceDialogUtil(getContext(), true, true, (count, isIncrease, skuUuid) -> {
            if(checkLogin()){
                mIncreaseNum = count;
                mPresenter.operationAddGoods(count,isIncrease,skuUuid);
            }
        });

        mRefundDialogUtil   = new RefundDialogUtil(getContext(), true, true, () -> {
            if(isCanChoice){
                mGdChooiceDialogUtil.show();
            }else{
                defaultChoiceFirst();
            }
        });
        mGdetailDecUtil     = new GDetailDescriptionUtil(getContext(),true,true, () -> {
            if(isCanChoice){
                mGdChooiceDialogUtil.show();
            }else{
                defaultChoiceFirst();
            }
        });
        mGdtailDeliveryUtil = new GDetailDeliveryUtil(getContext(),true,true, () -> {
            if(isCanChoice){
                mGdChooiceDialogUtil.show();
            }else{
                defaultChoiceFirst();
            }
        });
        mGdBuyPopDialogUtil = new GDBuyPopupDialogUtil(getContext(),true,true);

        displayBuyGoodsNum();
    }

    private void initViewPager2(){
        mCoverAdapter    = new DetailCoverAdapter(getContext(),mCoverList);
        vp_goodsCover.setAdapter(mCoverAdapter);
        vp_goodsCover.setOffscreenPageLimit(mCoverList.size());

        mCoverAdapter.setmListener(new DetailCoverAdapter.IBrowseListener() {
            @Override
            public void photoClick(int position) {
                mGdGalleryDialogUtil.show(position);
            }
        });

        vp_goodsCover.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mBmIndex = position;
                tv_tagPading.setText((position+1) +"/" + mMaxPage);
            }
        });
    }

    @OnClick({R.id.iv_shoppingCart,R.id.iv_goodsDshare,R.id.iv_switchFavorite,R.id.ll_refund,R.id.ll_descrite,R.id.ll_gdDelivery,R.id.qmui_buy,R.id.ll_choiceCAndS,R.id.iv_back})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_shoppingCart:
                if(checkLogin()){
                    clearHolderStack();
                    EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_BANNER_SKIP_CART));
                }
                break;
            case R.id.iv_goodsDshare:
                String shareUrl = UrlConfig.APP_PREFIX_SHARE_URL + mUuid;
                String shareTitle = getContext().getResources().getString(R.string.goods_detail_share_tilte);
                ShareUtils.shareText(getContext(),shareTitle,shareUrl);
                break;
            case R.id.iv_switchFavorite:
                if(checkLogin()){
                    isLoved = !isLoved;
                    iv_switchFavorite.setSelected(isLoved);
                    if(mGoodsInfoBase == null){
                        return;
                    }
                    if(isLoved){
                        mPresenter.operationFavoriteAdd(mGoodsInfoBase.getUuid());
                    }else{
                        mPresenter.operationFavoriteCancel(mGoodsInfoBase.getUuid());
                    }
                }
                break;
            case R.id.ll_refund:
                if(mGoodsInfoBase != null){
                    mRefundDialogUtil.show();
                }
                break;
            case R.id.ll_descrite:
                if(mGoodsInfoBase != null){
                    mGdetailDecUtil.show(mGoodsInfoBase.getDescription());
                }
                break;
            case R.id.ll_gdDelivery:
                if(mGoodsInfoBase != null){
                    String shippingArrivalDes = mGoodsInfoBase.getShippingArrivalDesc();
                    String shippingMinDay = String.valueOf(mGoodsInfoBase.getShippingMinDays());
                    String shippingMaxDay = String.valueOf(mGoodsInfoBase.getShippingMaxDays());

                    String shippingPrice   = mGoodsInfoBase.getPriceRetail()+"";
                    if(TextUtils.isEmpty(shippingArrivalDes) || TextUtils.isEmpty(shippingPrice)){
                        return;
                    }
                    mGdtailDeliveryUtil.show(shippingArrivalDes,shippingPrice,shippingMinDay,shippingMaxDay);
                }
                break;
            case R.id.qmui_buy:
            case R.id.ll_choiceCAndS:
                if(isCanChoice){
                    mGdChooiceDialogUtil.show();
                }else{
                    if(checkLogin()){
                        defaultChoiceFirst();
                    }
                }
                break;
            case R.id.iv_back:
                popBackStack();
                break;
        }
    }

    private void defaultChoiceFirst(){
        String sk_uuid;
        qmui_buy.setEnabled(false); //不让其点击
        String sorryNotFind = getContext().getResources().getString(R.string.goods_detail_sorry_hint);
        if(DataUtil.idNotNull(mGoodsInfoBase.getSkus().getSkus())){
            List<GoodsDetailInfoBean.SkusItem> skusList =  mGoodsInfoBase.getSkus().getSkus();
            if(skusList.size() >= 1){
                sk_uuid = skusList.get(0).getUuid();
                mIncreaseNum = 1;
                mPresenter.operationAddGoods(1,true,sk_uuid);
            }else{
                ToastUtil.showToast(sorryNotFind);
            }
        }
    }

    private boolean checkLogin(){
        UserUtil userUtil = UserUtil.getInstance();
        if(!userUtil.isLogin()) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            getContext().startActivity(intent);
        }
        return userUtil.isLogin();
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchCoverList(mUuid);
        mPresenter.fetchRelativeBox(mUuid);
    }

    @Override
    public void fetchCoverSuccess(GoodsDetailInfoBean result) {
        if(result != null){
            mGoodsInfoBase = result;
            if(mGoodsInfoBase == null || result.getSkus() == null){
                return;
            }
            if(DataUtil.idNotNull(result.getSkus().getSkus())){
                isCanChoice = result.getSkus().getSkus().size() > 1;   //小于一个直接加入购物车
            }else{
                isCanChoice = false;
            }
            tv_dGoodsName.setText(mGoodsInfoBase.getName());
            if(mGoodsInfoBase.getPriceOrigin() == mGoodsInfoBase.getPriceRetail()){
                tv_gd_orignPrice.setVisibility(View.GONE);
            }
            tv_gd_orignPrice.setText(MathUtil.Companion.formatPrice(mGoodsInfoBase.getPriceOrigin()));
            tv_gd_couponPrice.setText(MathUtil.Companion.formatPrice(mGoodsInfoBase.getPriceRetail()));
            mGdChooiceDialogUtil.syncData(mGoodsInfoBase.getSkus(),mGoodsInfoBase.getMainPhoto());

            if(!TextUtils.isEmpty(mGoodsInfoBase.getMainPhoto())){
                mGdBuyPopDialogUtil.lazzyImageUrl(mGoodsInfoBase.getMainPhoto());
            }
            if(mGoodsInfoBase.getPhotos() != null){
                if(mGoodsInfoBase.getPhotos().length <=0){
                    iv_noneCover.setVisibility(View.VISIBLE);
                    vp_goodsCover.setVisibility(View.GONE);
                    tv_tagPading.setVisibility(View.GONE);
                }else{
                    for(int i=0;i<mGoodsInfoBase.getPhotos().length;i++){
                        if (i >8){
                            break;
                        }
                        mCoverList.add(mGoodsInfoBase.getPhotos()[i]);
                    }
                    iv_noneCover.setVisibility(View.GONE);
                    mGdGalleryDialogUtil.buildData(mCoverList);
                    mMaxPage = mCoverList.size();
                    initViewPager2();
                    tv_tagPading.setVisibility(View.VISIBLE);
                    tv_tagPading.setText(1+"/" + mMaxPage);
                    isLoved = result.isUserIsCollection();
                    iv_switchFavorite.setSelected(isLoved);
                }
            }else{
                iv_noneCover.setVisibility(View.VISIBLE);
                vp_goodsCover.setVisibility(View.GONE);
                tv_tagPading.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void fetchCoverRSuccess(List<GoodsRelationBean> result) {
        if(DataUtil.idNotNull(result)){
            mRelativeApdater.setNewInstance(result);
        }
    }

    @Override
    public void fetchSuccess(String msg) {

    }

    @Override
    public void fetchFile(String msg) {
        qmui_buy.setEnabled(true);
        ToastUtil.showToast(msg);
    }

    @Override
    public void addCartSuccess(String msg) {
        qmui_buy.setEnabled(true);
        displayBuyGoodsNum();   //购买成功后刷新商品的数量
        mGdBuyPopDialogUtil.show();
        Map<String,Object> addCartEventMap = new HashMap<>();
        addCartEventMap.put(AppsFlyConfig.AF_EVENT_ADD_TO_CART,SPManager.sGetString(Constant.SP_DEVICE_MODEL));
        AppsFlyEventUtils.sendAppInnerEvent(addCartEventMap, AppsFlyConfig.AF_EVENT_ADD_TO_CART);
        EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_REFRESH_CAR));
    }

    @Override
    public void refreshWishFavorite() {
        EventBus.getDefault().post(new RefreshDataEvent(Constant.REFRESH_WISHLIST_FAVORITE));
    }

    @Override
    public void refreshRemoveWishFavorite() {
        EventBus.getDefault().post(new RefreshDataEvent(Constant.REFRESH_WISHLIST_FAVORITE));
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
    public void onDestroy() {
        super.onDestroy();
        Glide.get(getContext()).clearMemory();
        mGdBuyPopDialogUtil.clear();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }

}
