package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.TrendingSearchBean;
import com.project.app.contract.SearchGoodsContract;
import com.project.app.model.SearchGoodsModel;

import java.util.List;

public class SearchGoodsPresenter extends BasePresenter<SearchGoodsContract.View> implements SearchGoodsContract.Presenter {
    private SearchGoodsModel model;

    public SearchGoodsPresenter(){
        model = new SearchGoodsModel();
    }

    @Override
    public void trendingSearchs() {
        model.trendingSearchs(new BaseModelResponeListener<List<TrendingSearchBean.SearchItem>>() {
            @Override
            public void onSuccess(List<TrendingSearchBean.SearchItem> data) {
                mView.fetchTrendingSearchSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.fetchTrendingSearchFail(msg);
            }
        });
    }

    @Override
    public void searchGoods(boolean showProcess,int page, int pagesize, String query) {
        if(showProcess){
            mView.startLoading();
        }
        model.searchGoods(page,pagesize,query,new BaseModelResponeListener<ClassifyListBean>() {
            @Override
            public void onSuccess(ClassifyListBean data) {
                mView.stopLoading();
                mView.fetchSearchTargetSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.fetchSearchTargetFail(msg);
            }
        });
    }

}
