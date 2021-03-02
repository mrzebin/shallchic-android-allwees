package com.project.app.bean;

public class CatalogFunctions {
    private String functionName = "";

    public CatalogFunctions(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
}
