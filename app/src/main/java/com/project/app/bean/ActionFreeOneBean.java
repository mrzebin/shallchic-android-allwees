package com.project.app.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 买一送一活动
 */
public class ActionFreeOneBean {
    private String buyAndFreeUrl = "";
    private ActionFreeOneMainBean results = new ActionFreeOneMainBean();

    public String getBuyAndFreeUrl() {
        return buyAndFreeUrl;
    }

    public void setBuyAndFreeUrl(String buyAndFreeUrl) {
        this.buyAndFreeUrl = buyAndFreeUrl;
    }

    public ActionFreeOneMainBean getResults() {
        return results;
    }

    public void setResults(ActionFreeOneMainBean results) {
        this.results = results;
    }

    public static class ClassifyItem{
        String uuid = "";
        private int status = 0;
        private String name = "";
        private String mainPhoto = "";
        private String currency = "";
        private int salesTotal = 0;
        private int marketingType = 0;
        private double priceOrigin = 0.0;
        private double priceRetail = 0.0;
        private double priceShip = 0.0;
        private int displayAs = 0;
        private String statusDesc = "" ;
        private String discountOff = "";

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getDiscountOff() {
            return discountOff;
        }

        public void setDiscountOff(String discountOff) {
            this.discountOff = discountOff;
        }

        public double getPriceOrigin() {
            return priceOrigin;
        }

        public void setPriceOrigin(double priceOrigin) {
            this.priceOrigin = priceOrigin;
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

        public int getSalesTotal() {
            return salesTotal;
        }

        public void setSalesTotal(int salesTotal) {
            this.salesTotal = salesTotal;
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

        public void setMarketingType(int marketingType) {
            this.marketingType = marketingType;
        }

        public int getMarketingType() {
            return marketingType;
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
    }

    public class ActionFreeOneMainBean{
        private int current = 0;
        private int lastPage = 0;
        private int size = 0;
        private int total = 0;
        private int count = 0;
        private boolean hasMorePages = false;
        private List<ClassifyItem> results = new ArrayList<>();

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isHasMorePages() {
            return hasMorePages;
        }

        public void setHasMorePages(boolean hasMorePages) {
            this.hasMorePages = hasMorePages;
        }

        public List<ClassifyItem> getResults() {
            return results;
        }

        public void setResults(List<ClassifyItem> results) {
            this.results = results;
        }
    }



}
