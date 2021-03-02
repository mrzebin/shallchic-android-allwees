package com.project.app.utils

import com.hb.basemodel.utils.LoggerUtil
import java.util.*

object CaldenarUtil {

    fun getCurrentMonth():Int{
        var cInstance:Calendar = Calendar.getInstance()
        return cInstance.get(Calendar.MONTH) + 1
    }

    fun getCurrentYear():Int{
        var cInstance:Calendar = Calendar.getInstance()
        return cInstance.get(Calendar.YEAR)
    }

    /**
     * 得到指定月的天数
     */
    fun getMonthLastDay(year: Int, month: Int): Int {
        val a = Calendar.getInstance()
        a[Calendar.YEAR] = year
        a[Calendar.MONTH] = month - 1
        a[Calendar.DATE] = 1 //把日期设置为当月第一天
        a.roll(Calendar.DATE, -1) //日期回滚一天，也就是最后一天
        return a[Calendar.DATE]
    }



}