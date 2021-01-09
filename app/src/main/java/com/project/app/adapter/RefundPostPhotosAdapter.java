package com.project.app.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.project.app.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * create by hb
 * 物流信息
 */
public class RefundPostPhotosAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements LoadMoreModule {
    final int defaultCover = R.mipmap.ic_refund_add;

    public RefundPostPhotosAdapter(List<String> data) {
        super(R.layout.item_refund_photos_post, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, String localCacheFile) {
        int position = helper.getAdapterPosition();
        QMUIRadiusImageView iv_cover = helper.getView(R.id.qriv_photoCover);

        if(localCacheFile.equals("-1")){
            iv_cover.setImageResource(defaultCover);
            helper.setVisible(R.id.iv_refundDelete,false);
        }else{
            Bitmap bitmap = BitmapFactory.decodeFile(localCacheFile);
            iv_cover.setImageBitmap(bitmap);
        }
        if(localCacheFile.equals("-1")){
            helper.getView(R.id.qriv_photoCover).setOnClickListener(view -> {
                if(listener != null){
                    listener.choicePhotoByFunction();
                }
            });
        }

        helper.getView(R.id.iv_refundDelete).setOnClickListener(view -> {
            if(listener != null){
                listener.deletePhotoByFunction(position);
            }
        });
    }

    public ChoiceCallbackListener listener;

    public ChoiceCallbackListener getListener() {
        return listener;
    }

    public void setListener(ChoiceCallbackListener listener) {
        this.listener = listener;
    }

    public interface ChoiceCallbackListener{
        void choicePhotoByFunction();
        void deletePhotoByFunction(int deleteIndex);
    }
}
