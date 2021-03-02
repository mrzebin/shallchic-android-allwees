package com.project.app.utils;

import android.text.TextUtils;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.LoggerUtil;
import com.hb.basemodel.utils.SPManager;
import com.project.app.bean.AwsAccessTokenBean;
import com.project.app.s3transferutility.AwsClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ThreadLoopUtil {
    private static AwsClient mAwsClient;
    private String accessKey;
    private String secretId;
    private String sessionId;
    private String bucketName;
    private String prefixName;
    private String awsRegion;
    public long mFileLength;

    public ThreadLoopUtil(Constant.S3Type type){   //上传的类型
        String awsJson = "";
        if(type == Constant.S3Type.AVR){
            awsJson = SPManager.sGetString(Constant.SP_AWS_ACCESS_TOKEN_AVR);
        }else if(type == Constant.S3Type.REV){
            awsJson = SPManager.sGetString(Constant.SP_AWS_ACCESS_TOKEN_REV);
        }else if(type == Constant.S3Type.RFD){
            awsJson = SPManager.sGetString(Constant.SP_AWS_ACCESS_TOKEN_RFD);
        }else if(type == Constant.S3Type.FDK){
            awsJson = SPManager.sGetString(Constant.SP_AWS_ACCESS_TOKEN_FDK);
        }
        if(!TextUtils.isEmpty(awsJson)){
            AwsAccessTokenBean bean = JsonUtils.deserialize(awsJson, AwsAccessTokenBean.class);
            accessKey   = bean.getAccessKeyId();
            secretId    = bean.getSecretAccessKey();
            sessionId   = bean.getSessionToken();
            bucketName  = bean.getBucketName();
            prefixName  = bean.getKeyPrefix();
            awsRegion   = bean.getRegion();
            mAwsClient = new AwsClient(accessKey,secretId,sessionId,bucketName,awsRegion);
        }
    }

    class LoopThread extends Thread{
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
           String result =  mAwsClient.uploadToS3(mFileLength,mIs,remoteName);
           if(TextUtils.isEmpty(result)){
               LoggerUtil.i("上传失败:" + result);
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
            mFileLength = file.length();
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
