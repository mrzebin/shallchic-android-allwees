package com.project.app.bean

/**
 * 首页闪购对象
 */
class HomeFLashSaleBean {
    var current:Int = 0
    var lastPage:Int = 0
    var size:Int = 0
    var total:Int = 0
    var count:Int = 0
    var hasMorePages:Boolean = false
    var remainMillis:Long = 0
    var results:List<FlashSaleBean>  = listOf()

    class FlashSaleBean{
        var id:String = ""
        var uuid:String = ""
        var status:Int = 0
        var name:String = ""
        var mainPhoto:String = ""
        var salesTotal:Int = 0
        var marketingType:Int = 0
        var currency:String = ""
        var statusDesc:String = ""
        var displayAs:Int = 1
        var discountOff:String = ""
        var priceOrigin:Double = 0.0
        var priceRetail:Double = 0.0
        var priceShip:Double = 0.0
    }

}