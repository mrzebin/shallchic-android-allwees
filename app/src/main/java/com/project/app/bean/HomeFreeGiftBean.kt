package com.project.app.bean

class HomeFreeGiftBean {
    var current:Int = 0
    var lastPage:Int = 0
    var size:Int = 0
    var total:Int = 0
    var count:Int = 0
    var hasMorePages:Boolean = false
    var results:List<GiftItem>  = listOf()

    class GiftItem{
        var uuid:String = ""
        var status:Int = 0
        var name:String = ""
        var mainPhoto:String = ""
        var salesTotal:Double = 0.0
        var priceOrigin:Double = 0.0
        var priceRetail:Double = 0.0
        var priceShip:Double = 0.0
        var marketingType:Int = 0
        var discountOff:String = ""
        var displayAs:Int = 0
        var statusDesc:String = ""
        var isSelected:Boolean = false
    }

}