package com.project.app.s3;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.Date;

public class AwsS3Cache {
    private final Context context;
    private final String cacheKey = "awsKey";

    public AwsS3Cache(Context context) {
        this.context = context;
    }

    static class AwsS3KeyTtl implements Serializable {
        private AwsS3Key awsS3Key;
        private Date expiredAt;
    }

    public void setCache(AwsS3Code code, AwsS3Key awsS3Key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(cacheKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        AwsS3KeyTtl awsS3KeyTtl = new AwsS3KeyTtl();
        awsS3KeyTtl.awsS3Key = awsS3Key;
        final int cacheKeyMaxTtl = 11 * 60 * 60 * 1000;
        awsS3KeyTtl.expiredAt = new Date(new Date().getTime() + cacheKeyMaxTtl);

        String json = AwsS3Serialize.serializeObject(awsS3KeyTtl);
        editor.putString(code.getCode() + "-" + AwsS3Helper.getHostName(), json);
        editor.apply();
    }

    public AwsS3Key getCache(AwsS3Code code) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(cacheKey, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(code.getCode() + "-" + AwsS3Helper.getHostName(), null);
        if (json == null) {
            return null;
        }

        AwsS3KeyTtl awsS3KeyTtl = (AwsS3KeyTtl) AwsS3Serialize.deSerializeObject(json);
        if (awsS3KeyTtl == null) {
            return null;
        }

        if (awsS3KeyTtl.expiredAt.before(new Date())) {
            return null;
        }

        return awsS3KeyTtl.awsS3Key;
    }
}
