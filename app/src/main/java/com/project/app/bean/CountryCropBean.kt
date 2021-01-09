package com.project.app.bean

class CountryCropBean {
   var imags:String = ""
   var countries:List<CountryItem> = listOf()

    class CountryItem{
        var nameCn:String = ""
        var nameEn:String = ""
        var rowNum:String = ""
        var colNum:String = ""
        var region:String = ""
        var isSelect:Boolean = false
        var phoneAreaCode:String = ""  //国家手机区号
    }
}