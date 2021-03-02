package com.project.app.fragment.goods;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hb.basemodel.utils.LoggerUtil;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.project.app.R;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.ChartSizeBean;
import com.project.app.contract.GoodsSizeChartContract;
import com.project.app.presenter.GoodsSizeChartPresenter;
import com.project.app.ui.SizeChartView;
import com.project.app.utils.LocaleUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商品尺寸
 */
public class GoodsSizeChartFragment extends BaseMvpQmuiFragment<GoodsSizeChartPresenter> implements GoodsSizeChartContract.View {
    @BindView(R.id.emptyView)
    QMUIEmptyView emptyView;
    @BindView(R.id.srf_sizeChart)
    SmartRefreshLayout srf_sizeChart;
    @BindView(R.id.tv_toggle_cm)
    TextView tv_toggle_cm;
    @BindView(R.id.tv_toggle_inch)
    TextView tv_toggle_inch;
    @BindView(R.id.sizeChart)
    SizeChartView ll_table;
    @BindView(R.id.iv_healthyWear)
    ImageView iv_healthyWear;

    private String mProductUuid  = "";
    private String mCategoryNo   = "";
    private String mChartTabJson = "";
    private int mDefaultType     = 0;
    private int mLanguateType    = 0;  //0代表阿拉伯 1代表英文

    private Context mContext;
    private List<TextView> mToggleMeasures = new ArrayList<>();
    private List<ChartSizeBean.ChartSizeItem> mData = new ArrayList<>();

    public static com.project.app.fragment.goods.GoodsSizeChartFragment newInstance(Bundle bundle) {
        com.project.app.fragment.goods.GoodsSizeChartFragment fragment = new com.project.app.fragment.goods.GoodsSizeChartFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_goods_sizechart;
    }

    @Override
    public void initView() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        initBundle();
        initWidget();
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        mProductUuid = bundle.getString("productUuid");
        mCategoryNo  = bundle.getString("categoryNo");
        LoggerUtil.i("mProductUuid:" + mProductUuid + "--mCategoryNo:" + mCategoryNo);
    }

    private void initWidget() {
        mContext = getContext();
        mPresenter = new GoodsSizeChartPresenter();
        mPresenter.attachView(this);
        mToggleMeasures.add(tv_toggle_cm);
        mToggleMeasures.add(tv_toggle_inch);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        displaySizeChartByMeasureType();
        initLanguate();
    }

    private void initLanguate() {
        String languate = LocaleUtil.getInstance().getLanguage();
        if(languate.equals("ar")){      //语言是阿拉伯文
            mLanguateType = 0;
        }else if(languate.equals("en")){ //语言是英文
            mLanguateType = 1;
        }
    }

    private void displaySizeChartByMeasureType() {
       for(TextView item:mToggleMeasures){
           item.setSelected(false);
       }
       mToggleMeasures.get(mDefaultType).setSelected(true);
    }


    /**
     * 提取数据
     */
    private void parseValidData(String result) {
        try {
            mChartTabJson = result;

            LinkedHashMap<String,List<String>> mChartMap = new LinkedHashMap<>(); //过滤尺寸的数据,已id为key
            List<String> matchSize = new ArrayList<>();
            List<String> validData = new ArrayList<>();

            JSONObject obj = new JSONObject(result);
            JSONObject entryObj = obj.getJSONObject("entry");
            JSONArray sizesObj  = obj.getJSONArray("details");
            JSONObject entirysByUnit = entryObj.getJSONObject("entriesByUnit");  //获取尺寸的对象
            String displayWearUrl    = obj.getString("image");   //获取穿戴的图片

            if(!TextUtils.isEmpty(displayWearUrl)){
                Glide.with(getContext()).load(displayWearUrl).into(iv_healthyWear);
            }

            if(entryObj != null && sizesObj.length() >0){
                JSONObject titleObj = entryObj.getJSONObject("title");
                String keyTitle = titleObj.getString("id");
                if(!TextUtils.isEmpty(keyTitle)){
                    if(mLanguateType == 0){
                        matchSize.add(titleObj.getString("nameAr"));
                    }else if(mLanguateType == 1){
                        matchSize.add(titleObj.getString("nameEn"));
                    }

                    validData = cycleFetchSizeData(keyTitle,sizesObj);
                    matchSize.addAll(validData);
                    mChartMap.put(keyTitle,matchSize);
                }
            }

            if(entirysByUnit != null){
                JSONArray target = null;
                if(mDefaultType == 0){   //cm
                    target = entirysByUnit.getJSONArray("cm");
                }else{                   //Inch
                    target = entirysByUnit.getJSONArray("in");
                }
                if(target != null){
                    if(target.length() >0){
                        for(int i=0;i<target.length();i++){
                            List<String> entryByUnit = new ArrayList<>();
                            List<String> tempSize = new ArrayList<>();
                            if(mLanguateType == 0){
                                entryByUnit.add(target.getJSONObject(i).getString("nameAr"));
                            }else if(mLanguateType == 1){
                                entryByUnit.add(target.getJSONObject(i).getString("nameEn"));
                            }
                            String unitId = target.getJSONObject(i).getString("id");
                            tempSize = cycleFetchSizeData(unitId,sizesObj);
                            entryByUnit.addAll(tempSize);
                            mChartMap.put(unitId,entryByUnit);
                        }
                    }
                }
            }
            ll_table.canvasTableByDataSize(mChartMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<String> cycleFetchSizeData(String id,JSONArray parentSize){
        List<String> chartItem = new ArrayList<>();
        try {
            for(int i=0;i<parentSize.length();i++){
                JSONObject object = parentSize.getJSONObject(i);
                String sizeById = object.getString(id);
                chartItem.add(sizeById);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return chartItem;
    }

    @OnClick({R.id.iv_back,R.id.tv_toggle_cm,R.id.tv_toggle_inch})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
            case R.id.tv_toggle_cm:
                mDefaultType = 0;
                displaySizeChartByMeasureType();
                if(!TextUtils.isEmpty(mChartTabJson)){
                    parseValidData(mChartTabJson);
                }
                break;
            case R.id.tv_toggle_inch:
                mDefaultType = 1;
                displaySizeChartByMeasureType();
                if(!TextUtils.isEmpty(mChartTabJson)){
                    parseValidData(mChartTabJson);
                }
                break;
        }
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchGoodsSizeChart(mCategoryNo,mProductUuid);
    }

    @Override
    public void fetchGoodsSizeChartSuccess(String result) {
        if(TextUtils.isEmpty(result)){
            return;
        }
        parseValidData(result);
    }

    @Override
    public void fetchGoodsSizeChartFail(String msg) {
        ToastUtil.showToast(msg);
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

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
