package com.project.app.bean;

public class DailySignInBean{
    var signedIn:Boolean = false
    var signedTimes:Int = 0
    var completedAt:Long = 0
    var userCoupon:UserCouponItem = UserCouponItem()

    class UserCouponItem{
        var uuid:String = ""
        var status:Int = 0
        var createdAt:Long = 0
        var updatedAt:Long = 0
        var idx:Int = 0
        var no:String = ""
        var userUuid:String = ""
        var receiveDate:Long = 0
        var overdueTime:Long = 0
        var couponUuid:String = ""
        var couponName:String = ""
        var couponUseType:String = ""
        var couponType:String = ""
        var faceValue:Double = 0.0
        var maxDeductAmt:Double = 0.0
        var valueText:String = ""
        var statusDesc:String = ""
    }

}