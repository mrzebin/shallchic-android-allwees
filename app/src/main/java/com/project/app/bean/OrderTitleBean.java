package com.project.app.bean;

public class OrderTitleBean {

    String  title;
    String matchTitle;

    public OrderTitleBean(String title, String matchTitle) {
        this.title = title;
        this.matchTitle = matchTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMatchTitle() {
        return matchTitle;
    }

    public void setMatchTitle(String matchTitle) {
        this.matchTitle = matchTitle;
    }
}
