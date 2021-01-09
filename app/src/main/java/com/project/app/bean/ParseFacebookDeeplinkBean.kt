package com.project.app.bean

class ParseFacebookDeeplinkBean {
    var target_url:String = ""
    var extras:FacebookExtrasBean ?=null
    var referer_app_link:FacebookRefreAppBean ?= null

    class FacebookExtrasBean{
        var sc_deeplink:String = ""
    }

    class FacebookRefreAppBean{
        var app_name:String = ""
    }

}