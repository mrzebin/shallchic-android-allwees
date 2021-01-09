package com.project.app.s3;

import java.io.Serializable;

public class AwsS3Key implements Serializable {
    private String accessKeyId;
    private String accessSecretKey;
    private String region;
    private String sessionToken;
    private String bashPath;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessSecretKey() {
        return accessSecretKey;
    }

    public void setAccessSecretKey(String accessSecretKey) {
        this.accessSecretKey = accessSecretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getBashPath() {
        return bashPath;
    }

    public void setBashPath(String bashPath) {
        this.bashPath = bashPath;
    }
}
