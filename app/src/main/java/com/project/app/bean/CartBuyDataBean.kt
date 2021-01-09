package com.project.app.bean

class CartBuyDataBean {
    var uuid:String = ""
    var userUuid:String = ""
    var promoCode:String = ""
    var sum:Int = 0
    var userAmtCash:Double = 0.0
    var showCashBalance:Double = 0.0
    var amtCash:Double = 0.0
    var amt:Double = 0.0
    var deduction:Double = 0.0
    var originalAmt:Double = 0.0
    var originalAmtProduct:Double = 0.0
    var amtProduct:Double = 0.0
    var amtShipping:Double = 0.0
    var amtProductCoupon:Double = 0.0
    var originalAmtShipping:Double = 0.0
    var amtSaleTax:Double = 0.0
    var amtShippingInsurance:Double = 0.0
    var showSaleTax:Boolean = false
    var showShippingInsurance:Boolean = false
    var showCanGetOneFree:Boolean = false
    var deductMore:Double = 0.0
    var discountRate:Double = 0.0
    var items:ArrayList<ProductItem> = ArrayList()

    class ProductItem{
        lateinit var product:Product
        lateinit var sku:Sku
        var quantity:Int = 0
        var amt:Double = 0.0
        var amtSku:Double = 0.0
        var originalAmtSku:Double = 0.0
        var amtShipping:Double = 0.0
        var productAmtSkuAfterAdd:Double = 0.0
        var amtCashSku:Double = 0.0
        var amtCashProductSku:Double = 0.0
        var amtCashShippingSku:Double = 0.0
        var free:Boolean = false
    }

    class Product{
        var uuid:String = ""
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
        var marketingType:Int = 0
        var photos:List<String> = listOf()
        var displayAs:Int = 0
        var statusDesc:String = ""
    }

    class Sku{
        var uuid:String = ""
        var no:String = ""
        var productUuid:String = ""
        var color:String = ""
        var size:String = ""
        var originalPrice:Double = 0.0
        var retailPrice:Double = 0.0
        var currency:String = ""
        var quantity:String = ""
        var shippingPrice:Double =0.0
        var shippingCurrency:String = ""
        var priceOrigin:Double = 0.0
        var priceRetail:Double = 0.0
        var priceShip:Double = 0.0
        var statusDesc:String = ""
    }
}