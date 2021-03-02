package com.project.app.receiver;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.utils.LoggerUtil;
import com.hb.basemodel.utils.SPManager;
import com.hb.basemodel.utils.SystemUtil;
import com.project.app.activity.MainActivity;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 小米的推送
 */
public class MIuiPushMessageReceiver extends PushMessageReceiver{
    private String mRegId;
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mUserAccount;
    private String mStartTime;
    private String mEndTime;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        LoggerUtil.i("onReceivePassThroughMessage:" + message.getContent());
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount=message.getUserAccount();
        }
        LoggerUtil.i("推送后台数据:" + mMessage + "--mTopic:" + mTopic + "--mAlias:" + mAlias + "--mUserAccount" + mUserAccount);
    }
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        LoggerUtil.i("onNotificationMessageClicked:" + message.getContent());
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount=message.getUserAccount();
        }

        if(!SystemUtil.isAppAlive(context,android.os.Process.myPid(), Constant.APP_PACKAGE_NAME)){   //如果不是活的
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(Constant.APP_PACKAGE_NAME);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(launchIntent);
        }else{
            Intent mainIntent = new Intent(context, MainActivity.class);  //是活的
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
        }

        if(!TextUtils.isEmpty(message.getContent())){
            try {
                JSONObject pushJson = new JSONObject(message.getContent());
                JSONObject contentJSON = pushJson.getJSONObject("content");
                JSONObject deeplikJSON = contentJSON.getJSONObject("deepLink");
                String  deepLink = deeplikJSON.getString("any");
                if(!TextUtils.isEmpty(deepLink)){
                    RefreshDataEvent pushEvent = new RefreshDataEvent(Constant.EVENT_PUSH_APP_DEEPLINK);
                    pushEvent.setData(deepLink);
                    EventBus.getDefault().postSticky(pushEvent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        LoggerUtil.i("onNotificationMessageArrived:" + message.getContent());
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount=message.getUserAccount();
        }
    }
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                LoggerUtil.i("小米推送Id:" + cmdArg1);
                SPManager.sPutString(Constant.SP_BIND_MIUI_PUSH_ID,cmdArg1);   //保存小米的pushid
                mRegId = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
            }
        }
    }
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        }
    }

}
