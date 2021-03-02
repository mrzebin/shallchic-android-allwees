package com.project.app.model;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.http.OkHttpUtils;
import com.project.app.contract.GoodsSizeChartContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GoodsSizeChartModel implements GoodsSizeChartContract.Model {

    public GoodsSizeChartModel() {

    }

    @Override
    public void fetchGoodsSizeChart(String categoryNo, String productNo, BaseModelResponeListener listener) {
        String requestType = "0";
        String url = BaseUrlConfig.getRootHost() + UrlConfig.FETCH_GOODS_SIZE_CHART_URL;
        HashMap<String,String> params = new HashMap<>();
        params.put("categoryNo",categoryNo);
        params.put("productUuid",productNo);

        OkHttpUtils.get(url,requestType,params,new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    int code = 0;
                    String msg = "";
                    JSONObject responseEntry = new JSONObject(response);
                    code = responseEntry.getInt("code");
                    if(code == 1){
                        listener.onSuccess(responseEntry.getString("data"));
                    }else{
                        listener.onFail(responseEntry.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}
