package com.hb.basemodel.wheelview;

/**
 * @desc：
 * @author: yongzhi
 * @time: 2017/4/12 0012
 * @reviser_and_time:
 */

public class WheelAddress {
    //    /**
//     * @param provinceName 省、自治区名称
//     * @param provinceID   省id
//     * @param cityName     城市名称
//     * @param cityID       城市id
//     * @param countryName  城区、县名称
//     * @param countryID    城区、县id
//     */
    //今日/次日  小时  分钟
    private String day, hour, min;
    //地址
    private String address;

    public WheelAddress(String address) {
        this.address = address;
    }

    public WheelAddress(String day, String hour, String min) {
        this.day = day;
        this.hour = hour;
        this.min = min;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }
}
