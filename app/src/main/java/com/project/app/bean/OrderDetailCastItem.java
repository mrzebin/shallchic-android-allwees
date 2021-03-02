package com.project.app.bean;

/**
 * 订单详情的价格
 */
public class OrderDetailCastItem {
    public String tollType = "";  //收费的类型
    public String virtualCash;    //虚拟的价格
    public int colorType;      //0为黑色 1为系统颜色 2为红色
    public boolean showLine = true;

    public OrderDetailCastItem(String tollType, String virtualCash,int colorType) {
        this.tollType = tollType;
        this.virtualCash = virtualCash;
        this.colorType   = colorType;
        this.showLine    = showLine;
    }

    public OrderDetailCastItem(String tollType, String virtualCash,int colorType,boolean showLine) {
        this.tollType = tollType;
        this.virtualCash = virtualCash;
        this.colorType   = colorType;
        this.showLine    = showLine;
    }
}
