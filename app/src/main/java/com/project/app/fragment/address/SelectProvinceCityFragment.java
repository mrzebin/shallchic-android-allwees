package com.project.app.fragment.address;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.utils.DataUtil;
import com.hb.basemodel.utils.JsonUtils;
import com.project.app.R;
import com.project.app.adapter.ChoicePcAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.ProvinceBean;
import com.project.app.contract.ProvinceCityContract;
import com.project.app.presenter.ProvinceCityPresenter;
import com.project.app.ui.widget.SimpleDividerItemDecoration;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择省份和城市
 */
public class SelectProvinceCityFragment extends BaseMvpQmuiFragment<ProvinceCityPresenter> implements ProvinceCityContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.srl_pc)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.rlv_pc)
    RecyclerView rlv_pc;
    @BindView(R.id.iv_tb_back)
    ImageView iv_tb_back;
    @BindView(R.id.tv_sureChoiceC)
    TextView tv_sureChoiceC;
    @BindView(R.id.tv_title_pc)
    TextView tv_title_pc;
    @BindView(R.id.ll_editPcWrap)
    LinearLayout ll_editPcWrap;
    @BindView(R.id.et_editPc)
    EditText et_editPc;

    private int mChoiceIndex = -1;
    private ProvinceBean mSelectProvince;
    private String mRegion;
    private String mRegionCity;
    private int mRequestType = 0;
    private boolean isInputed = false;     //判断是否是手动输入
    private boolean isPriority = false;    //是否为默认的三剑客

    private List<ProvinceBean> mData = new ArrayList<>();
    private ChoicePcAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select_p_city;
    }

    @Override
    public void initView() {
        initWidget();
        initWidgetListener();
    }

    private void initWidgetListener() {
        et_editPc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputStr = editable.toString();
                if(inputStr.length() >0){
                    tv_sureChoiceC.setClickable(true);
                    tv_sureChoiceC.setTextColor(getResources().getColor(R.color.theme_color));
                }else{
                    tv_sureChoiceC.setClickable(false);
                    tv_sureChoiceC.setTextColor(getResources().getColor(R.color.color_999));
                }
            }
        });
    }

    private void initWidget() {
        Bundle bundle = getArguments();
        mRequestType = bundle.getInt("type");        //0为选择省份 1为城市
        mRegion  = bundle.getString("region");

        String title = "";
        if(mRequestType == 0){
            title = getContext().getResources().getString(R.string.pc_title);
        }else if(mRequestType == 1){
            title = getContext().getResources().getString(R.string.pc_city_title);
            if(!mRegion.equals("-1")){
                mRegionCity = bundle.getString("regionCity");
            }
        }
        if(mRegion.equals("-1")){
            isInputed = true;
            mSwipeRefresh.setVisibility(View.GONE);
            ll_editPcWrap.setVisibility(View.VISIBLE);
        }else{
            mSwipeRefresh.setVisibility(View.VISIBLE);
            ll_editPcWrap.setVisibility(View.GONE);
        }

        tv_title_pc.setText(title);
        if(mRegion.equals("53950000") || mRegion.equals("2610000") || mRegion.equals("48650000")){
            isPriority = true;
        }
        mPresenter = new ProvinceCityPresenter();
        mPresenter.attachView(this);
        mSwipeRefresh.setOnRefreshListener(this);
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        mSwipeRefresh.setColorSchemeResources(R.color.theme_color,android.R.color.holo_blue_dark,android.R.color.holo_orange_dark);
        rlv_pc.setLayoutManager(new LinearLayoutManager(getContext()));
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.line_divider);
        rlv_pc.addItemDecoration(new SimpleDividerItemDecoration(getContext(),drawable,1));
    }

    @OnClick({R.id.iv_tb_back,R.id.tv_sureChoiceC})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_tb_back:
                popBackStack();
                break;
            case R.id.tv_sureChoiceC:
                if (isInputed) {    //手动输入省份
                    String inputInfo = et_editPc.getText().toString();
                    if (!TextUtils.isEmpty(inputInfo)) {
                        RefreshDataEvent event = new RefreshDataEvent(Constant.EVENT_INPUT_PC);
                        event.setData(inputInfo);
                        EventBus.getDefault().post(event);
                        popBackStack();
                    }
                }else{
                    if (mChoiceIndex != -1) {
                        RefreshDataEvent event = new RefreshDataEvent(Constant.EVENT_SPECITY_PCITY);
                        event.setData(JsonUtils.serialize(mSelectProvince));
                        EventBus.getDefault().post(event);
                        popBackStack();
                    }
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefresh.postDelayed(() -> {
            if(mSwipeRefresh != null){
                mSwipeRefresh.setRefreshing(false);
            }
        }, Constant.DELAY_LOADING_TIME_OUT);
    }

    @Override
    protected void lazyFetchData() {
        if(mRequestType == 0 && !mRegion.equals("-1")){
            mPresenter.fetchProvinceList(mRegion);
        }else if(mRequestType == 1 && !mRegion.equals("-1") && !mRegionCity.equals("-1")){
            mPresenter.fetchCityList(mRegion,mRegionCity);
        }
    }

    /**
     * 成功获取省份的列表
     * @param result
     */
    @Override
    public void fetchProvinceListSuccess(List<ProvinceBean> result) {
        if(DataUtil.idNotNull(result)){
            mAdapter = new ChoicePcAdapter(result);
            rlv_pc.setAdapter(mAdapter);
            mData = result;
            mAdapter.setListener((position, item) -> {
                mSelectProvince = item;
                mChoiceIndex = position;
                tv_sureChoiceC.setClickable(true);
                tv_sureChoiceC.setTextColor(getResources().getColor(R.color.theme_color));
                for(int i=0;i<mData.size();i++){
                    mData.get(i).setSelect(false);
                }
                mData.get(position).setSelect(true);
                mAdapter.notifyDataSetChanged();
            });
        }else{
//            mAdapter.setEmptyView(getEmptyView());
            isInputed = true;
            mSwipeRefresh.setVisibility(View.GONE);
            ll_editPcWrap.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void fetchCityListSuccess(List<ProvinceBean> result) {
        if(DataUtil.idNotNull(result)){
            mAdapter = new ChoicePcAdapter(result);
            rlv_pc.setAdapter(mAdapter);
            mData = result;
            mAdapter.setListener((position, item) -> {
                mSelectProvince = item;
                mChoiceIndex = position;
                tv_sureChoiceC.setClickable(true);
                tv_sureChoiceC.setTextColor(getResources().getColor(R.color.theme_color));
                for(int i=0;i<mData.size();i++){
                    mData.get(i).setSelect(false);
                }
                mData.get(position).setSelect(true);
                mAdapter.notifyDataSetChanged();
            });
        }else{
//            mAdapter = new ChoicePcAdapter(result);
//            rlv_pc.setAdapter(mAdapter);
//            mAdapter.setEmptyView(getEmptyView());
            isInputed = true;
            mSwipeRefresh.setVisibility(View.GONE);
            ll_editPcWrap.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void fetchFail(String msg) {

    }

    public View getEmptyView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_no_cs_history,null);
        return view;
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
