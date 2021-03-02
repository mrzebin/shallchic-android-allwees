package com.project.app.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import com.hb.basemodel.utils.AppManager;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.SwipeBackLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.project.app.fragment.home.HomeFragment;
import com.project.app.ui.dialog.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseMvpQmuiFragment<T extends BasePresenter> extends QMUIFragment{
    public T mPresenter;
    private boolean isViewPrepared; // 标识fragment视图已经初始化完毕
    public boolean hasFetchData;   // 标识已经触发过懒加载数据
    private Unbinder mUnBinder;
    protected LoadingDialog dialog;
    private View mView;

    private long  dialogCreateTime;
    private final Handler handler = new Handler();

    public abstract int getLayoutId();

    public abstract void initView();

    protected abstract void lazyFetchData();

    public View getmView() {
        return mView;
    }

    public void setmView(View mView) {
        this.mView = mView;
    }

    @Override
    protected View onCreateView() {
        mView = LayoutInflater.from(getContext()).inflate(getLayoutId(),null);
        mUnBinder = ButterKnife.bind(this,mView);
        this.initView();
        isViewPrepared = true;
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lazyFetchDataIfPrepared();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        lazyFetchDataIfPrepared();
    }

    @Override
    protected int backViewInitOffset(Context context, int dragDirection, int moveEdge) {
        if (moveEdge == SwipeBackLayout.EDGE_TOP || moveEdge == SwipeBackLayout.EDGE_BOTTOM) {
            return 0;
        }
        return QMUIDisplayHelper.dp2px(context, 100);
    }

    @Override
    public Object onLastFragmentFinish() {
        return new HomeFragment();
    }

    private void lazyFetchDataIfPrepared() {
        if (isViewPrepared && getUserVisibleHint() && !hasFetchData) {
            lazyFetchData();
            hasFetchData = true;
        }
    }

    /**
     * fragment的清栈
     */
    public void clearHolderStack(){
        AppManager.instance.killActivity(getActivity());
    }

    /**
     * 开启加载效果
     */
    public void startProgressDialog(Context context) {
//        if(mLoadingUtil == null){
//            mLoadingUtil = new HLoadingDialogUtil(new WeakReference<>(context), R.style.hLoadingStyle);
//            mLoadingUtil.setCancalAble(false);
//            mLoadingUtil.setCancelOutSideAble(false);
//            mLoadingUtil.create();
//            mLoadingUtil.show();
//        }
        if (dialog == null ||!dialog.isShowing()) {
            dialogCreateTime = System.currentTimeMillis();
            dialog = new LoadingDialog(context);
            dialog.show();
        }
    }

    /**
     * 关闭加载
     */
    public void stopProgressDialog() {
        dismissLoadingDialog(null);
//        if(mZDialog != null){
//            mZDialog.dismiss();
//        }
    }

    /**
     * 取消加载dialog. 因为延迟， 所以要延时完成之后， 再在 runnable 中执行逻辑.
     * <p>
     * 延迟关闭时间是因为接口有时返回太快。
     */
    public void dismissLoadingDialog(Runnable runnable) {
        if (!getActivity().isFinishing()) {
            if (dialog != null && dialog.isShowing()) {
                // 由于可能请求接口太快，则导致加载页面一闪问题， 所有再次做判断，
                // 如果时间太快（小于 500ms）， 则会延时 1s，再做关闭。
//                if (dialog != null && getActivity() != null) {
//                    dialog.dismiss();
//                    dialog = null;
//                }
                if (System.currentTimeMillis() - dialogCreateTime < 500) {
                    handler.postDelayed(() -> {
                        if (runnable != null) {
                            runnable.run();
                        }
                        if (dialog != null && getActivity() != null) {
                            dialog.dismiss();
                            dialog = null;
                        }
                    }, 1000);
                } else {
                    if(dialog != null && getActivity() != null){
                        dialog.dismiss();
                        dialog = null;
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.detachView();
        }
        hasFetchData = false;
        isViewPrepared = false;
        if(mUnBinder != null){
            mUnBinder.unbind();
        }
    }
}