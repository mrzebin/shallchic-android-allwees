package com.project.app.utils

import java.text.DecimalFormat
import kotlin.math.ceil

/**
 * create by hb
 */
class MathUtil{

    companion object{
        fun getDoubleNum(num:Double):String{
            val format:DecimalFormat  = DecimalFormat("###.00")   //保留俩位小数
            return format.format(num)
        }

        fun formatPrice(price:Double):String{
            var symbolCell = LocaleUtil.getInstance().symbole
            var symbolTag   = price.toString()
            if(symbolTag.length >6){
                symbolTag = symbolTag.substring(0,6);
            }
            return symbolCell + symbolTag;
        }

        fun formatBuyPrice(price:Double,unit:String):String{
            val df:DecimalFormat = DecimalFormat("0.00")
            val result:String = unit + df.format(price)
            return result
        }

        fun dimGradeDigit(num:Int):String{
            var cellNum:String = "0"
            if(num == 0){
                cellNum = "10+"
            }else if(num <= 99){
                cellNum = (ceil(((num/10).toDouble())).toInt() * 10).toString() + "+"
            }else if(num <999){
                cellNum = (ceil(((num/100).toDouble())).toInt() * 100).toString() + "+"
            }else if(num <9999){
                cellNum = (ceil(((num/1000).toDouble())).toInt() * 1000).toString() + "+"
            }else if(num < 99999){
                cellNum = (ceil(((num/10000).toDouble())).toInt() * 10000).toString() + "+"
            }else{
                cellNum = "10w+"
            }
            return cellNum
        }
    }
}