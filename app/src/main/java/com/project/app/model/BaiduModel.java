package com.project.app.model;


import com.project.app.contract.BaiduContract;

public class BaiduModel implements BaiduContract.Model {

    @Override
    public void login(String name, String password, ModelListener listener) {
        listener.complete("请求成功:" + name);
        listener.fail("请求失败:" + password);
    }

}
