package com.project.app.bean

class LogisticTrackBean {
    var code:Int = 0
    var data:List<LogisticTrackItem> = listOf()
    var deliverDate:Long = 0
    var delivered:Boolean = false
    var trackingNumber:String = ""

    class LogisticTrackItem{
        var checkpoint_status:String = ""
        var Details:String = ""
        var substatus:String = ""
        var StatusDescription:String = ""
        var Date:String = ""
        var checkTimeStamp:Long = 0
    }

}