package com.project.app.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoodsDetailInfoBean {
    String uuid = "";
    int status = 0;
    String retailerUuid = "";
    String storeUuid = "";
    String no = "";
    String categoryNo = "";
    String name = "";
    String keywords = "";
    String description = "";
    String mainPhoto = "";
    double originalPrice = 0.0;
    double retailPrice = 0.0;
    double shippingPrice = 0.0;
    String currency = "";
    String shippingArrivalDesc = "";
    int shippingMinDays= 0;
    int shippingMaxDays = 0;
    String rating = "";
    int totalSales = 0;
    int salesTotal = 0;
    int totalCollections = 0;
    int totalReviews = 0;
    int totalShares = 0;
    int totalViews = 0;
    String publishAt = "";
    double priceOrigin = 0.0;
    double priceRetail = 0.0;
    double priceShip = 0.0;
    int marketingType = 0;
    String discountOff ="";
    String[] photos = new String[]{};
    int displayAs = 0;
    Skus skus = new Skus();
    double minRetailPrice = 0.0;
    double maxRetailPrice = 0.0;
    double minOriginalPrice = 0.0;
    double maxOriginalPrice = 0.0;
    String statusDesc = "";
    boolean userIsCollection = false;

    public boolean isUserIsCollection() {
        return userIsCollection;
    }

    public void setUserIsCollection(boolean userIsCollection) {
        this.userIsCollection = userIsCollection;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Skus getSkus() {
        return skus;
    }

    public void setSkus(Skus skus) {
        this.skus = skus;
    }

    public double getMinRetailPrice() {
        return minRetailPrice;
    }

    public void setMinRetailPrice(double minRetailPrice) {
        this.minRetailPrice = minRetailPrice;
    }

    public double getMaxRetailPrice() {
        return maxRetailPrice;
    }

    public void setMaxRetailPrice(double maxRetailPrice) {
        this.maxRetailPrice = maxRetailPrice;
    }

    public double getMinOriginalPrice() {
        return minOriginalPrice;
    }

    public void setMinOriginalPrice(double minOriginalPrice) {
        this.minOriginalPrice = minOriginalPrice;
    }

    public double getMaxOriginalPrice() {
        return maxOriginalPrice;
    }

    public void setMaxOriginalPrice(double maxOriginalPrice) {
        this.maxOriginalPrice = maxOriginalPrice;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRetailerUuid() {
        return retailerUuid;
    }

    public void setRetailerUuid(String retailerUuid) {
        this.retailerUuid = retailerUuid;
    }

    public String getStoreUuid() {
        return storeUuid;
    }

    public void setStoreUuid(String storeUuid) {
        this.storeUuid = storeUuid;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(String categoryNo) {
        this.categoryNo = categoryNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getShippingArrivalDesc() {
        return shippingArrivalDesc;
    }

    public void setShippingArrivalDesc(String shippingArrivalDesc) {
        this.shippingArrivalDesc = shippingArrivalDesc;
    }

    public int getShippingMinDays() {
        return shippingMinDays;
    }

    public void setShippingMinDays(int shippingMinDays) {
        this.shippingMinDays = shippingMinDays;
    }

    public int getShippingMaxDays() {
        return shippingMaxDays;
    }

    public void setShippingMaxDays(int shippingMaxDays) {
        this.shippingMaxDays = shippingMaxDays;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public int getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(int salesTotal) {
        this.salesTotal = salesTotal;
    }

    public int getTotalCollections() {
        return totalCollections;
    }

    public void setTotalCollections(int totalCollections) {
        this.totalCollections = totalCollections;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public int getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(int totalShares) {
        this.totalShares = totalShares;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(int totalViews) {
        this.totalViews = totalViews;
    }

    public String getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(String publishAt) {
        this.publishAt = publishAt;
    }

    public double getPriceOrigin() {
        return priceOrigin;
    }

    public void setPriceOrigin(double priceOrigin) {
        this.priceOrigin = priceOrigin;
    }

    public double getPriceRetail() {
        return priceRetail;
    }

    public void setPriceRetail(double priceRetail) {
        this.priceRetail = priceRetail;
    }

    public double getPriceShip() {
        return priceShip;
    }

    public void setPriceShip(double priceShip) {
        this.priceShip = priceShip;
    }

    public int getMarketingType() {
        return marketingType;
    }

    public void setMarketingType(int marketingType) {
        this.marketingType = marketingType;
    }

    public String getDiscountOff() {
        return discountOff;
    }

    public void setDiscountOff(String discountOff) {
        this.discountOff = discountOff;
    }

    public String[] getPhotos() {
        return photos;
    }

    public void setPhotos(String[] photos) {
        this.photos = photos;
    }

    public int getDisplayAs() {
        return displayAs;
    }

    public void setDisplayAs(int displayAs) {
        this.displayAs = displayAs;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public static class Skus{
        String[] color = new String[]{};
        String[] size = new String[]{};
        List<SkusItem> skus = new ArrayList<>();

        public String[] getColor() {
            return color;
        }

        public void setColor(String[] color) {
            this.color = color;
        }

        public String[] getSize() {
            return size;
        }

        public void setSize(String[] size) {
            this.size = size;
        }

        public List<SkusItem> getSkus() {
            return skus;
        }

        public void setSkus(List<SkusItem> skus) {
            this.skus = skus;
        }
    }

    public static class SkusItem{
        String uuid = "";
        int status = 0;
        String createdAt = "";
        String updatedAt = "";
        String no = "";
        String productUuid ="";
        String color = "";
        String size = "";
        double originalPrice = 0.0;
        double retailPrice = 0.0;
        String currency = "";
        int quantity = 0;
        double shippingPrice = 0.0;
        String shippingCurrency = "";
        double priceOrigin = 0.0;
        double priceRetail = 0.0;
        double priceShip = 0.0;
        String[] photos = new String[]{};
        String statusDesc = "";

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getProductUuid() {
            return productUuid;
        }

        public void setProductUuid(String productUuid) {
            this.productUuid = productUuid;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public double getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(double originalPrice) {
            this.originalPrice = originalPrice;
        }

        public double getRetailPrice() {
            return retailPrice;
        }

        public void setRetailPrice(double retailPrice) {
            this.retailPrice = retailPrice;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getShippingPrice() {
            return shippingPrice;
        }

        public void setShippingPrice(double shippingPrice) {
            this.shippingPrice = shippingPrice;
        }

        public String getShippingCurrency() {
            return shippingCurrency;
        }

        public void setShippingCurrency(String shippingCurrency) {
            this.shippingCurrency = shippingCurrency;
        }

        public double getPriceOrigin() {
            return priceOrigin;
        }

        public void setPriceOrigin(double priceOrigin) {
            this.priceOrigin = priceOrigin;
        }

        public double getPriceRetail() {
            return priceRetail;
        }

        public void setPriceRetail(double priceRetail) {
            this.priceRetail = priceRetail;
        }

        public double getPriceShip() {
            return priceShip;
        }

        public void setPriceShip(double priceShip) {
            this.priceShip = priceShip;
        }

        public String[] getPhotos() {
            return photos;
        }

        public void setPhotos(String[] photos) {
            this.photos = photos;
        }

        public String getStatusDesc() {
            return statusDesc;
        }

        public void setStatusDesc(String statusDesc) {
            this.statusDesc = statusDesc;
        }

        @Override
        public String toString() {
            return "SkusItem{" +
                    "uuid='" + uuid + '\'' +
                    ", status=" + status +
                    ", createdAt='" + createdAt + '\'' +
                    ", updatedAt='" + updatedAt + '\'' +
                    ", no='" + no + '\'' +
                    ", productUuid='" + productUuid + '\'' +
                    ", color='" + color + '\'' +
                    ", size='" + size + '\'' +
                    ", originalPrice=" + originalPrice +
                    ", retailPrice=" + retailPrice +
                    ", currency='" + currency + '\'' +
                    ", quantity=" + quantity +
                    ", shippingPrice=" + shippingPrice +
                    ", shippingCurrency='" + shippingCurrency + '\'' +
                    ", priceOrigin=" + priceOrigin +
                    ", priceRetail=" + priceRetail +
                    ", priceShip=" + priceShip +
                    ", photos=" + Arrays.toString(photos) +
                    ", statusDesc='" + statusDesc + '\'' +
                    '}';
        }
    }



}
