package com.project.app.bean;

/**
 * 语言网络环境
 */
class AppLocaleBean {
    var country:CountryBean ?= null
    var currency:CurrencyBean ?= null
    var ip:String = ""
    var host:String = ""
    var cdn:String = ""

    class CountryBean{
        var code:String =""
        var language:String = ""
        var nameEn:String = ""
        var region:String = ""
    }

    class CurrencyBean{
        var symbol:String = ""
        var rate:String = ""
        var abbr:String = ""
    }
}