package com.project.app.utils;

import android.text.TextUtils;

import com.project.app.bean.CategoryBean;
import com.project.app.bean.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class FilterDataUtils {

    public static CategoryBean filterData(CategoryBean cells){
        List<CategoryItem> seeds = new ArrayList<>();
        for(CategoryItem item :cells.getCategories()){
            if(!TextUtils.isEmpty(item.getName())){
                if(item.getType() != 1){
                    continue;
                }
                seeds.add(item);
            }
        }
        cells.setCategories(seeds);
        return cells;
    }

}
