package com.project.app.bean;

/**
 * 订单详情的价格
 */
public class PaymentCastBean {
   public  String castType;
   public String castPrice;
   public int fontStyle = 0;   //0 为正常 1为加了横杠
   public int fontColorType = 0;  //0为正常 1为蓝色 2为红色
   public String attachTag;

    public PaymentCastBean(String castType,String castPrice,int fontColorType){
        this.castType = castType;
        this.castPrice = castPrice;
        this.fontColorType = fontColorType;
    }

   public PaymentCastBean(String castType,String castPrice,int fontColorType,int fontStyle){
        this.castType = castType;
        this.castPrice = castPrice;
        this.fontStyle  = fontStyle;
        this.fontColorType = fontColorType;
   }

    public PaymentCastBean(String castType,String castPrice,int fontColorType,int fontStyle,String attachTag){
        this.castType = castType;
        this.castPrice = castPrice;
        this.fontStyle  = fontStyle;
        this.fontColorType = fontColorType;
        this.attachTag     = attachTag;
    }


}
