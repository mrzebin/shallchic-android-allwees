package com.project.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.basemodel.image.ImageLoader;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.fragment.GoodsDetailFragment;
import com.project.app.utils.LocaleUtil;
import com.project.app.utils.MathUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class CartOrderAdapter extends RecyclerView.Adapter<QMUISwipeViewHolder>{
    private final Context mContext;
    private final List<CartBuyDataBean.ProductItem> mData;
    private final QMUISwipeAction mDeleteAction;

    public CartOrderAdapter(Context context,List<CartBuyDataBean.ProductItem> datas){
        this.mContext = context;
        this.mData    = datas;
        String delete = context.getString(R.string.str_delete);

        QMUISwipeAction.ActionBuilder builder = new QMUISwipeAction.ActionBuilder()
                .textSize(QMUIDisplayHelper.sp2px(context, 14))
                .textColor(Color.WHITE)
                .swipeDirectionMinSize(QMUIDisplayHelper.dp2px(context,5))
                .paddingStartEnd(QMUIDisplayHelper.dp2px(context, 18));

        mDeleteAction = builder.text(delete).backgroundColor(Color.RED).build();
    }

    @NonNull
    @Override
    public QMUISwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_corder_product, parent, false);
        final QMUISwipeViewHolder vh = new QMUISwipeViewHolder(view);
        vh.addSwipeAction(mDeleteAction);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull QMUISwipeViewHolder helper, int position) {
        boolean isPfree  = false;
        String  strFree  = mContext.getString(R.string.str_free);
        String  languate = LocaleUtil.getInstance().getLanguage();
        CartBuyDataBean.ProductItem productItem = mData.get(position);
        CartBuyDataBean.Product product = productItem.getProduct();
        CartBuyDataBean.Sku productSku  = productItem.getSku();
        View viewHolder         = helper.itemView;
        String goodsUrl         = product.getMainPhoto();
        ImageView iv_goods      = viewHolder.findViewById(R.id.iv_carGoods);
        TextView tv_free        = viewHolder.findViewById(R.id.tv_free);
        TextView tv_origin      = viewHolder.findViewById(R.id.tv_cartOriginP);
        TextView tv_cGoodsRult  = viewHolder.findViewById(R.id.tv_cartGoodsRult);
        TextView tv_cGoodsDes   = viewHolder.findViewById(R.id.tv_cartGoodsDes);
        TextView tv_cartBuyP    = viewHolder.findViewById(R.id.tv_cartBuyP);
        TextView tv_cShippingP  = viewHolder.findViewById(R.id.tv_cartShippingP);
        TextView tv_cShipping   = viewHolder.findViewById(R.id.tv_cShipping);
        TextView tv_carBuyCount = viewHolder.findViewById(R.id.tv_carBuyCount);
        LinearLayout ll_cModifyCount = viewHolder.findViewById(R.id.ll_cModifyCount);

        String retailPrice = MathUtil.Companion.formatPrice(productSku.getRetailPrice());
        String orginPrice  = MathUtil.Companion.formatPrice(productSku.getOriginalPrice());
        String sPrice;
        String gSize       = productItem.sku.getSize();
        String gColor      = productItem.sku.getColor();

        if(productItem.getAmtShipping() == 0 ){
            sPrice = strFree;
            tv_cShippingP.setTextColor(mContext.getResources().getColor(R.color.allwees_theme_color));
        }else{
            sPrice =  MathUtil.Companion.formatPrice(productSku.getShippingPrice());
        }

        if(languate.equals("ar")){
            tv_cShippingP.setVisibility(View.GONE);
            tv_cShipping.setVisibility(View.GONE);
        }

        if(productItem.getProduct().getRetailPrice() == 0){
            isPfree = true;
            tv_free.setVisibility(View.VISIBLE);
        }else{
            tv_free.setVisibility(View.GONE);
        }

        if(productItem.getFree()){
            isPfree = true;
            tv_free.setVisibility(View.VISIBLE);
        }else{
            tv_free.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(goodsUrl)){
            ImageLoader.getInstance().displayImage(iv_goods,goodsUrl,R.mipmap.allwees_ic_default_goods);
        }else{
            iv_goods.setImageResource(R.mipmap.allwees_ic_default_goods);
        }

        if(!TextUtils.isEmpty(product.getName())){
            tv_cGoodsDes.setText(product.getName());
        }

        if(!TextUtils.isEmpty(retailPrice)){
            tv_cartBuyP.setText(retailPrice);
        }

        if(!TextUtils.isEmpty(orginPrice)){
            if(productSku.getOriginalPrice() == productSku.getRetailPrice()){
                tv_origin.setText(strFree);
            }else if(productSku.getOriginalPrice() > productSku.getRetailPrice()){
                tv_origin.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_origin.setText(orginPrice);
            }
        }
        if(!TextUtils.isEmpty(sPrice)){
            tv_cShippingP.setText(sPrice);
        }
        tv_cGoodsRult.setText(gColor+","+ gSize);
        tv_carBuyCount.setText(productItem.getQuantity()+"");

        boolean finalIsPfree = isPfree;
        ll_cModifyCount.setOnClickListener(view -> {
            if(listener != null){
                listener.modifyBuyCount(productItem.getQuantity(),productSku.getUuid(),product.getUuid(), finalIsPfree);
            }
        });

        helper.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("uuid", productItem.product.getUuid());
            bundle.putString("type", "1");
            Intent intent = HolderActivity.of(mContext, GoodsDetailFragment.class,bundle);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(@Nullable List<CartBuyDataBean.ProductItem> list) {
        mData.clear();
        if (list != null) {
            mData.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    public void add(int pos, CartBuyDataBean.ProductItem item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    public void prepend(@NonNull List<CartBuyDataBean.ProductItem> items) {
        mData.addAll(0, items);
        notifyDataSetChanged();
    }

    public void append(@NonNull List<CartBuyDataBean.ProductItem> items) {
        mData.addAll(items);
        notifyDataSetChanged();
    }

    public InDecreaseCountListener listener;

    public InDecreaseCountListener getListener() {
        return listener;
    }

    public void setListener(InDecreaseCountListener listener) {
        this.listener = listener;
    }

    public interface InDecreaseCountListener{
        void modifyBuyCount(int count,String skuuid,String productUuid,boolean isFree);
    }

}
