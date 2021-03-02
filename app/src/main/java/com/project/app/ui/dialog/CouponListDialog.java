package com.project.app.ui.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.utils.DataUtil;
import com.project.app.R;
import com.project.app.bean.CartCouponsBaean;
import com.project.app.contract.CartCouponsDialogContract;
import com.project.app.presenter.CartCouponDialogPresenter;
import com.project.app.utils.MathUtil;
import com.project.app.utils.StringUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CouponListDialog extends Dialog implements View.OnClickListener, CartCouponsDialogContract.View {
    private Context mContext;
    private View view;
    private SmartRefreshLayout srl_cartCoupons;
    private RecyclerView rlv_couponSheet;
    private QMUIEmptyView ev_loading;
    private ImageView iv_closeCoupon;
    private TextView tv_emptyHint;
    private CouponsAdapter mAdapter;
    private final List<CartCouponsBaean.CpItem> couponSheets = new ArrayList<>();
    private ScaleAnimation mScaleAnima;
    private CartCouponDialogPresenter mPresenter;

    public CouponListDialog(@NonNull Context context) {
        super(context);
    }

    public CouponListDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.transparentFrameWindowStyle);
    }

    public CouponListDialog(Context context, CouponCallBack listener) {
        super(context, R.style.transparentFrameWindowStyle);
        initAnim();
        mContext = context;
        view = View.inflate(context, R.layout.dialog_select_coupon_code, null);
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
        wl.width  = QMUIDisplayHelper.getScreenWidth(getContext());
        wl.height = (int) (QMUIDisplayHelper.getScreenHeight(getContext())*0.7);
        // 设置显示位置
        onWindowAttributesChanged(wl);
        // 设置点击外围解散
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initAnim() {
        mScaleAnima = new ScaleAnimation(0.9f,1f,0.9f,1f, 0.5f,0.5f);
        mScaleAnima.setDuration(300);
        mScaleAnima.setFillAfter(false);
        mScaleAnima.setRepeatCount(0);
    }

    private void initView() {
        mPresenter = new CartCouponDialogPresenter();
        mPresenter.attachView(this);
        srl_cartCoupons = view.findViewById(R.id.srl_cartCoupons);
        ev_loading      = view.findViewById(R.id.ev_loading);
        tv_emptyHint    = view.findViewById(R.id.tv_emptyHint);
        iv_closeCoupon  = view.findViewById(R.id.iv_closeCDialog);
        iv_closeCoupon.setOnClickListener(this);
        rlv_couponSheet = view.findViewById(R.id.rlv_couponSheet);
        mAdapter = new CouponsAdapter(couponSheets);
        rlv_couponSheet.setLayoutManager(new LinearLayoutManager(mContext));
        rlv_couponSheet.setAdapter(mAdapter);
    }

    public void reloadingCouponList(){
        mPresenter.fetchCouponList();
        show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_closeCDialog) {
            dismiss();
        }
    }

    @Override
    public void fetchCouponListSuccess(CartCouponsBaean result) {
       ev_loading.setVisibility(View.GONE);
       srl_cartCoupons.setVisibility(View.VISIBLE);
        if(DataUtil.idNotNull(result.getData())){
            rlv_couponSheet.setVisibility(View.VISIBLE);
            tv_emptyHint.setVisibility(View.GONE);
            mAdapter.setNewInstance(result.getData());
        }else{
            rlv_couponSheet.setVisibility(View.GONE);
            tv_emptyHint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void fetchCouponListFail(String msg) {
        ev_loading.setVisibility(View.GONE);
        srl_cartCoupons.setVisibility(View.VISIBLE);
    }

    @Override
    public void fetchNetError() {
        ev_loading.setVisibility(View.GONE);
        srl_cartCoupons.setVisibility(View.VISIBLE);
        tv_emptyHint.setText(getContext().getResources().getString(R.string.no_net_hint));
    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    public interface CouponCallBack{

    }

    class CouponsAdapter extends BaseQuickAdapter<CartCouponsBaean.CpItem, BaseViewHolder>{
        public CouponsAdapter(@Nullable List<CartCouponsBaean.CpItem> data) {
            super(R.layout.item_coupon_wrap, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder helper, CartCouponsBaean.CpItem item) {
            ImageView iv_copy = helper.getView(R.id.iv_copyTrackNo);
            LinearLayout ll_kissCoupon = helper.getView(R.id.ll_kissCoupon);
            helper.setText(R.id.tv_couponNo,item.getNo());
            helper.setText(R.id.tv_offValue,item.getValueText());
            StringBuffer amtStringBuffer = new StringBuffer();
            StringBuffer expireStringBuffer = new StringBuffer();

            String deductAmt = MathUtil.Companion.formatPrice(item.getMaxDeductAmt());
            String max = getContext().getString(R.string.str_max);

            if(!TextUtils.isEmpty(item.getName())){
                amtStringBuffer.append(item.getName());
            }

            amtStringBuffer.append(deductAmt + " ");
            amtStringBuffer.append(max);

            String expireTimePrefix = getContext().getString(R.string.expires_one);
            String expireTime = StringUtils.getEnMDFormat(item.getOverdueTime());
            expireStringBuffer.append(expireTimePrefix+" ");
            expireStringBuffer.append(expireTime);

            helper.setText(R.id.tv_maxBonus,amtStringBuffer.toString());
            helper.setText(R.id.tv_expiresTime,expireStringBuffer.toString());

            ll_kissCoupon.setOnClickListener(view -> {
                iv_copy.setAnimation(mScaleAnima);
                iv_copy.startAnimation(mScaleAnima);
                ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", item.getNo());
                cm.setPrimaryClip(mClipData);
            });
        }
    }
}
