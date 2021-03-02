package com.project.app.bean;

import java.util.ArrayList;
import java.util.List;

public class CategoryBean {
    private List<CategoryItem> categories = new ArrayList<>();

    public List<CategoryItem> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryItem> categories) {
        this.categories = categories;
    }

}
