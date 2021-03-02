package com.project.app.bean;

class CartCouponsBaean {
    var msg:String = ""
    var total:Int = 0
    var code:Int = 0
    var data:List<CpItem> = listOf()

    class CpItem{
        var uuid:String = ""
        var no:String = ""
        var couponType:String = ""
        var discountRate:Double = 0.0
        var receiveDate:Long = 0
        var overdueTime:Long = 0
        var maxDeductAmt:Double = 0.0
        var valueText:String = ""
        var name:String = ""
    }

}