package com.project.app.fragment.home.classify;


import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.utils.SPManager;
import com.project.app.utils.StringUtils;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.project.app.R;
import com.project.app.adapter.BaseState2Adapter;
import com.project.app.base.BaseMvpQmuiFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.OnClick;

/**
 *  闪购
 */
public class FlashSaleFragment extends BaseMvpQmuiFragment{
    @BindView(R.id.tv_buyNum)
    TextView tv_buyNum;
    @BindView(R.id.ll_flashtoday)
    LinearLayout ll_flashtoday;
    @BindView(R.id.ll_flashTomorrow)
    LinearLayout ll_flashTomorrow;
    @BindView(R.id.tv_fsToday_time)
    TextView tv_fsToday_time;
    @BindView(R.id.tv_fsToday_status)
    TextView tv_fsToday_status;
    @BindView(R.id.tv_fsTomorrowTime)
    TextView tv_fsTomorrowTime;
    @BindView(R.id.tv_fsTomorrowStatus)
    TextView tv_fsTomorrowStatus;
    @BindView(R.id.vp_fsContent)
    ViewPager2 vp_fsContent;
    @BindView(R.id.iv_fsQuestMark)
    ImageView iv_fsQuestMark;
    @BindView(R.id.cl_comeMyCart)
    ConstraintLayout cl_comeMyCart;
    @BindView(R.id.v_hasCart)
    View v_hasCart;

    private int mCurrentIndex = 0;
    private List<QMUIFragment> mFragmentFs;
    private BaseState2Adapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_flash_sale;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        initCarData();
        initWidget();
    }

    private void initWidget() {
        mAdapter = new BaseState2Adapter(mFragmentFs,getActivity());
        vp_fsContent.setAdapter(mAdapter);
        vp_fsContent.setCurrentItem(mCurrentIndex);
        refreshMatchTitleFont();

        iv_fsQuestMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        vp_fsContent.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentIndex = position;
                refreshMatchTitleFont();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    /**
     * 刷新标题字体的颜色
     */
    private void refreshMatchTitleFont() {
        String tomorrowDate = StringUtils.getEnMDFormat(System.currentTimeMillis());

        if(mCurrentIndex == 0){
            tv_fsToday_time.setTextColor(getContext().getResources().getColor(R.color.white));
            tv_fsToday_status.setTextColor(getContext().getResources().getColor(R.color.white));
            tv_fsTomorrowTime.setTextColor(getContext().getResources().getColor(R.color.color_999));
            tv_fsTomorrowStatus.setTextColor(getContext().getResources().getColor(R.color.color_999));
        }else if(mCurrentIndex == 1){
            tv_fsToday_time.setTextColor(getContext().getResources().getColor(R.color.color_999));
            tv_fsToday_status.setTextColor(getContext().getResources().getColor(R.color.color_999));
            tv_fsTomorrowTime.setTextColor(getContext().getResources().getColor(R.color.white));
            tv_fsTomorrowStatus.setTextColor(getContext().getResources().getColor(R.color.white));
        }

        tv_fsTomorrowStatus.setText(tomorrowDate);
    }

    @Override
    protected void lazyFetchData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshDataEvent event){
        if (event.getmMsg().equals(Constant.EVNET_CHANGE_CURRENT_CART_NUM)){
            changeCartNum();
        }
    }

    @OnClick({R.id.iv_back,R.id.ll_flashtoday,R.id.ll_flashTomorrow,R.id.cl_comeMyCart})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
            case R.id.ll_flashtoday:
                if(mCurrentIndex == 0){
                    return;
                }
                mCurrentIndex = 0;
                refreshMatchTitleFont();
                vp_fsContent.setCurrentItem(mCurrentIndex,false);
                break;
            case R.id.ll_flashTomorrow:
                if(mCurrentIndex == 1){
                    return;
                }
                mCurrentIndex = 1;
                refreshMatchTitleFont();
                vp_fsContent.setCurrentItem(mCurrentIndex,false);
                break;
            case R.id.cl_comeMyCart:
                popBackStack();
                EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_BANNER_SKIP_CART));
                break;
        }
    }

    private void initCarData() {
        changeCartNum();
        mFragmentFs = new ArrayList<>();
        mFragmentFs.add(FlashSaleClassifyFragment.newInstance(0));
        mFragmentFs.add(FlashSaleClassifyFragment.newInstance(1));
    }

    private void changeCartNum() {
        int cartSkuNum = SPManager.sGetInt(Constant.SP_SAVE_CART_GOODS_NUM);
        String buyNum = "";
        if(cartSkuNum > 0){
            tv_buyNum.setVisibility(View.VISIBLE);
            v_hasCart.setVisibility(View.VISIBLE);
            if (cartSkuNum >99){
                buyNum = cartSkuNum + "+";
            }else{
                buyNum = String.valueOf(cartSkuNum);
            }
            tv_buyNum.setText(String.valueOf(buyNum));
        }else{
            tv_buyNum.setVisibility(View.GONE);
            v_hasCart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
