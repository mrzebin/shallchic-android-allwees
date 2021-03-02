package com.project.app.fragment.task;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.adapter.DailyLoginTaskAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.DailySignInBean;
import com.project.app.bean.DailySignInTasItemBean;
import com.project.app.contract.DailySignInContract;
import com.project.app.fragment.earn.RewardFragment;
import com.project.app.presenter.DailySignInPresenter;
import com.project.app.ui.dialog.DailyAwardDialogUtil;
import com.project.app.utils.StatusBarUtils;
import com.project.app.utils.StringUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录任务
 */
public class LoginTaskCouponFragment extends BaseMvpQmuiFragment<DailySignInPresenter> implements DailySignInContract.View {
    @BindView(R.id.ll_inflateStateBar)
    LinearLayout ll_inflateStateBar;
    @BindView(R.id.ll_dailyContainer)
    LinearLayout ll_dailyContainer;
    @BindView(R.id.tv_completeTaskDate)
    TextView tv_completeTaskDate;
    @BindView(R.id.rlv_puzzleTask)
    RecyclerView rlv_puzzleTask;
    @BindView(R.id.tv_viewLatestCoupon)
    TextView tv_viewLatestCoupon;
    private DailyLoginTaskAdapter mAdapter;
    private List<DailySignInTasItemBean> mSignInDatas = new ArrayList<>();
    private DailyAwardDialogUtil mAwardUtil;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login_task;
    }

    @Override
    public void initView() {
        initTopbar();
        initWidget();
    }

    private void initWidget() {
        mPresenter = new DailySignInPresenter();
        mPresenter.attachView(this);
        GridLayoutManager manager = new GridLayoutManager(getContext(),3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 6 ? 3:1;
            }
        });
        rlv_puzzleTask.setLayoutManager(manager);
        mAwardUtil = new DailyAwardDialogUtil(getContext(),true,true);
    }

    @OnClick({R.id.iv_back,R.id.tv_viewLatestCoupon})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_back:
                popBackStack();
                break;
            case R.id.tv_viewLatestCoupon:
//                mAwardUtil.show();
                 HolderActivity.startFragment(getContext(), RewardFragment.class);
                break;
        }
    }

    private void initTopbar() {
        QMUIStatusBarHelper.setStatusBarDarkMode(getActivity());
        StatusBarUtils.setStatusBarView(getContext(),ll_inflateStateBar);
    }

    private void decorSignInPuzzle(DailySignInBean bean) {
        String mdComplete = StringUtils.getEnMDFormat(bean.getCompletedAt());
        tv_completeTaskDate.setText(mdComplete);
        int signTimes = bean.getSignedTimes();
        for(int i=0;i<7;i++){
            DailySignInTasItemBean dailyBean = null;
            if(i < signTimes){
                 dailyBean = new DailySignInTasItemBean(true,i+1);
            }else{
                 dailyBean = new DailySignInTasItemBean(false,i+1);
            }
            mSignInDatas.add(dailyBean);
        }

        if(bean.getUserCoupon() != null){
            if(!TextUtils.isEmpty(bean.getUserCoupon().getNo())){
                mAwardUtil.syncCouponData(bean.getUserCoupon());
                mAwardUtil.show();
            }
        }

//        DailySignInBean.UserCouponItem userItem = new DailySignInBean.UserCouponItem();
//        userItem.setUuid("827b6be78c29445bb10cf5b3c46d4492");
//        userItem.setStatus(0);
//        userItem.setCreatedAt(Long.valueOf("1614413557993"));
//        userItem.setUpdatedAt(Long.valueOf("1614413557993"));
//        userItem.setIdx(0);
//        userItem.setNo("WFAGN7EJ");
//        userItem.setUserUuid("5f1c517ab5a1400f9a8d984890f9931c");
//        userItem.setReceiveDate(Long.valueOf("1614413557992"));
//        userItem.setOverdueTime(Long.valueOf("1616141557992"));
//        userItem.setCouponUuid("c9611687196e4a2ca246625dbb86030e");
//        userItem.setCouponName("Daily Login Bonus");
//        userItem.setCouponUseType("SYSTEM_SING");
//        userItem.setCouponType("RATE");
//        userItem.setFaceValue(0.95);
//        userItem.setMaxDeductAmt(50.00);
//        userItem.setValueText("5%");
//        userItem.setStatusDesc("statusDesc");
//        mAwardUtil.syncCouponData(userItem);

        mAdapter = new DailyLoginTaskAdapter(mSignInDatas);
        rlv_puzzleTask.setAdapter(mAdapter);
    }

    @Override
    protected void lazyFetchData() {
        mPresenter.fetchDailySignIn();
    }

    @Override
    public void fetchDailySignInResultSuccess(DailySignInBean result) {
        if(result == null){
            return;
        }
        ll_dailyContainer.setVisibility(View.VISIBLE);
        decorSignInPuzzle(result);
    }

    @Override
    public void fetchDailySignInResultFail(String msg) {

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
//            mPresenter.onDestoryView();
            mPresenter = null;
            System.gc();
        }
    }
}
