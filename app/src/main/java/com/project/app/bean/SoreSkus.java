package com.project.app.bean;

import java.util.Comparator;

/**
 * 排序的类
 */
public class SoreSkus implements Comparator<GoodsDetailInfoBean.SkusItem>{

    @Override
    public int compare(GoodsDetailInfoBean.SkusItem skusItem, GoodsDetailInfoBean.SkusItem t1) {
        return (int) (skusItem.priceRetail - t1.priceRetail);
    }

}
