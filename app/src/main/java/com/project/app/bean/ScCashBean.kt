package com.project.app.bean

class ScCashBean {
    var current:Int = 0
    var lastPage:Int = 0
    var size:Int = 0
    var total:Double = 0.0
    var count:Int = 0
    var hasMorePages:Boolean = false
    var results:List<CashItem> = arrayListOf()

    class CashItem{
        var uuid:String = ""
        var status:Int = 0
        var createdAt:Long = 0
        var idx:Int = 0
        var userUuid:String = ""
        var logType:String = ""
        var value:Double = 0.0
        var relateKey:String = ""
        var statusDesc:String = ""
    }
}