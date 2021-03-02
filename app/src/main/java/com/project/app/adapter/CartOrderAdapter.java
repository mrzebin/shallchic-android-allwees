package com.project.app.adapter;

import android.content.Context;
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
        boolean isfree  = false;
        String  strFree  = mContext.getString(R.string.str_free);
        CartBuyDataBean.ProductItem productItem = mData.get(position);
        CartBuyDataBean.Product product   = productItem.getProduct();
        CartBuyDataBean.Sku productSku    = productItem.getSku();
        View viewHolder                   = helper.itemView;
        String goodsUrl                   = product.getMainPhoto();
        ImageView iv_goods                = viewHolder.findViewById(R.id.iv_carGoods);
        TextView tv_free                  = viewHolder.findViewById(R.id.tv_free);
        TextView tv_origin                = viewHolder.findViewById(R.id.tv_cartOriginP);
        TextView tv_cGoodsRult            = viewHolder.findViewById(R.id.tv_cartGoodsRult);
        TextView tv_cGoodsDes             = viewHolder.findViewById(R.id.tv_cartGoodsDes);
        TextView tv_cartPriceRetail       = viewHolder.findViewById(R.id.tv_cartPriceRetail);   //售价
        TextView tv_cartSkuShippingPrice  = viewHolder.findViewById(R.id.tv_cartSkuShippingPrice);
        TextView tv_carBuyCount           = viewHolder.findViewById(R.id.tv_carBuyCount);
        LinearLayout ll_cModifyCount      = viewHolder.findViewById(R.id.ll_cModifyCount);

        String retailPrice = MathUtil.Companion.formatPrice(productSku.getPriceRetail());
        String orginPrice  = MathUtil.Companion.formatPrice(productSku.getPriceOrigin());
        String gSize       = productItem.sku.getSize();
        String gColor      = productItem.sku.getColor();

        //售价为0则free,邮费按amtShipping价格来计算
        if(productItem.getSku().getPriceRetail() == 0 ){
            tv_cartSkuShippingPrice.setTextColor(mContext.getResources().getColor(R.color.theme_color));
            tv_cartSkuShippingPrice.setText(strFree);
        }else{
            tv_cartSkuShippingPrice.setText(MathUtil.Companion.formatPrice(productItem.getAmtShipping()));
        }

        if(!TextUtils.isEmpty(goodsUrl)){
            ImageLoader.getInstance().displayImage(iv_goods,goodsUrl,R.mipmap.allwees_ic_default_goods);
        }else{
            iv_goods.setImageResource(R.mipmap.allwees_ic_default_goods);
        }

        if(!TextUtils.isEmpty(product.getName())){
            tv_cGoodsDes.setText(product.getName());
        }

        if(productItem.getSku().getPriceRetail() == 0){    //售价是0则free
            isfree = true;
            tv_free.setVisibility(View.VISIBLE);
        }else{
            tv_free.setVisibility(View.GONE);
            tv_cartPriceRetail.setText(retailPrice);
        }

        if(!TextUtils.isEmpty(orginPrice)){
            if(productSku.getPriceOrigin() == productSku.getPriceRetail()){      //售价跟原价一样不显示原价
                tv_origin.setVisibility(View.GONE);
            }else if(productSku.getOriginalPrice() > productSku.getRetailPrice()){
                tv_origin.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_origin.setText(orginPrice);
            }
        }

        tv_cGoodsRult.setText(gColor+","+ gSize);
        tv_carBuyCount.setText(productItem.getQuantity()+"");

        boolean finalIsPfree = isfree;
        ll_cModifyCount.setOnClickListener(view -> {
            if(listener != null){
                listener.modifyBuyCount(productItem.getQuantity(),productSku.getUuid(),product.getUuid(), finalIsPfree);
            }
        });

        helper.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("uuid", productItem.product.getUuid());
            bundle.putString("type", "1");
            HolderActivity.startFragment(mContext,GoodsDetailFragment.class,bundle);
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
