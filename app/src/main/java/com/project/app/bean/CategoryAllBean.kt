package com.project.app.bean;

class CategoryAllBean {
    var categories = mutableListOf<CategoryItem>()

    class CategoryItem{
        var no = ""
        var type = 0
        var level = 0
        var name = ""
        var children = mutableListOf<CategoryChildItem>()
    }

    class CategoryChildItem{
        var no = ""
        var type = 0
        var level = 0
        var name = ""
        var children = mutableListOf<CategoryThireLevelChildItem>()
    }

    class CategoryThireLevelChildItem{
        var no = ""
        var img  = ""
        var type = 0
        var level = 0
        var name = ""
        var chartUuid = ""
        var children = mutableListOf<CategoryChildItem>()
    }


}