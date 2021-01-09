package com.project.app.utils;

import android.text.TextUtils;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
import com.project.app.bean.RefundAccessTokenBean;
import com.project.app.s3transferutility.AwsClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ThreadLoopUtil {
    private static ThreadLoopUtil mInstance;
    private static AwsClient mAwsClient;
    private final String accessKey;
    private final String secretId;
    private final String sessionId;
    private final String bucketName;
    private final String prefixName;
    private final String awsRegion;

    public ThreadLoopUtil(){
        String awsJson = SPManager.sGetString(Constant.SP_AWS_ACCESS_TOKEN_RFD);
        RefundAccessTokenBean bean = JsonUtils.deserialize(awsJson,RefundAccessTokenBean.class);
        accessKey   = bean.getAccessKeyId();
        secretId    = bean.getSecretAccessKey();
        sessionId   = bean.getSessionToken();
        bucketName  = bean.getBucketName();
        prefixName  = bean.getKeyPrefix();
        awsRegion   = bean.getRegion();
        mAwsClient = new AwsClient(accessKey,secretId,sessionId,bucketName,awsRegion);
    }

    public static ThreadLoopUtil getInstance(){
        if(mInstance == null){
            mInstance = new ThreadLoopUtil();
        }
        return mInstance;
    }

    static class LoopThread extends Thread{
        private final InputStream mIs;
        private final String remoteName;
        private final CallbackListener listener;

        public LoopThread(InputStream inputStream,String remoteName,CallbackListener listener){
            this.mIs = inputStream;
            this.remoteName = remoteName;
            this.listener = listener;
        }

        @Override
        public void run() {
            super.run();
           String result =  mAwsClient.uploadToS3(mIs,remoteName);
           if(TextUtils.isEmpty(result)){
               listener.s3UploadFail("fail");
           }else{
               listener.s3UploadSuccess(result);
           }
        }
    }

    public void startUploadFile(File file,CallbackListener listener){
        InputStream is;
        String remoteName = prefixName + "android" + System.currentTimeMillis() + ".jpg";
        try {
            is = new FileInputStream(file);
            LoopThread tempThread = new LoopThread(is, remoteName, listener);
            tempThread.start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public CallbackListener listener;

    public CallbackListener getListener() {
        return listener;
    }

    public void setListener(CallbackListener listener) {
        this.listener = listener;
    }

    public interface CallbackListener{
        void s3UploadSuccess(String result);
        void s3UploadFail(String result);
    }

}
