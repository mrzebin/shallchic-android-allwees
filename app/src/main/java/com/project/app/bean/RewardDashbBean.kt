package com.project.app.bean

class RewardDashbBean {
    var current:Int = 0
    var lastPage:Int = 0
    var size:Int = 0
    var total:Int = 0
    var count:Int = 0
    var hasMorePages:Boolean = false
    var results:List<CouponItem> = listOf()

    class CouponItem{
        var uuid:String = ""
        var no:String = ""
        var couponType:String = ""
        var discountRate:Double = 0.0
        var receiveDate:Long = 0
        var overdueTime:Long = 0
        var maxDeductAmt:Int = 0
        var valueText:String = ""
        var name:String = ""
    }

}