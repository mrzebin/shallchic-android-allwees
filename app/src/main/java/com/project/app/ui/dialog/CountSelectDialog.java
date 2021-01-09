package com.project.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hb.basemodel.wheelview.OnWheelChangedListener;
import com.hb.basemodel.wheelview.WheelView;
import com.hb.basemodel.wheelview.adapter.ArrayWheelAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.project.app.R;

import androidx.annotation.NonNull;

public class CountSelectDialog extends Dialog implements OnWheelChangedListener, View.OnClickListener {
    private Context mContext;
    private BuyCallbackListener mWheelClickLitener;
    private WheelView wv_selectCount;
    private TextView area_tv_ok;
    private TextView area_tv_cancel;
    private View view;
    private int mBuyCount;
    private String mSkuUuid;
    private String mItemUuid;
    private boolean isFree = false;
    private final String[] countTotal = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25",
                         "26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50"};

    private final String[] limitFree = {"0","1"};

    public CountSelectDialog(@NonNull Context context) {
        super(context);
    }

    public CountSelectDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.transparentFrameWindowStyle);
    }

    public CountSelectDialog(Context context, BuyCallbackListener listener) {
        super(context, R.style.transparentFrameWindowStyle);
        mContext = context;
        mWheelClickLitener = listener;
        view = View.inflate(context, R.layout.dialog_select_area, null);
        setContentView(view);
        Window window = getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.Dialog_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wl.x = 0;
        wl.y = wm.getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = (int) (QMUIDisplayHelper.getScreenWidth(getContext()) * 0.9);
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        onWindowAttributesChanged(wl);
        // 设置点击外围解散
        setCanceledOnTouchOutside(true);
        wv_selectCount = view.findViewById(R.id.wv_selectCount);
        area_tv_ok = view.findViewById(R.id.tv_ok);
        area_tv_cancel = view.findViewById(R.id.tv_cancel);
        initView();
    }

    public void referModifyData(int count, String skuuid, String itemUuid,boolean isFree){
        wv_selectCount.invalidateWheel(true);
        this.mBuyCount  = count;
        this.mSkuUuid   = skuuid;
        this.mItemUuid  = itemUuid;
        this.isFree     = isFree;
        ArrayWheelAdapter adapter;
        if(isFree){
            adapter = new ArrayWheelAdapter<>(mContext,limitFree);
        }else{
            adapter = new ArrayWheelAdapter<>(mContext,countTotal);
        }
        wv_selectCount.setViewAdapter(adapter);
    }

    private void initView() {
        wv_selectCount.addChangingListener(this);
        wv_selectCount.setVisibleItems(5);
        area_tv_ok.setOnClickListener(this);
        area_tv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_ok) {
            String address = countTotal[wv_selectCount.getCurrentItem()];
            int modifyCount = Integer.parseInt(address);
            boolean isIncr;
            if(modifyCount == mBuyCount){
                dismiss();
                return;
            }
            isIncr = modifyCount > mBuyCount;
            modifyCount = modifyCount - mBuyCount;   //如果是将则为负数
            mWheelClickLitener.onOkBuyClick(modifyCount,isIncr,mSkuUuid,mItemUuid);
            dismiss();
        } else if (v.getId() == R.id.tv_cancel) {
            dismiss();
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {

    }

    public interface BuyCallbackListener {
        void onOkBuyClick(int buyCount,boolean isIncr,String skuUuid,String itemUuid);
    }
}
