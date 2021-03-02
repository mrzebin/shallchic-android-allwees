package com.project.app.bean;

class HistoryPointsBean {
    var current:Int = 0
    var lastPage:Int = 0
    var size:Int = 0
    var total:Int = 0
    var count:Int = 0
    var hasMorePages:Boolean = false
    var results:List<HistoryPointsItem>   = listOf()

    class HistoryPointsItem{
        var uuid:String = ""
        var status:Int = 0
        var createdAt:Long = 0
        var updatedAt:Long = 0
        var idx:Int = 0
        var pointUuid:String = ""
        var userUuid:String = ""
        var logKey:String = ""
        var useType:String = ""
        var value:Double = 0.0
        var statusDesc:String = ""
    }
}