package com.project.app.utils;

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(String error);
}
