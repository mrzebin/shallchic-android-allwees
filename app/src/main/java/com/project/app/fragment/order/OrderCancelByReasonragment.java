package com.project.app.fragment.order;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.utils.LoggerUtil;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.project.app.R;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.OrderDetailBean;
import com.project.app.contract.OrderCancelContract;
import com.project.app.presenter.OrderCancelPresenter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * cancel 取消订单
 */
public class OrderCancelByReasonragment extends BaseMvpQmuiFragment<OrderCancelPresenter> implements OrderCancelContract.View {
    @BindView(R.id.tv_cancelType)
    TextView tv_cancelType;
    @BindView(R.id.et_reNote)
    EditText et_reNote;

    private String mOrderId;
    private String mItemUuid;
    private Context mContext;
    private String mCode = "6";
    private String mCancel = "";
    private String[] mCancleTypes    = new String[]{};

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_cancle;
    }

    @Override
    public void initView() {
        initTopbar();
        initWidget();
    }

    private void initTopbar() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        Bundle baba = getArguments();
        mItemUuid = baba.getString("orderItemUuid");
        mOrderId  = baba.getString("orderUuid");
        LoggerUtil.i("orderUuid:" + mOrderId + "--orderItemUuid:" + mItemUuid);
        mCancel = getContext().getResources().getString(R.string.str_cancel);
        mCancleTypes = getContext().getResources().getStringArray(R.array.cancel_reason);
    }

    private void initWidget() {
        mContext = getContext();
        mPresenter = new OrderCancelPresenter();
        mPresenter.attachView(this);
        tv_cancelType.setText(mCancleTypes[0]);
    }

    @OnClick({R.id.btn_postRefund,R.id.ll_choiceRefundType,R.id.iv_back})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.btn_postRefund:
                checkInputValid();
                break;
            case R.id.ll_choiceRefundType:
                showSelectRefundReason(true, true, false,
                        getResources().getString(R.string.order_cancel_order_reason_title), mCancleTypes.length,
                        false, false);
                break;
            case R.id.iv_back:
                popBackStack();
                break;
        }
    }

    private void checkInputValid() {
        String note = et_reNote.getText().toString().trim();
        String reason = tv_cancelType.getText().toString().trim();
        mPresenter.submitCancelReasonToService(mItemUuid,mOrderId,reason,note,0);
    }

    private void showSelectRefundReason(boolean gravityCenter,
                                        boolean addCancelBtn,
                                        boolean withIcon,
                                        CharSequence title,
                                        int itemCount,
                                        boolean allowDragDismiss,
                                        boolean withMark) {
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(mContext);
        builder.setGravityCenter(gravityCenter)
                .setSkinManager(QMUISkinManager.defaultInstance(mContext))
                .setTitle(title)
                .setAddCancelBtn(addCancelBtn)
                .setAllowDrag(allowDragDismiss)
                .setNeedRightMark(withMark)
                .setCancelText(mCancel)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    if(position < 5){
                        mCode = 6 + position + "";
                    }else{
                        mCode = "0";
                    }
                    tv_cancelType.setText(mCancleTypes[position]);
                });
        for(int i=0;i<itemCount;i++){
            builder.addItem(mCancleTypes[i]);
        }
        builder.build().show();
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    public void submitCancalSuccess(OrderDetailBean result) {
        String cancelSuccess = getContext().getResources().getString(R.string.cancel_success);
        EventBus.getDefault().postSticky(new RefreshDataEvent(Constant.EVENT_REFRESH_ORDER_STATE));
        ToastUtil.showToast(cancelSuccess);
        popBackStack();
    }

    @Override
    public void submitCancalFail(String result) {
        String cancelFail = getContext().getResources().getString(R.string.cancel_fail);
        ToastUtil.showToast(cancelFail);
    }

    @Override
    public void startLoading() {
        startProgressDialog(getContext());
    }

    @Override
    public void stopLoading() {
        stopProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
