package com.project.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.AppManager;
import com.hb.basemodel.utils.SPManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.project.app.R;
import com.project.app.adapter.LanguageAdapter;
import com.project.app.base.BaseActivity;
import com.project.app.bean.languageSettingBean;
import com.project.app.manager.LocaleSwitcherManager;
import com.project.app.utils.HomeTitlesUtils;
import com.project.app.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 切换语言
 */
public class LanguageActivity extends BaseActivity {
    @BindView(R.id.ll_inflate)
    LinearLayout ll_Inflate;
    @BindView(R.id.rlv_language)
    RecyclerView rlv_language;

    private Unbinder mUnbinder;
    private LanguageAdapter mAdapter;
    private List<languageSettingBean> mDatas;
    private int beforePosition = 0;
    private int afterPosition  = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        mUnbinder = ButterKnife.bind(this);
        StatusBarUtils.setStatusBarView(this,ll_Inflate);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        refreshLocaleEnvironment();
        initWidget();
        initData();
    }

    private void initWidget() {
        mDatas = new ArrayList<>();
        String[] languages = getResources().getStringArray(R.array.language_items);
        int defaultIndex =  SPManager.sGetInt(Constant.SP_DEFAULT_LANGUAGE_TYPE,0);
        for(int i=0;i<languages.length;i++){
            languageSettingBean bean = new languageSettingBean();
            bean.setChoice(i == defaultIndex);
            String languageName = languages[i];
            bean.setLanguageName(languageName);
            mDatas.add(bean);
        }
        LinearLayoutManager manager = new LinearLayoutManager(LanguageActivity.this);
        rlv_language.setLayoutManager(manager);
        mAdapter = new LanguageAdapter(mDatas);
        rlv_language.setAdapter(mAdapter);

        mAdapter.setListener(position -> {
            if(position == 0){
                Locale locale = new Locale("en");
                SPManager.sPutString(Constant.SP_LOCALE_COUNTRY,locale.getCountry());
                SPManager.sPutString(Constant.SP_LOCALE_LANGUAGE,locale.getLanguage());
                SPManager.sPutInt(Constant.SP_DEFAULT_LANGUAGE_TYPE,position);
                LocaleSwitcherManager.INSTANCE.configureBaseContext(LanguageActivity.this);
            }else if(position == 1){
                Locale locale = new Locale("ar");
                SPManager.sPutString(Constant.SP_LOCALE_COUNTRY,locale.getCountry());
                SPManager.sPutString(Constant.SP_LOCALE_LANGUAGE,locale.getLanguage());
                LocaleSwitcherManager.INSTANCE.configureBaseContext(LanguageActivity.this);
                SPManager.sPutInt(Constant.SP_DEFAULT_LANGUAGE_TYPE,position);
            }
            afterPosition = position;
            recreate();
            for(int i=0;i<mDatas.size();i++){
                mDatas.get(i).setChoice(false);
            }
            mDatas.get(position).setChoice(true);
            mAdapter.setNewInstance(mDatas);
        });
    }

    private void initData() {
        beforePosition = SPManager.sGetInt(Constant.SP_DEFAULT_LANGUAGE_TYPE);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("beforePosition",beforePosition);
        outState.putInt("afterPosition",afterPosition);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        beforePosition = savedInstanceState.getInt("beforePosition");
        afterPosition = savedInstanceState.getInt("afterPosition");
    }

    @OnClick({R.id.iv_back})
    public void onClickViewed(View view){
        if (view.getId() == R.id.iv_back) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(afterPosition != beforePosition){
            startActivity(new Intent(LanguageActivity.this,MainActivity.class));
            HomeTitlesUtils.getInstance().clear();
            AppManager.instance.finishAllActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mUnbinder != null){
            mUnbinder.unbind();
        }
    }
}

