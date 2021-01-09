package com.project.app.fragment.address;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.project.app.R;
import com.project.app.adapter.ChoiceCountryAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.CountryCropBean;
import com.project.app.contract.CCountryContract;
import com.project.app.presenter.CCountryPresenter;
import com.project.app.ui.widget.SimpleDividerItemDecoration;
import com.project.app.utils.LocaleUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class SelectCountryFragment extends BaseMvpQmuiFragment<CCountryPresenter> implements CCountryContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.srl_country)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.rlv_country)
    RecyclerView rlv_country;
    @BindView(R.id.iv_tb_back)
    ImageView iv_tb_back;
    @BindView(R.id.tv_sureChoiceC)
    TextView tv_sureChoiceC;
    private int mChoiceIndex;
    private CountryCropBean mCountryBean;
    private CountryCropBean.CountryItem mSelectCountry;

    private List<CountryCropBean.CountryItem> mData = new ArrayList<>();
    private ChoiceCountryAdapter mAdapter;

    public static SelectCountryFragment newInstance() {
        return new SelectCountryFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select_country;
    }

    @Override
    public void initView() {
        initWidget();
    }

    private void initWidget() {
        mPresenter = new CCountryPresenter();
        mPresenter.attachView(this);
        mSwipeRefresh.setOnRefreshListener(this);
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        mSwipeRefresh.setColorSchemeResources(R.color.allwees_theme_color,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);
        rlv_country.setLayoutManager(new LinearLayoutManager(getContext()));
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.line_divider);
        rlv_country.addItemDecoration(new SimpleDividerItemDecoration(getContext(),drawable,1));
    }

    @OnClick({R.id.iv_tb_back,R.id.tv_sureChoiceC})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_tb_back:
                popBackStack();
                break;
            case R.id.tv_sureChoiceC:
                if(mSelectCountry != null){
                    refreshLocale();
                    RefreshDataEvent event = new RefreshDataEvent(Constant.EVENT_INTENT_COUNTRY_SELECT_INFO);
                    event.setData(JsonUtils.serialize(mSelectCountry));
                    EventBus.getDefault().post(event);
                    popBackStack();
                }
                break;
        }
    }

    /**
     * 自定义
     */
    private void refreshLocale() {
        LocaleUtil.getInstance().setLocaleCustom(true);
        LocaleUtil.getInstance().setLocalCustomRegion(mSelectCountry.getRegion());
        LocaleUtil.getInstance().setLocalCustomSymbol(mSelectCountry.getRegion());
        LocaleUtil.getInstance().setLocalCustomNameEn(mSelectCountry.getNameEn());
        LocaleUtil.getInstance().setLocaleCustomCountryFlagCloumn(mSelectCountry.getColNum());
        LocaleUtil.getInstance().setLocaleCustomCountryFlagRow(mSelectCountry.getRowNum());
        LocaleUtil.getInstance().setCustomPhoneAreaCode(mSelectCountry.getPhoneAreaCode());
    }

    @Override
    public void onRefresh() {
        mSwipeRefresh.postDelayed(() -> {
            if(mSwipeRefresh != null){
                mSwipeRefresh.setRefreshing(false);
            }
        }, Constant.DELAY_LOADING_TIME_OUT);
        mPresenter.fetchCountryRList();
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchCountryRList();
    }

    @Override
    public void fetchSuccess(CountryCropBean result) {
        if(result != null){
            mCountryBean = result;
            mData = result.getCountries();
            if(DataUtil.idNotNull(result.getCountries())){
                mAdapter = new ChoiceCountryAdapter(getContext(),mData,rlv_country);
                rlv_country.setAdapter(mAdapter);
                mAdapter.setListener((position, item) -> {
                    mSelectCountry = item;
                    mChoiceIndex = position;
                    tv_sureChoiceC.setClickable(true);
                    tv_sureChoiceC.setTextColor(getResources().getColor(R.color.allwees_theme_color));
                    for(int i=0;i<mData.size();i++){
                        mData.get(i).setSelect(false);
                    }
                    mData.get(position).setSelect(true);
                    mAdapter.notifyDataSetChanged();
                });
            }
        }else{
            mAdapter.setEmptyView(getEmptyView());
        }
    }

    public View getEmptyView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_view,null);
        QMUIRoundButton refreData = view.findViewById(R.id.qrb_refreshOrder);
        refreData.setOnClickListener(view1 -> mPresenter.fetchCountryRList());
        return view;
    }

    @Override
    public void fetchFail(String failReason) {
        ToastUtil.showToast(failReason);
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
}
