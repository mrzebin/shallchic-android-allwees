package com.project.app.adapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.utils.JsonUtils;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.bean.AddressBean;
import com.project.app.fragment.home.ShippingAddressFragment;
import com.project.app.utils.LocaleUtil;
import com.project.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AddressMaangerAdapter extends BaseQuickAdapter<AddressBean.AddressItem, BaseViewHolder> {
    private int mType;  //0显示checkbox 1不显示checkbox
    private final List<AddressBean.AddressItem> mDatas;

    public AddressMaangerAdapter(@Nullable List<AddressBean.AddressItem> data,int type) {
        super(R.layout.item_address_file, data);
        this.mType = type;
        this.mDatas = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, AddressBean.AddressItem item) {
        CheckBox cb_selectA = helper.findView(R.id.cb_selectA);
        int position = helper.getAdapterPosition();
        String languageType    = LocaleUtil.getInstance().getLanguage();
        String addressStreet   = item.getStreet();
        String addressCity     = item.getCity();
        String addressProvince = item.getProvince();
        String addressCountry  = item.getCountry();
        String zipCode         = item.getZipCode();
        String note            = item.getNote();
        String address1        = item.getAddressLine1();
        String address2        = item.getAddressLine2();
        String phoneNum        = item.getPhone();

        StringBuffer holeName = new StringBuffer();
        if(!TextUtils.isEmpty(item.getFirstName())){
            holeName.append(item.getFirstName());
        }
        if(!TextUtils.isEmpty(item.getLastName())){
            holeName.append(item.getLastName());
        }
        //显示完成的名字
        if(!TextUtils.isEmpty(holeName.toString())){
            helper.setText(R.id.tv_aPeopleName,holeName.toString());
        }

        StringBuilder holeAddress = new StringBuilder();
        if(!TextUtils.isEmpty(addressStreet)){
            holeAddress.append(addressStreet + " ");
        }

        if(!TextUtils.isEmpty(addressCity)){
            holeAddress.append(addressCity+" ");
        }
        if(!TextUtils.isEmpty(addressProvince)){
            holeAddress.append(addressProvince + " ");
        }
        if(!TextUtils.isEmpty(addressCountry)){
            holeAddress.append(addressCountry+" ");
        }

        if(!TextUtils.isEmpty(zipCode)){
            holeAddress.append(zipCode + " ");
        }

        if(address1.contains("#")){
            holeAddress.append(StringUtils.filterZipCode(address1) + " ");   //提取code码
        }

        if(!TextUtils.isEmpty(note)){
            holeAddress.append(note + " ");
        }

        helper.setText(R.id.tv_cpSet,holeAddress.toString());

        if(!TextUtils.isEmpty(address1)){
            if(address1.contains("#")){
                helper.setText(R.id.tv_firstAM, StringUtils.filterValidAddress(address1));  //提取地址详情
            }else{
                helper.setText(R.id.tv_firstAM, address1);  //提取地址详情
            }
        }

        if(!TextUtils.isEmpty(phoneNum)){
            String areaNum  = ""; //区号
            if(phoneNum.contains("(")){
                int lastBrackets = phoneNum.lastIndexOf(")");
                areaNum  = phoneNum.substring(1,lastBrackets);
                phoneNum = phoneNum.substring(lastBrackets+1,phoneNum.length());
            }
            helper.setText(R.id.tv_cpPhone,"+" + areaNum + phoneNum);
        }

        if(mType == 0){
            cb_selectA.setVisibility(View.VISIBLE);
            if(mDatas.size() == 1){
                cb_selectA.setVisibility(View.GONE);
                helper.setVisible(R.id.iv_retriveLocation,true);
            }else{
                helper.setVisible(R.id.iv_retriveLocation,false);
                cb_selectA.setChecked(item.isDefault());
            }
        }else{
            cb_selectA.setVisibility(View.GONE);
        }

        cb_selectA.setOnCheckedChangeListener((compoundButton, isCheck) -> {
            if(listener != null && isCheck){
                listener.reRefreshCheck(helper.getAdapterPosition(),item.getUuid());
            }
        });

        helper.getView(R.id.tv_editAddress).setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("type", ShippingAddressFragment.INDEX_ACTION_EDIT);
            bundle.putString("data", JsonUtils.serialize(item));
            HolderActivity.startFragment(getContext(),ShippingAddressFragment.class,bundle);
        });
        helper.getView(R.id.tv_DeleteAddress).setOnClickListener(view -> {
            if(listener != null){
                listener.oprDelete(position,item.getUuid());
            }
        });
    }

    public void repalceAllData(List<AddressBean.AddressItem> data){
        if(mDatas.size() >0){
            mDatas.clear();
        }
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public OpreationAddressListener listener;

    public OpreationAddressListener getListener() {
        return listener;
    }

    public void setListener(OpreationAddressListener listener) {
        this.listener = listener;
    }

   public interface OpreationAddressListener{
        void oprDelete(int position,String addressId);
        void reRefreshCheck(int position,String addressId);
    }
}
