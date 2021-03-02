package com.project.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.bean.DailySignInTasItemBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DailyLoginTaskAdapter extends BaseQuickAdapter<DailySignInTasItemBean,BaseViewHolder> {

    @Override
    public int getItemViewType(int position) {
        return position == 6 ? 3:1;
    }

    public DailyLoginTaskAdapter(List<DailySignInTasItemBean> datas) {
        super(R.layout.item_daily_login_task_score, datas);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, DailySignInTasItemBean bean) {
        int adapterPostion = helper.getAdapterPosition();

        if(bean.isSignIn()){
            helper.setVisible(R.id.iv_howDayDecor,true);
            helper.setVisible(R.id.tv_signInNumber,false);
            helper.setVisible(R.id.ll_boundAward,false);
        }else{
            if(adapterPostion != 6){
                helper.setVisible(R.id.iv_howDayDecor,false);
                helper.setVisible(R.id.ll_boundAward,false);
                helper.setVisible(R.id.tv_signInNumber, true);
                helper.setText(R.id.tv_signInNumber,String.valueOf(bean.getSignNumber()));
            }else{
                helper.setVisible(R.id.iv_howDayDecor,false);
                helper.setVisible(R.id.tv_signInNumber, false);
                helper.setVisible(R.id.ll_boundAward,true);
            }
        }

        switch (adapterPostion){
            case 0:
                helper.setImageResource(R.id.iv_howDayDecor,R.mipmap.ic_sign_in_daily_one);
                break;
            case 1:
                helper.setImageResource(R.id.iv_howDayDecor,R.mipmap.ic_sign_in_daily_two);
                break;
            case 2:
                helper.setImageResource(R.id.iv_howDayDecor,R.mipmap.ic_sign_in_daily_three);
                break;
            case 3:
                helper.setImageResource(R.id.iv_howDayDecor,R.mipmap.ic_sign_in_daily_four);
                break;
            case 4:
                helper.setImageResource(R.id.iv_howDayDecor,R.mipmap.ic_sign_in_daily_five);
                break;
            case 5:
                helper.setImageResource(R.id.iv_howDayDecor,R.mipmap.ic_sign_in_daily_six);
                break;
            case 6:
                helper.setImageResource(R.id.iv_howDayDecor,R.mipmap.ic_sign_in_daily_seven);
                break;
        }
    }
}
