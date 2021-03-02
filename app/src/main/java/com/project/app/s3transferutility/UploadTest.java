package com.project.app.s3transferutility;

import android.os.Environment;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.hb.basemodel.utils.LoggerUtil;
import com.project.app.MyApp;

import java.io.File;

public class UploadTest {
    private final String TAG = "UploadTest";
    /**
     * 上传到AWS
     */
    public void AwsTest() {
            AWSMobileClient.getInstance().initialize(MyApp.mContext, new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i(TAG, "AWSMobileClient initialized. User State is " + userStateDetails.getUserState());
            }
            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Initialization error.", e);
            }
        });
        uploadWithTransferUtility();
    }

    public void uploadWithTransferUtility() {
        String AMAZON_S3_KEY = "ASIAVOIWJRDVECBUZWUL";
        String AMAZON_S3_KEY_PWD = "InCM+SCn2FSg/ZRSe186kRHglz9BQkM6PTgvwVNc";
        String sessionToken = "IQoJb3JpZ2luX2VjEFwaCXVzLXdlc3QtMiJGMEQCICYDRd9wmkuxUf6oPltCFsokU2E2EqY+Iskxs+s937PBAiAU1F5SzBHxYaPhbMyj8xszKzXPixig21k0p0CnyhIJQSquAwjV//////////8BEAAaDDM3NDI0NTc4Nzg4MiIM9S4OEeC2CF3dxjc5KoIDoArSo7rUR5eOWJkh/oU+ktFlvsHH+8iAwUYKVQimnjS0leFSn/Lze0UW+fHWyuyDG7G5s6NEKLJPly4sCpl44hmzhGcihpmNY9bkalVPoaLXxRE87yI6SfOfVJ4l8RJnqpBsdCovAd76f+U65E8nfEiQHKUZL8wQvTaGa96JlN3ebPvbYirgQbW/nqiF1K6mxVo0DJFB5hRYRevBtgMlJBDPwLujuDDLRNHqTDSA9NLG5EkAAx7K6wAYaZHB8jo9vt4AihaqcF1OilIvoBSryM7qWpuX8TLTnt0dfFOJZpOQ/y8hzTqBW/r+dTkzsm6C3iQX0h5JATPzKte0oJL+nCcnomnHT/BWiFeqLPrvk9L82C9ccp+W2TgDz6V0ZZk9BxoS7hVe5X15QTrFGtmnrt0pV/gFIqG047P+l1tLKJLpw7ClX0O+l1Wy2SRHe53m9Rf8V7ilOULQe1RZpw+l++NG3YaKYaMGWVIE2YjdhWmDu1oDu/iRvylup8r8PjQSaL0w9uTz/QU6ngHLBjFNSq5bdopVNYc1fAjRQ4I3FFT3DZvBVzZQM+LzrP6emQ65vGHwjNgHSzSVNJ2HUfIY7MVDHscJaqTP+oa+CnkfBxzQ6kJYvdh7BmsyirbPEFMAMQywULriVGXtIsPYPlbqDUc1SR5Luq40xNltRG6qIrFk2hhcGDPVZ/2sSnJqtpz5tWhyTumnYUXgC149eRnE+IVy2bsy7UiNSA==";
        String IMAGE_DATA_NAME = "rfd/5f1c517ab5a1400f9a8d984890f9931c/20201124/test.jpg";
        String bucketName = "shallchic-test";
        AWSCredentials credentials11 = new BasicSessionCredentials(AMAZON_S3_KEY,AMAZON_S3_KEY_PWD,sessionToken);

        String mImageCropPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/111.png";
        File targetFile = new File(mImageCropPath);
        if(!targetFile.exists()){
            LoggerUtil.i("上传文件不存在");
            return;
        }
        AmazonS3 client = new AmazonS3Client(credentials11);
//        PutObjectRequest request = new PutObjectRequest(bucketName,);
//
//        client.putObject(new PutObjectRequest(BUCKET_NAME, IMAGE_DATA_NAME, stream,metadata).withCannedAcl(CannedAccessControlList.PublicRead))




        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(MyApp.mContext)
                        .defaultBucket("shallchic-test")
                        .s3Client(client)
                        .build();

        TransferObserver uploadObserver =
                transferUtility.upload(
                        IMAGE_DATA_NAME,
                        new File(mImageCropPath));

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                    LoggerUtil.i("AWS上传图片:"+IMAGE_DATA_NAME+"成功");
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;
                Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
                LoggerUtil.i("AWS上传图片:"+IMAGE_DATA_NAME+"失败" + "--" + ex.getMessage());
            }
        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }
    }
}
