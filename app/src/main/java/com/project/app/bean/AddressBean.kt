package com.project.app.bean

class AddressBean {
    var current:Int = 0
    var lastPage:Int = 0
    var size:Int = 0
    var total:Int = 0
    var count:Int = 0
    var hasMorePages:Boolean = false
    var results:List<com.project.app.bean.AddressBean.AddressItem> = listOf()

    class AddressItem{
        var uuid:String = ""
        var userUuid:String = ""
        var isSelect:Boolean  = false
        var isDefault:Boolean = false
        var firstName:String = ""
        var lastName:String = ""
        var itu:String = ""
        var phone:String = ""
        var country:String = ""
        var province:String = ""
        var provinceRegion:String = ""
        var city:String = ""
        var region:String = ""
        var zipCode:String = ""
        var addressLine1:String = ""
        var addressLine2:String = ""
        var note:String = ""
        var street:String = ""
        var createdAt:Long = 0
        var updatedAt:Long = 0
    }
}