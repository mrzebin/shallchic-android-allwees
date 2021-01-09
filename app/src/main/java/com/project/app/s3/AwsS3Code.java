package com.project.app.s3;

public enum AwsS3Code {
    AVR("avr", "头像"),
    REV("rev", "订单评价"),
    RFD("rfd", "订单退款"),
    FDK("fdk", "平台反馈");

    AwsS3Code(String code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    private final String code;
    private final String comment;

    public String getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }
}
