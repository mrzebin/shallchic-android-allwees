package com.project.app.base;

import android.content.Context;

/**
 * 首页
 */
public class BaseController<P extends BasePresenter> extends BaseMvpController {
    public P mPresenter;
    public BaseController(Context context) {
        super(context);
    }
}