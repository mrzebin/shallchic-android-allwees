package com.project.app.bean;

class OrderBean {
    var current:Int = 0
    var lastPage:Int = 0
    var size:Int = 0
    var total:Int = 0
    var count:Int = 0
    var hasMorePages:Boolean = false
    var results:List<OrderResult> = listOf()

    class OrderResult{
        var uuid:String = ""
        var no:String = ""
        var userUuid:String = ""
        var type:Int=0
        var sum:Int = 0
        var amt:Double = 0.0
        var amtProduct:Double = 0.0
        var amtProductCoupon:Double = 0.0
        var platformAmt:Double = 0.0
        var amtShipping:Double = 0.0
        var currency:String = ""
        var state:String = ""
        var shippingAddressUuid:String = ""
        lateinit var shippingAddress:OrderItemAddress
        var paymentExpiredAt:Long = 0
        var payed:Boolean = false
        var createdAt:Long = 0
        var receiveEndDays:Int = 0
        var items:List<OrderItems> = listOf()
        var cancelAble:Boolean = false
        var payAble:Boolean = false
        var receiveAble:Boolean =false
        var amtSaleTax:Double = 0.0
        var originalAmtSaleTax:Double = 0.0

        var amtSaleTaxAfterAdd:Double = 0.0
        var originalAmtShipping:Double = 0.0
        var originalAmtProduct:Double = 0.0
        var amtProductAfterAdd:Double = 0.0
        var amtShippingAfterAdd:Double = 0.0
        var mainPhoto:String = ""
        var amtPayedOfCoupon:Boolean = false
        var downTime:Long = 0
        var stateDesc:String = ""
    }

    class OrderItemAddress{
        var firstName:String = ""
        var lastName:String = ""
        var itu:String = ""
        var phone:String = ""
        var country:String = ""
        var province:String = ""
        var city:String = ""
        var zipCode:String = ""
        var addressLine1:String = ""
        var addressLine2:String = ""
    }

    class OrderItems{
        var orderUuid:String = ""
        var uuid:String = ""
        var productUuid:String = ""
        var skuUuid:String = ""
        var product:Product = Product()
        var sku:Sku = Sku()
        var reviewAble:Boolean = false
        var refundAble:Boolean = false
        var receiveAble:Boolean = false
        var cancelAble:Boolean  = false
        var quantity:Int = 0
        var price:Double = 0.0
        var amt:Double = 0.0
        var amtSku:Double = 0.0
        var amtShipping:Double = 0.0
        var delivered:Boolean = false
        var shippingAmtSkuAfterAdd:Double = 0.0
        var productAmtSkuAfterAdd:Double = 0.0
        var platformAmtSku:Double = 0.0
        var amtCashSku:Double = 0.0
        var refundStateDesc:String = ""
        var patyStatus:String = ""
        var createT:Long = 0
        var no:String = ""
        var stateDesc:String = ""
    }

    class Product{
        var uuid:String = ""
        var name:String = ""
        var mainPhoto:String = ""
        var description:String = ""
        var shippingPrice:Double= 0.0
        var shippingCurrency:String = ""
        var shippingArrivalDesc:String = ""
        var freeGift:Boolean = false
    }

    class Sku{
        var uuid:String = ""
        var no:String = ""
        var color:String = ""
        var size:String = ""
        var photos:List<String> = listOf()
        var originalPrice:Double = 0.0
        var retailPrice:Double = 0.0
        var currency:String = ""
        var quantity:Int = 0
    }
}