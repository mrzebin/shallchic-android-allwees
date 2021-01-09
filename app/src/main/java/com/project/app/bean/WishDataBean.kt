package com.project.app.bean

class WishDataBean {
    var current:Int = 0
    var lastPage:Int = 0
    var size:Int = 0
    var total:Int = 0
    var count:Int = 0
    var hasMorePages:Boolean = false
    var results:List<ProductDtoWrapp> = listOf()

    class ProductDtoWrapp{
        var productDto:ProductDto ?= null
        var createdAt:String = ""
    }

    class ProductDto{
        var uuid:String = ""
        var status:Int = 0
        var retailerUuid:String = ""
        var storeUuid:String = ""
        var no:String = ""
        var categoryNo:String = ""
        var name:String = ""
        var mainPhoto:String = ""
        var originalPrice:Double = 0.0
        var retailPrice:Double = 0.0
        var shippingPrice:Double = 0.0
        var currency:String = ""
        var shippingArrivalDesc:String = ""
        var shippingMinDays:Int = 0
        var shippingMaxDays:Int = 0
        var rating:Double = 0.0
        var totalSales:Int = 0
        var salesTotal:Int = 0
        var totalCollections:Int = 0
        var totalReviews:Int = 0
        var totalShares:Int = 0
        var totalViews:Int = 0
        var publishAt:String = ""
        var priceOrigin:Double = 0.0
        var priceRetail:Double = 0.0
        var priceShip:Double = 0.0
        var marketingType:Int = 0
        var discountOff:String = ""
        var photos: Array<String> = arrayOf()
        var displayAs:Int = 0
        var userIsCollection:Boolean = false
        var statusDesc:String = ""
    }
}