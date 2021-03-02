package com.project.app.fragment.order;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.basemodel.utils.DataUtil;
import com.project.app.R;
import com.project.app.adapter.LogisticTrackAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.LogisticTrackBean;
import com.project.app.contract.LogisticsInfomationContract;
import com.project.app.presenter.LogisticsInfomationPresenter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 物流信息
 */
public class OrderLogisticsInformationFragment extends BaseMvpQmuiFragment<LogisticsInfomationPresenter> implements LogisticsInfomationContract.View{
    @BindView(R.id.rvl_logisticTrack)
    RecyclerView rvl_logisticTrack;
    @BindView(R.id.tv_currentTrackNo)   //物流信息
    TextView tv_logisticNo;
    @BindView(R.id.tv_shippingCompanyName)
    TextView tv_shippingCompanyName;
    @BindView(R.id.tv_linkLogistics)
    TextView tv_linkLogistics;
    @BindView(R.id.iv_copyTrackNo)
    ImageView iv_copyTrackNo;

    private String mTrackNo;
    private Context mContext;
    private ScaleAnimation mScaleAnima;
    private List<LogisticTrackBean.LogisticTrackItem> mTrackList = new ArrayList<>();
    private LogisticTrackAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_logistics_information;
    }

    @Override
    public void initView() {
        initBundle();
        initWidget();
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if(bundle != null){
            mTrackNo = bundle.getString("trackNo");
        }

        mTrackNo = "11166d26660c40dfa0d75cf11a46762c";
    }

    private void initWidget() {
        mContext = getContext();
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        mPresenter = new LogisticsInfomationPresenter();
        mPresenter.attachView(this);

        mScaleAnima = new ScaleAnimation(1,1.2f,1,1.2f, 0.5f,0.5f);
        mScaleAnima.setDuration(300);
        mScaleAnima.setFillAfter(false);
        mScaleAnima.setRepeatCount(0);

        rvl_logisticTrack.setLayoutManager(new LinearLayoutManager(getContext()));
        String linkCompany = mContext.getResources().getString(R.string.logistics_link_valid_company);
        String linkWebSite = mContext.getResources().getString(R.string.logistics_link_valid_by_website);

        tv_linkLogistics.setText(Html.fromHtml(linkCompany + "<font color=\"#31B5ED\">" + "97131241241" + "</font>") + linkWebSite);
        mAdapter = new LogisticTrackAdapter(mTrackList);
        rvl_logisticTrack.setAdapter(mAdapter);
    }

    private View getEmptyLogistic(){
        String noData = mContext.getResources().getString(R.string.str_nd);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_empty_order,null);
        TextView tv_hint = view.findViewById(R.id.tv_emptyOrderHint);
        LinearLayout cl_inflateView = view.findViewById(R.id.cl_refreshEmpty);
        cl_inflateView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dp2px(mContext,350)));
        QMUIRoundButton qrb_refreshOrder = view.findViewById(R.id.qrb_refreshOrder);
        qrb_refreshOrder.setVisibility(View.GONE);
        tv_hint.setText(noData);
        return view;
    }

    @OnClick({R.id.iv_back,R.id.iv_copyTrackNo})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
            case R.id.iv_copyTrackNo:    //辅助文本到粘贴板
                iv_copyTrackNo.setAnimation(mScaleAnima);
                iv_copyTrackNo.startAnimation(mScaleAnima);
                String copyNo = tv_logisticNo.getText().toString().trim();
                if(TextUtils.isEmpty(copyNo)){
                    return;
                }
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", copyNo);
                cm.setPrimaryClip(mClipData);
                break;
        }
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.checkLogisticTrack(mTrackNo);
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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void fetchLogisticTSuccess(LogisticTrackBean tracks) {
        if(tracks == null){
            return;
        }
        if(DataUtil.idNotNull(tracks.getData())){
            mAdapter = new LogisticTrackAdapter(tracks.getData());
            rvl_logisticTrack.setAdapter(mAdapter);
            if(!TextUtils.isEmpty(tracks.getTrackingNumber())){
                tv_logisticNo.setText(tracks.getTrackingNumber());
            }
        }else{
            mAdapter = new LogisticTrackAdapter(mTrackList);
            mAdapter.setEmptyView(getEmptyLogistic());
            rvl_logisticTrack.setAdapter(mAdapter);
            tv_logisticNo.setText("");
        }
    }

    @Override
    public void fetchLogisticTFail(String msg) {

    }
}
