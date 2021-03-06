package com.project.app.bean;

import java.util.ArrayList;
import java.util.List;

public class CartItemReqBean {
    String shippingAddressUuid = "";
    String userCouponUuid = "";
    List<Items> items = new ArrayList<>();
    String type = "";
    Double amtCash = 0.0;
    String platform = "";
    String Xplatform = "ANDROID";
    String uuid = "";
    String paymentType = null;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    //注意 x-platform 头部添加ANDROID

    public String getXplatform() {
        return Xplatform;
    }
    public void setXplatform(String xplatform) {
        Xplatform = xplatform;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Double getAmtCash() {
        return amtCash;
    }

    public void setAmtCash(Double amtCash) {
        this.amtCash = amtCash;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShippingAddressUuid() {
        return shippingAddressUuid;
    }

    public void setShippingAddressUuid(String shippingAddressUuid) {
        this.shippingAddressUuid = shippingAddressUuid;
    }

    public String getUserCouponUuid() {
        return userCouponUuid;
    }

    public void setUserCouponUuid(String userCouponUuid) {
        this.userCouponUuid = userCouponUuid;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public static class Items{
        private String skuUuid = "";
        private Integer quantity = 0;
        private String productUuid = "";

        public String getProductUuid() {
            return productUuid;
        }

        public void setProductUuid(String productUuid) {
            this.productUuid = productUuid;
        }

        public String getSkuUuid() {
            return skuUuid;
        }

        public void setSkuUuid(String skuUuid) {
            this.skuUuid = skuUuid;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "Items{" +
                    "skuUuid='" + skuUuid + '\'' +
                    ", quantity=" + quantity +
                    ", productUuid='" + productUuid + '\'' +
                    '}';
        }
    }

}
