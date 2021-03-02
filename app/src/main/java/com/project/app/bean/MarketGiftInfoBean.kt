package com.project.app.bean;

class MarketGiftInfoBean {
    var completed:Boolean = false
    var applied:Boolean = false
    var coupon:CouponItem = CouponItem()
    
    class CouponItem{
        var status:Int = 0
        var updatedAt:Long = 0
        var overdueTime:Long = 0
        var couponName:String = ""
        var statusDesc:String = ""
    }
}