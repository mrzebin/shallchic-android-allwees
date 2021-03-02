package com.project.app.bean;

/**
 *
 */
class HomePopularActionBean {
    var icon:String = ""
    var title:String = ""
    var appLink:String = ""

    var items:List<ActivityAreaItem> = listOf()

    class ActivityAreaItem{
        var uuid = ""
        var status:Int = 0
        var name:String = ""
        var mainPhoto:String = ""
        var currency:String = ""
        var salesTotal:Int = 0
        var marketingType:Int = 0
        var priceOrigin:Double = 0.0
        var priceRetail:Double = 0.0
        var priceShip:Double = 0.0
        var discountOff:String = ""
        var statusDesc:String = ""
        var displayAs:Int = 0
    }
}