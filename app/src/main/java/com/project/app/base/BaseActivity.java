/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.project.app.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.utils.AppManager;
import com.project.app.MyApp;
import com.project.app.manager.LocaleSwitcherManager;
import com.project.app.ui.dialog.LoadingDialog;
import com.qmuiteam.qmui.arch.QMUIFragmentActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;

@SuppressLint("Registered")
public class BaseActivity extends QMUIFragmentActivity {
    protected Context context;
    private   long  dialogCreateTime;
    protected LoadingDialog dialog;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        AppManager.instance.addActivity(this);
        if (registerEventBus()){
            EventBus.getDefault().register(this);
        }
    }

    protected boolean registerEventBus() {
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleSwitcherManager.INSTANCE.configureBaseContext(newBase));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshDataEvent event){

    }

    /**
     * 刷新语言环境
     */
    public void refreshLocaleEnvironment(){
        LocaleSwitcherManager.INSTANCE.configureBaseContext(this);
    }

    /**
     * 开启加载效果
     */
    public void startProgressDialog() {
        if (dialog == null || (!dialog.isShowing())) {
            dialogCreateTime = System.currentTimeMillis();
            dialog = new LoadingDialog(MyApp.mContext);
            dialog.show();
        }
    }

    /**
     * 关闭加载
     */
    public void stopProgressDialog() {
        dismissLoadingDialog(null);
    }


    /**
     * 取消加载dialog. 因为延迟， 所以要延时完成之后， 再在 runnable 中执行逻辑.
     * <p>
     * 延迟关闭时间是因为接口有时返回太快。
     */
    public void dismissLoadingDialog(Runnable runnable) {
        if (!isDestroyed()) {
            if (dialog != null && dialog.isShowing()) {
                // 由于可能请求接口太快，则导致加载页面一闪问题， 所有再次做判断，
                // 如果时间太快（小于 500ms）， 则会延时 1s，再做关闭。
                if (System.currentTimeMillis() - dialogCreateTime < 500) {
                    handler.postDelayed(() -> {
                        if (runnable != null) {
                            runnable.run();
                        }
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                        }
                    }, 1000);

                } else {
                    dialog.dismiss();
                    dialog = null;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (registerEventBus())
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}

