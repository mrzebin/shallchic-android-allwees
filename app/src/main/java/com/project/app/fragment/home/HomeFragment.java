package com.project.app.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.other.UserUtil;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.activity.LoginActivity;
import com.project.app.activity.MainActivity;
import com.project.app.base.BaseController;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.AppUpdateBean;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.bean.CategoryBean;
import com.project.app.bean.ContactAppBean;
import com.project.app.bean.CountryCropBean;
import com.project.app.bean.MarketGiftInfoBean;
import com.project.app.bean.ParseFacebookDeeplinkBean;
import com.project.app.config.AppsFlyConfig;
import com.project.app.contract.HomeContract;
import com.project.app.fragment.GoodsDetailFragment;
import com.project.app.fragment.WebExplorerFragment;
import com.project.app.fragment.address.AddressManagerFragment;
import com.project.app.fragment.earn.RewardFragment;
import com.project.app.fragment.earn.SchallCashFragment;
import com.project.app.fragment.home.classify.CategoryClassifyFragment;
import com.project.app.fragment.home.classify.DeliverDoubleFragment;
import com.project.app.fragment.order.OrderDetailFragment;
import com.project.app.presenter.HomePresenter;
import com.project.app.ui.NoScrollViewPager;
import com.project.app.ui.dialog.RunoobCouponUtil;
import com.project.app.ui.dialog.UpdateAppUtil;
import com.project.app.update.DownloadApk;
import com.project.app.utils.AppsFlyEventUtils;
import com.project.app.utils.CheckVersionUtil;
import com.project.app.utils.HomeTitlesUtils;
import com.project.app.utils.LocaleUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseMvpQmuiFragment<HomePresenter> implements HomeContract.View {
    @BindView(R.id.pager)
    NoScrollViewPager mViewPager;
    @BindView(R.id.iv_tab_main_home)
    ImageView iv_tab_main_home;
    @BindView(R.id.iv_tab_main_category)
    ImageView iv_tab_main_category;
    @BindView(R.id.iv_tab_main_cart)
    ImageView iv_tab_main_cart;
    @BindView(R.id.iv_tab_main_wishlist)
    ImageView iv_tab_main_wishlist;
    @BindView(R.id.iv_tab_main_me)
    ImageView iv_tab_main_me;
    @BindView(R.id.tv_tabCartCount)
    TextView tv_tabCartCount;
    @BindView(R.id.tab_main_home)
    LinearLayout tab_main_home;
    @BindView(R.id.tab_main_category)
    LinearLayout tab_main_category;
    @BindView(R.id.tab_main_cart)
    ConstraintLayout tab_main_cart;
    @BindView(R.id.tab_main_wishlist)
    LinearLayout tab_main_wishlist;
    @BindView(R.id.tab_main_me)
    LinearLayout tab_main_me;

    private HashMap<Pager, QMUIWindowInsetLayout> mPager;
    private HomeController     mHomeController;
    private CategoryController mCategoryController;
    private FavoriteController mGrilController;
    private CartController     mCarController;
    private MeController       mMeController;
    private ArrayList<ImageView> mTabList;
    private int mCurrentIndex = 0;
    private RunoobCouponUtil mRunoobCouponUtil;
    private UpdateAppUtil mUpdateAppUtil;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
        initTabs();
        initPagers();
        initWidget();
        mPresenter = new HomePresenter();
        mPresenter.attachView(this);
        EventBus.getDefault().register(this);
    }

    private void initWidget() {
        initDailog();
        ParseFacebookDeeplinkBean deeplinkBean = ((MainActivity)getActivity()).mDeeplinkBean;
        if(deeplinkBean == null){
            return;
        }
        String forwardUri = deeplinkBean.getExtras().getSc_deeplink();
        if(!TextUtils.isEmpty(forwardUri)){
            initDeeplinkStatus(forwardUri);
        }
    }

    private void initDailog() {
        mRunoobCouponUtil = new RunoobCouponUtil(getContext(),true,false);
        mUpdateAppUtil    = new UpdateAppUtil(getContext(),true,false);

        mRunoobCouponUtil.setListener(new RunoobCouponUtil.RunoobObserveCancelListener() {
            @Override
            public void dialogCancel() {
                if(!SPManager.sGetBoolean(Constant.SP_DIALOG_DAILY_CHECK_UPDATE)){
                    mPresenter.checkAppersion();
                }
            }
        });

        mUpdateAppUtil.setListener(new UpdateAppUtil.CallbackOpreateListener() {
            @Override
            public void selectUpdateApp(String downUrl,String content) {
                DownloadApk.downloadApk(getContext(),downUrl,content, Constant.APPLICATION_ID);
            }
        });
    }

    private void checkMiuiPush(boolean isBind) {
        String pushId = SPManager.sGetString(Constant.SP_BIND_MIUI_PUSH_ID);
        if(!TextUtils.isEmpty(pushId)){
            mPresenter.bindMiuiPushId(pushId,isBind);
        }
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchCountryList();
        mPresenter.fetchContactInfo();
        mPresenter.fetchHomePopularTitles();
        checkMiuiPush(true);
        if(!UserUtil.getInstance().isLogin()){    //未登录弹
            mRunoobCouponUtil.show();
        }else {
            mPresenter.fetchFreeGiftStatus();    //如果登录则调取接口获取记录
        }
    }

    @OnClick({R.id.tab_main_home,R.id.tab_main_category,R.id.tab_main_cart,R.id.tab_main_wishlist,R.id.tab_main_me})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.tab_main_home:
                mCurrentIndex = 0;
                break;
            case R.id.tab_main_category:
                mCurrentIndex = 1;
                break;
            case R.id.tab_main_cart:
                mCurrentIndex = 2;
                break;
            case R.id.tab_main_wishlist:
                mCurrentIndex = 3;
                break;
            case R.id.tab_main_me:
                mCurrentIndex = 4;
                break;
        }
        resetTabs(mCurrentIndex);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(RefreshDataEvent event) {
        switch (event.getmMsg()) {
            case Constant.EVENT_LOGIN_SUCCESS:     //登录成功后的操作
                mPresenter.fetchFreeGiftStatus();  //登录成功后获取是否获取礼物接口
                if(mHomeController != null){
                    mHomeController.onRefreshView();
                }
                if(mGrilController != null){
                    mGrilController.onRefreshView();
                }
                if (mCarController != null) {
                    mCarController.onRefreshView();
                }
                if (mMeController != null) {
                    mMeController.onRefreshView();
                }
                checkMiuiPush(true);    //退出app时候调用
                break;
            case Constant.EVENT_MODIFY_USERINFO_SUCCESS:  
                if(mMeController != null){
                    mMeController.onRefreshView();
                }
                break;
            case Constant.EVENT_REFRESH_CAR:
                if (mCarController != null) {
                    mCarController.onRefreshView();
                }
                break;
            case Constant.EVENT_GO_HOME_CART:
                if (mCarController != null) {
                    mCurrentIndex = 2;
                    resetTabs(mCurrentIndex);
                }
                break;
            case Constant.EVENT_ADD_ADDRESS_SURE:
                if (mCarController != null) {
                    mCarController.onRefreshAddress();
                }
                break;
            case Constant.EVENT_EXIT_APP_SURE:       //退出app时清理数据
                if(mHomeController != null){
                    mHomeController.onRefreshView();
                }
                if (mCarController != null) {
                    mCarController.syncExitApp();
                }
                if (mGrilController != null){
                    mGrilController.syncExitApp();
                }
                if (mMeController != null) {
                    mMeController.syncExitApp();
                }
                SPManager.sPutBoolean(Constant.SP_HOME_POPULAR_FLOAT_GIFT,false);
                refreshTabCartCount();
                checkMiuiPush(false);
                break;
            case Constant.REFRESH_WISHLIST_FAVORITE:
                if (mGrilController != null) {
                    mGrilController.syncFavorite();
                }
                break;
            case Constant.REFRESH_AFTER_PAY_CARDATA:
                if (mCarController != null) {
                    mCarController.onRefreshAfterPay();
                }
                break;
            case Constant.EVENT_BANNER_SKIP_CATEGORY:
                if (mCategoryController != null) {
                    mCurrentIndex = 1;
                    resetTabs(mCurrentIndex);
                }
                break;
            case Constant.EVENT_BANNER_SKIP_CART:
                if (mCategoryController != null) {
                    mCurrentIndex = 2;
                    resetTabs(mCurrentIndex);
                }
                break;
            case Constant.EVENT_BANNER_SKIP_WISH:
                if (mGrilController != null) {
                    mCurrentIndex = 3;
                    resetTabs(mCurrentIndex);
                }
                break;
            case Constant.EVENT_BANNER_SKIP_MINE:
                if (mMeController != null) {
                    mCurrentIndex = 4;
                    resetTabs(mCurrentIndex);
                }
                break;
            case Constant.EVNET_CHANGE_CURRENT_CART_NUM:
                refreshTabCartCount();
                break;
            case Constant.EVENT_INTENT_COUNTRY_SELECT_INFO:
                if(mMeController != null){
                    mMeController.fetchLocaleLanguage();
                }
                break;
            case Constant.EVENT_DEEPLINK_HOME:
                if(mHomeController != null){
                    mCurrentIndex = 0;
                    resetTabs(mCurrentIndex);
                }
                break;
            case Constant.EVENT_INNER_APP_DEEPLINK:    //内部app通过deeplink跳转
                String linkData = event.getData();
                if(!TextUtils.isEmpty(linkData)){
                    initDeeplinkStatus(linkData);
                }
                break;
            case Constant.EVENT_PUSH_APP_DEEPLINK:
                String pushDeepLink = event.getData();
                if(!TextUtils.isEmpty(pushDeepLink)){
                    initDeeplinkStatus(pushDeepLink);
                }
                break;
        }
    }

    /**
     * 刷新购物车数量
     */
    private void refreshTabCartCount() {
        int count = SPManager.sGetInt(Constant.SP_SAVE_CART_GOODS_NUM);
        if(count <= 0){
            tv_tabCartCount.setVisibility(View.GONE);
        }else{
            tv_tabCartCount.setVisibility(View.VISIBLE);
            if(count<=99){
                if(mCurrentIndex == 2){
                    tv_tabCartCount.setTextColor(getResources().getColor(R.color.theme_color));
                }else{
                    tv_tabCartCount.setTextColor(getResources().getColor(R.color.color_999));
                }
                tv_tabCartCount.setText(String.valueOf(count));
            }else{
                tv_tabCartCount.setText("99+");
            }
        }
    }

    private final PagerAdapter mPagerAdapter = new PagerAdapter() {
        private int mChildCount = 0;

        @Override
        public int getCount() {
            return mPager.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            QMUIWindowInsetLayout page = mPager.get(Pager.getPagerByPosition(position));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(page,params);
            return page;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            if(mChildCount == 0){
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }
    };

    private void initPagers() {
        BaseController.HomeControlListener mSpriteListener = new BaseController.HomeControlListener() {
            @Override
            public void startIntent() {                  //控制home category cart wishlist me contract
                if(mHomeController != null){
                    mCurrentIndex = 0;
                    resetTabs(mCurrentIndex);
                }
            }

            @Override
            public void startFragment(QMUIFragment fragment) {
                HomeFragment.this.startFragment(fragment);
            }

            @Override
            public void startFragment(@NotNull Class<? extends QMUIFragment> fragment) {
                try {
                    QMUIFragment desFragment = fragment.newInstance();
                    HomeFragment.this.startFragment(desFragment);
                } catch (IllegalAccessException | InstantiationException | java.lang.InstantiationException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void startFragment(@NotNull Class<? extends QMUIFragment> fragment, Bundle argsBundle) {
                try {
                    QMUIFragment desFragment = fragment.newInstance();
                    desFragment.setArguments(argsBundle);
                    HomeFragment.this.startFragment(desFragment);
                } catch (IllegalAccessException | InstantiationException | java.lang.InstantiationException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void showProgressDialog() {
                startProgressDialog(getContext());
            }

            @Override
            public void hideProgressDialog() {
                stopProgressDialog();
            }
        };

        mPager = new HashMap<>();
        mHomeController = new HomeController(getContext(),getChildFragmentManager());
        mHomeController.setHomeControlListener(mSpriteListener);
        mPager.put(Pager.HOME,mHomeController);

        mCategoryController = new CategoryController(getContext());
        mCategoryController.setHomeControlListener(mSpriteListener);
        mPager.put(Pager.CATEGORY,mCategoryController);

        mCarController = new CartController(getContext());
        mCarController.setHomeControlListener(mSpriteListener);
        mPager.put(Pager.CAR,mCarController);

        mGrilController = new FavoriteController(getContext());
        mGrilController.setHomeControlListener(mSpriteListener);
        mPager.put(Pager.GIRL,mGrilController);

        mMeController   = new MeController(getContext());
        mMeController.setHomeControlListener(mSpriteListener);
        mPager.put(Pager.ME,mMeController);
        mViewPager.setNoScroll(true);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(mTabList.size());
        mViewPager.setCurrentItem(mCurrentIndex);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    QMUIStatusBarHelper.setStatusBarDarkMode(getActivity());
                    mHomeController.onRefreshView();
                } else if (position == 1) {
                    QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
                    mCategoryController.onRefreshView();
                    AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_CATEGORY_HOME);
                } else if (position == 2) {
                    QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
                    mCarController.onRefreshView();
                } else if (position == 3) {
                    QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
                    mGrilController.onRefreshView();
                } else if (position == 4) {
                    QMUIStatusBarHelper.setStatusBarDarkMode(getActivity());
                    mMeController.onRefreshView();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        refreshTabCartCount();
    }

    private void initDeeplinkStatus(String forwardUri) {
        if(!TextUtils.isEmpty(forwardUri)){
            if(forwardUri.equalsIgnoreCase(Constant.SCHEME_HOST_DEEPLINK_HOME)){
                if (mHomeController != null) {
                    mCurrentIndex = 0;
                    resetTabs(mCurrentIndex);
                }
            }else if(forwardUri.equalsIgnoreCase(Constant.SCHEME_HOST_DEEPLINK_CATEGORY)){
                if (mCategoryController != null) {
                    mCurrentIndex = 1;
                    resetTabs(mCurrentIndex);
                }
            }else if(forwardUri.equalsIgnoreCase(Constant.SCHEME_HOST_DEEPLINK_CART)){
                if (mCarController != null) {
                    mCurrentIndex = 2;
                    resetTabs(mCurrentIndex);
                }
            }else if(forwardUri.equalsIgnoreCase(Constant.SCHEME_HOST_DEEPLINK_WISHLIST)){
                if (mGrilController != null) {
                    mCurrentIndex = 3;
                    resetTabs(mCurrentIndex);
                }
            }else if(forwardUri.equalsIgnoreCase(Constant.SCHEME_HOST_DEEPLINK_MINE)){
                if (mMeController != null) {
                    mCurrentIndex = 4;
                    resetTabs(mCurrentIndex);
                }
            }else if(forwardUri.equalsIgnoreCase(Constant.SCHEME_HOST_DEEPLINK_LOGIN)){
                Intent goLogin = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(goLogin);
            }else if(forwardUri.equalsIgnoreCase(Constant.SCHEME_HOST_DEEPLINK_ADDRESS)){
                if(UserUtil.getInstance().isLogin()){
                    Bundle bundle = new Bundle();
                    bundle.putString("type","1");
                    HolderActivity.startFragment(getContext(),AddressManagerFragment.class,bundle);
                }
            }else if(forwardUri.equalsIgnoreCase(Constant.SCHEME_HOST_DEEPLINK_CASH)){
                if(UserUtil.getInstance().isLogin()){
                    HolderActivity.startFragment(getContext(),SchallCashFragment.class);
                }
            }else if(forwardUri.equalsIgnoreCase(Constant.SCHEME_HOST_DEEPLINK_REWARDS)){
                if(UserUtil.getInstance().isLogin()){
                    HolderActivity.startFragment(getContext(),RewardFragment.class);
                }
            }else if(forwardUri.startsWith(Constant.SCHEME_HOST_DEEPLINK_ORDERHISTORY)){
                if(UserUtil.getInstance().isLogin()){
                    HolderActivity.startFragment(getContext(),OrderFragment.class);
                }
            }else if(forwardUri.equalsIgnoreCase(Constant.SCHEME_HOST_DEEPLINK_ORDERHISTORYDETAIL)){
                 if(UserUtil.getInstance().isLogin()){
                     String orderId = subEqualSymbol(forwardUri);
                     Bundle bundle = new Bundle();
                     bundle.putString("orderId",orderId);
                     HolderActivity.startFragment(getContext(),OrderDetailFragment.class,bundle);
                }
            }else if(forwardUri.startsWith(Constant.SCHEME_HOST_DEEPLINK_GOODSDETAIL)){    //商品详情
                //解析字符串
                String uuid = subEqualSymbol(forwardUri);
                if(uuid != null && !TextUtils.isEmpty(uuid)){
                    Bundle bundle = new Bundle();
                    bundle.putString("uuid", uuid);
                    bundle.putString("type","1");
                    HolderActivity.startFragment(getContext(),GoodsDetailFragment.class,bundle);
                }
            }else if(forwardUri.startsWith(Constant.SCHEME_HOST_DEEPLINK_WEBVIEW)){
                String skipUrl = subEqualSymbol(forwardUri);    //提取url
                Bundle goP = new Bundle();
                goP.putString("type","4");
                goP.putString("webUrl",skipUrl);
                HolderActivity.startFragment(getContext(),WebExplorerFragment.class,goP);
            }else if(forwardUri.startsWith(Constant.SCHEME_HOST_DEEPLINK_CATEGORY_ITEM)){    //提取分类
                String title = "";
                String categoryNo = "";
                if(forwardUri.contains("&") && forwardUri.contains("#")){
                    int startPosition = forwardUri.indexOf("&");
                    int startEPosition = forwardUri.indexOf("=");
                    int endEPosition   = forwardUri.lastIndexOf("=");
                    title = forwardUri.substring(startEPosition+1,startPosition);
                    categoryNo = forwardUri.substring(endEPosition+1,forwardUri.length());
                    if(!title.equals("") && !categoryNo.equals("")){
                        Bundle bundle = new Bundle();
                        bundle.putString("type", categoryNo);
                        bundle.putString("name",title);
                        HolderActivity.startFragment(getContext(),CategoryClassifyFragment.class,bundle);
                    }
                }
            }else if(forwardUri.startsWith(Constant.SCHEME_HOST_DEEPLINK_MARKETACTIVITY)){          //跳转到活动专区
                HashMap<String,String> params = parseDeeplinkParams(forwardUri);
                Bundle bundle = new Bundle();
                bundle.putString("marketTitle",params.get("title"));
                bundle.putString("marketId",params.get("bundleId"));
                HolderActivity.startFragment(getContext(),DeliverDoubleFragment.class,bundle);
            }
        }
    }

    //截取等号后面的订单号
    private String subEqualSymbol(String target){
        String result = "";
        if(target.contains("=")){
            int endPosition = target.indexOf("=");
            result = target.substring(endPosition+1,target.length());
        }
        return result;
    }

    //解析带&符号拼接的字符串
    private HashMap parseDeeplinkParams(String target){
        HashMap<String,String> validParams = new HashMap<>();
        int positionLable = 0;
        int positionEqualLable = 0;

        if(target.contains("&")){
            positionLable = target.indexOf("&");
            validParams.put("title",target.substring(positionLable+1,target.length()));
        }
        if(target.contains("=")){
            positionEqualLable = target.indexOf("=");
            if(target.contains("&")){
                validParams.put("bundleId",target.substring(positionEqualLable+1,positionLable));
            }
        }
        return validParams;
    }

    private void initTabs() {
        mTabList = new ArrayList<>();
        mTabList.add(iv_tab_main_home);
        mTabList.add(iv_tab_main_category);
        mTabList.add(iv_tab_main_cart);
        mTabList.add(iv_tab_main_wishlist);
        mTabList.add(iv_tab_main_me);
        resetTabs(mCurrentIndex);
    }

    private void resetTabs(int index) {
        for (ImageView item : mTabList) {
            item.setSelected(false);
        }
        mTabList.get(index).setSelected(true);
        mViewPager.setCurrentItem(index, false);
        refreshTabCartCount();
    }

    /**
     * 更新apk的dialog
     * @param bean
     */
    public void updateDialog(AppUpdateBean bean){
        if(bean.getForceUpdate()){
            if(!TextUtils.isEmpty(bean.getDownUrl())){
                DownloadApk.downloadApk(getContext(),bean.getDownUrl(),bean.getContent(), Constant.APPLICATION_ID);
            }
        }else{
            mUpdateAppUtil.show(bean);
            SPManager.sPutBoolean(Constant.SP_DIALOG_DAILY_CHECK_UPDATE,true);   //今天已经显示过了
        }
    }

    enum Pager{
        HOME,CATEGORY,CAR,GIRL,ME;

        public static Pager getPagerByPosition(int position){
            switch (position){
                case 0:
                    return HOME;
                case 1:
                    return CATEGORY;
                case 2:
                    return CAR;
                case 3:
                    return GIRL;
                case 4:
                    return ME;
                default:
                    return HOME;
            }
        }
    }

    /*获取购物车的数据*/
    @Override
    public void fetchUserCartData(CartBuyDataBean result) {
        SPManager.sPutInt(Constant.SP_SAVE_CART_GOODS_NUM,result.getSum());
        refreshTabCartCount();
    }

    @Override
    public void fetchSuccess(CategoryBean result) {

    }

    @Override
    public void fetchFail(String failReason) {

    }

    @Override
    public void fetchAppVersionSuccess(AppUpdateBean result) {
        if(!TextUtils.isEmpty(result.getLatestVersion())){
            if(CheckVersionUtil.isNeedUpdate(getContext(),result.getLatestVersion())){
                if(!TextUtils.isEmpty(result.getDownUrl())){
                    if(!mRunoobCouponUtil.mDialog.isShowing()){    //新手任务弹窗不显示的时候弹更新弹窗
                        if(!SPManager.sGetBoolean(Constant.SP_DIALOG_DAILY_CHECK_UPDATE)){ //判断今天有没有弹更新
                            updateDialog(result);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void fetchCountrysSuccess(CountryCropBean result) {
        infilterPhoneAreaCode(result);
    }

    @Override
    public void fetchFreeGiftsStatusSuccess(MarketGiftInfoBean result) {
        if(!result.getApplied() && !result.getCompleted()){
            checkRunoobRules();
        }else{
            SPManager.sPutBoolean(Constant.SP_HOME_POPULAR_FLOAT_GIFT,true);
            if(!SPManager.sGetBoolean(Constant.SP_DIALOG_DAILY_CHECK_UPDATE)){
                mPresenter.checkAppersion();
            }
        }
    }

    @Override
    public void bindMiuiPushIdSuccess() {   //小米推送绑定成功

    }

    @Override
    public void fetchHomePopularTitlesSuccess(CategoryBean bean) {
        if(bean != null){
            HomeTitlesUtils instance =  HomeTitlesUtils.getInstance();
            instance.setmCategoryBean(bean);
        }
    }

    @Override
    public void fetchContackInfoSuccess(String result) {
        if(!TextUtils.isEmpty(result)){
            List<ContactAppBean> contactList = JsonUtils.deserializeList(result,ContactAppBean[].class);
            if(DataUtil.idNotNull(contactList)){
                for(ContactAppBean item:contactList){
                    if(item.getName().equals(Constant.CONTACT_SPECIFY_EMAIL_FLAG)){
                        SPManager.sPutString(Constant.SP_CONTACT_EMAIL_NUMBER,item.getAcctNum());
                    }else if(item.getName().equals(Constant.CONTACT_SPECIFY_WHATSAPP_FLAG)){
                        SPManager.sPutString(Constant.SP_CONTACT_WHATS_NUMBER,item.getAcctNum());
                    }else if(item.getName().equals(Constant.CONTACT_SPECIFY_INSTAGRAM_FLAG)){
                        SPManager.sPutString(Constant.SP_CONTACT_INSTAGRAM_NUMBER,item.getAcctNum());
                    }else if(item.getName().equals(Constant.CONTACT_SPECIFY_YOUTUBE_FLAG)){
                        SPManager.sPutString(Constant.SP_CONTACT_YOUTUBE_NUMBER,item.getAcctNum());
                    }else if(item.getName().equals(Constant.CONTACT_SPECIFY_FACEBOOK_FLAG)){
                        SPManager.sPutString(Constant.SP_CONTACT_FACEBOOK_NUMBER,item.getAcctNum());
                    }else if(item.getName().equals(Constant.CONTACT_SPECIFY_TWITTER_FLAG)){
                        SPManager.sPutString(Constant.SP_CONTACT_TWITTER_NUMBER,item.getAcctNum());
                    }
                }
            }
        }
    }

    private void checkRunoobRules(){    //每天只弹一次
        boolean isToday =  SPManager.sGetBoolean(Constant.SP_DIALOG_TODAY_RUNOOB_GIFT);
        if(!isToday){
            mRunoobCouponUtil.show();
            SPManager.sPutBoolean(Constant.SP_DIALOG_TODAY_RUNOOB_GIFT,true);
        }else{
            SPManager.sPutBoolean(Constant.SP_HOME_POPULAR_FLOAT_GIFT,true);
            if(!SPManager.sGetBoolean(Constant.SP_DIALOG_DAILY_CHECK_UPDATE)){
                mPresenter.checkAppersion();
            }
        }
    }

    //获取国家区号
    private void infilterPhoneAreaCode(CountryCropBean bean){
        CountryCropBean.CountryItem defaultCountryItem = null;
        String regionCode = LocaleUtil.getInstance().getRegion();
        String phoneAreaCode = "";
        for(CountryCropBean.CountryItem item:bean.getCountries()){
            if(item.getRegion().equals(regionCode)){
                phoneAreaCode = item.getPhoneAreaCode();
                defaultCountryItem = item;
                break;
            }
        }

        if(!TextUtils.isEmpty(phoneAreaCode)){
            LocaleUtil.getInstance().setPhoneAreaCode(phoneAreaCode);
        }
        if(defaultCountryItem != null){
            LocaleUtil.getInstance().setDefaultCountry(defaultCountryItem);
            if(mMeController != null){
                mMeController.fetchLocaleLanguage();
            }
        }
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
