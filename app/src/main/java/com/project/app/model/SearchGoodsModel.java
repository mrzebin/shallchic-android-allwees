package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.hb.basemodel.utils.DataUtil;
import com.project.app.base.BaseObjectBean;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.TrendingSearchBean;
import com.project.app.contract.SearchGoodsContract;

import java.util.HashMap;
import java.util.List;

public class SearchGoodsModel implements SearchGoodsContract.Model {

    public SearchGoodsModel() {

    }

    @Override
    public void trendingSearchs(BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.SEARCH_TRENDING_ITEMS_URL;
        HashMap<String,String> params = new HashMap<>();

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<List<TrendingSearchBean.SearchItem>>>() {
            @Override
            public void onSuccess(BaseObjectBean<List<TrendingSearchBean.SearchItem>> response){
                if(response.getCode() == 1){
                    if(DataUtil.idNotNull(response.getData())){
                        listener.onSuccess(response.getData());
                    }else{
                        listener.onFail(response.getMsg());
                    }
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {
//                listener.onFail(e.getMessage());
            }
        });
    }

    @Override
    public void searchGoods(int page, int pagesize, String query, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.SEARCH_GOODS_RESULT_URL;
        HashMap<String,String> params = new HashMap<>();
        params.put("current",String.valueOf(page));
        params.put("size",String.valueOf(pagesize));
        params.put("q",query);

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<BaseObjectBean<ClassifyListBean>>() {
            @Override
            public void onSuccess(BaseObjectBean<ClassifyListBean> response){
                if(response.getCode() == 1){
                    listener.onSuccess(response.getData());
                }else{
                    listener.onFail(response.getMsg());
                }
            }
            @Override
            public void onFailure(Exception e) {
//                listener.onFail(e.getMessage());
            }
        });
    }
}
