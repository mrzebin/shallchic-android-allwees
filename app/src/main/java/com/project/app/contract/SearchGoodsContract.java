package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.TrendingSearchBean;

import java.util.List;

public interface SearchGoodsContract {

    interface Model{
        void trendingSearchs(BaseModelResponeListener listener);
        void searchGoods(int page,int pagesize,String query,BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void fetchTrendingSearchSuccess(List<TrendingSearchBean.SearchItem> result);
        void fetchTrendingSearchFail(String result);
        void fetchSearchTargetSuccess(ClassifyListBean result);
        void fetchSearchTargetFail(String result);
    }

    interface Presenter{
        void trendingSearchs();
        void searchGoods(boolean searchGoods,int page,int pagesize,String query);
    }

}
