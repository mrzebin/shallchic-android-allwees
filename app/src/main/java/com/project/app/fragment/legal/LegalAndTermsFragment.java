package com.project.app.fragment.legal;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.hb.basemodel.config.api.UrlConfig;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.adapter.LegalAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.fragment.WebExplorerFragment;
import com.project.app.utils.LocaleUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class LegalAndTermsFragment extends BaseMvpQmuiFragment {
    @BindView(R.id.rlv_legal)
    RecyclerView rlv_legal;

    private LegalAdapter mAdater;
    private final List<String> mChoiceTitle = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_legal_terms;
    }

    @Override
    public void initView() {
        initWidget();
    }

    private void initWidget() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        String[] choiceTitles = getContext().getResources().getStringArray(R.array.lt);
        mChoiceTitle.addAll(Arrays.asList(choiceTitles));
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rlv_legal.setLayoutManager(manager);
        mAdater = new LegalAdapter(mChoiceTitle);
        rlv_legal.setAdapter(mAdater);

        mAdater.setOnItemClickListener((adapter, view, position) -> {
            Bundle bundle = new Bundle();
            String skipUrl = "";
            String skipTitle = "";
            bundle.putString("type","1");
            skipTitle = mChoiceTitle.get(position);
            if(position ==0){
                if(LocaleUtil.getInstance().getLanguage().equals("en")){
                    skipUrl = UrlConfig.TERMS_OF_USE_EN;
                }else{
                    skipUrl = UrlConfig.TERMS_OF_USE;
                }
            }else if(position == 1){
                if(LocaleUtil.getInstance().getLanguage().equals("en")){
                    skipUrl = UrlConfig.PRIVACY_POLICY_EN;
                }else{
                    skipUrl = UrlConfig.PRIVACY_POLICY;
                }
            }else if(position == 2){
                if(LocaleUtil.getInstance().getLanguage().equals("en")){
                    skipUrl = UrlConfig.SHIPPING_POLICY_EN;
                }else{
                    skipUrl = UrlConfig.SHIPPING_POLICY;
                }
            }else if(position == 3){
                if(LocaleUtil.getInstance().getLanguage().equals("en")){
                    skipUrl = UrlConfig.RETURN_POLICY_EN;
                }else{
                    skipUrl = UrlConfig.RETURN_POLICY;
                }
            }else if(position == 4){
                if(LocaleUtil.getInstance().getLanguage().equals("en")){
                    skipUrl = UrlConfig.PAYMENT_POLICY_EN;
                }else{
                    skipUrl = UrlConfig.PAYMENT_POLICY;
                }
            }
            bundle.putString("webUrl", skipUrl);
            bundle.putString("title",skipTitle);
            HolderActivity.startFragment(getContext(),WebExplorerFragment.class,bundle);
        });
    }

    @OnClick({R.id.iv_back})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
        }
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }

}
