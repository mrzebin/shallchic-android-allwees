package com.project.app.contract;

public interface HttpResultListener {
    void complte(String result);
    void fail(String result);
}
