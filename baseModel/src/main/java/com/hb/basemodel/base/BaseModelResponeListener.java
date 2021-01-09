package com.hb.basemodel.base;

public interface BaseModelResponeListener<T> {
    void onSuccess(T data);
    void onFail(String msg);
}
