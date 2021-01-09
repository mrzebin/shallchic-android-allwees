package com.project.app.adapter;

import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.bean.languageSettingBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LanguageAdapter extends BaseQuickAdapter<languageSettingBean, BaseViewHolder>{
    private final List<languageSettingBean> mDatas;

    public LanguageAdapter(List<languageSettingBean> data) {
        super(R.layout.item_language_setting, data);
        this.mDatas = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, languageSettingBean bean) {
        helper.setText(R.id.tv_languageName,bean.getLanguageName());
        CheckBox cb_girl = helper.getView(R.id.cb_selectLanguage);

        cb_girl.setChecked(bean.isChoice());

        cb_girl.setOnCheckedChangeListener((compoundButton, isCheck) -> {
            if(isCheck){
                if(listener != null){
                    listener.changeLanguage(helper.getPosition());
                }
            }
        });

        helper.itemView.setOnClickListener(view -> {
            if(!cb_girl.isChecked()){
                if(listener != null){
                    listener.changeLanguage(helper.getPosition());
                }
            }
        });
    }

    public NotifyChangeLanguage listener;

    public NotifyChangeLanguage getListener() {
        return listener;
    }

    public void setListener(NotifyChangeLanguage listener) {
        this.listener = listener;
    }

    public interface NotifyChangeLanguage{
        void changeLanguage(int position);
    }

}
