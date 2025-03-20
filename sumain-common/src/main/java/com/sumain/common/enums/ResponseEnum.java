package com.sumain.common.enums;

import lombok.Getter;


@Getter
public enum ResponseEnum {

    SUCCESS(20000,"成功"),

    //客户端
    BUSINESS_ERROR(40000,"业务异常"),
    PARAM_ERROR(40001,"参数异常"),
    UNAUTHORIZED(40002,"请先登录！"),
    FORBIDDEN(40003,"您暂无权限进行此操作！"),


    //服务端
    FAIL(50000, "失败"),
    NETWORK_ERROR(50200, "网络异常,请重试"),
    SERVICE_ERROR(50001,"服务异常"),
    DATA_ERROR(50002,"数据异常"),
    SENTINEL_BLOCK(50003, "系统繁忙，请稍后再试"),
    ;

    private final Integer code;

    private final String msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
