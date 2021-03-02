package com.project.app.bean;

class RefundReasonBean {
    var orderItemUuid:String = ""
    var orderUuid:String = ""
    var photos :List<String> = listOf()
    var reason :String = ""
    var remarks :String = ""
    var rating:Int = 1 //默认一颗星
    var text:String = ""
    var type = 0
}