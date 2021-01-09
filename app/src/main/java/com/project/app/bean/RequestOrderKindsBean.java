package com.project.app.bean;

public class RequestOrderKindsBean {
    int current;
    String queryType;
    int size;

    public RequestOrderKindsBean(){

    }

    public RequestOrderKindsBean(int current, String queryType, int size) {
        this.current = current;
        this.queryType = queryType;
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
