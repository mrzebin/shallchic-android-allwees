package com.project.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hb.basemodel.utils.AppManager;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.ToastUtil;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment;
import com.qmuiteam.qmui.arch.annotation.FirstFragments;
import com.qmuiteam.qmui.arch.annotation.LatestVisitRecord;
import com.project.app.R;
import com.project.app.base.BaseActivity;
import com.project.app.bean.ParseFacebookDeeplinkBean;
import com.project.app.fragment.home.HomeFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentContainerView;

@FirstFragments(
    value = {
            HomeFragment.class,
    }
)
@DefaultFirstFragment(HomeFragment.class)
@LatestVisitRecord

public class MainActivity extends BaseActivity {
    private long exitTime = 0;
    public static ParseFacebookDeeplinkBean mDeeplinkBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
        refreshLocaleEnvironment();
        parseDeepLink();
    }

    //解析facebook link
    private void parseDeepLink() {
       Uri deeplinkUri =  getIntent().getData();
       if(deeplinkUri != null){
           String alApplinkData = deeplinkUri.getQueryParameter("al_applink_data");
           if(!TextUtils.isEmpty(alApplinkData)){
               ParseFacebookDeeplinkBean deeplinkBean = JsonUtils.deserialize(alApplinkData,ParseFacebookDeeplinkBean.class);
               if(deeplinkBean != null){
                   mDeeplinkBean = deeplinkBean;
               }
           }
       }
    }

    @Override
    protected RootView onCreateRootView(int fragmentContainerId) {
        return new CustomRootView(this, fragmentContainerId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static Intent of(@NonNull Context context,
                            @NonNull Class<? extends QMUIFragment> firstFragment) {
        return QMUIFragmentActivity.intentOf(context, MainActivity.class, firstFragment);
    }

    public static Intent of(@NonNull Context context,
                            @NonNull Class<? extends QMUIFragment> firstFragment,
                            @Nullable Bundle fragmentArgs) {
        return QMUIFragmentActivity.intentOf(context, MainActivity.class, firstFragment, fragmentArgs);
    }



    static class CustomRootView extends QMUIFragmentActivity.RootView {
        private final FragmentContainerView fragmentContainer;

        public CustomRootView(Context context, int fragmentContainerId) {
            super(context, fragmentContainerId);
            fragmentContainer = new FragmentContainerView(context);
            fragmentContainer.setId(fragmentContainerId);
            addView(fragmentContainer,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        }

        @Override
        public FragmentContainerView getFragmentContainerView() {
            return fragmentContainer;
        }
    }

    @Override
    public void onBackPressed() {
        String exitWarn = getResources().getString(R.string.exit_hint);
        if(System.currentTimeMillis() - exitTime > 2000){
            exitTime = System.currentTimeMillis();
            ToastUtil.showToast(exitWarn);
        }else{
//            moveTaskToBack(true);
            System.gc();
            Glide.get(this).clearMemory();
            AppManager.instance.finishAllActivity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid()); //杀死这个进程
        }
    }

}