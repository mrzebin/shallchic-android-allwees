package com.project.app.bean

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
        var language:String = ""
        var nameEn:String = ""
        var region:String = ""
        var isArab:Boolean = false
    }

    class CurrencyBean{
        var symbol:String = ""
        var abbr:String = ""
    }
}