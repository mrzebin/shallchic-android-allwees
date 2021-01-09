package com.project.app.contract;

import com.hb.basemodel.base.BaseView;
import com.project.app.bean.CategoryBean;

/**
 * 首页分类
 */
public interface HomeGoodsContract {

    interface Model{
        void fetchSpecifyGoodsInfo(String no);
    }

    interface View extends BaseView{
        void fetchSpecifyGi(CategoryBean result);
        void fetchFail(String failReason);
    }

    interface Presenter{

    }


}
