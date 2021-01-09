package com.project.app.bean

class RedeemBean {
    var msg:String = ""
    var total:Int = 0
    var code:Int = 0
    var data:List<RedeemItem> = arrayListOf()

    class RedeemItem{
        var uuid:String = ""
        var status:Int = 0
        var createdAt:Long = 0
        var updatedAt:Long = 0
        var idx:Int = 0
        var name:String = ""
        var type:String = ""
        var faceValue:Double = 0.0
        var overdueType:String = ""
        var overdueValue:Double = 0.0
        var overdueValueUnit:String = ""
        var count:Double = 0.0
        var useType:String = ""
        var singleUserReceiveMaxCount:Double = 0.0
        var receivedCount:Double = 0.0
        var maxDeductAmt:Double = 0.0
        var valueText:String = ""
        var pointDeductAmt:Double = 0.0
        var pointExchange:Boolean = false
        var reserve:Double = 0.0
        var statusDesc:String = ""
    }

}