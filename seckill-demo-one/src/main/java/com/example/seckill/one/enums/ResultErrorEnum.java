package com.example.seckill.one.enums;

import lombok.Getter;

@Getter
public enum ResultErrorEnum {

    SERVER_ERROR(500100, "服务端异常"),
    //登录模块
    BIND_ERROR(500101, "请求参数校验异常：%s"),
    SESSION_ERROR(500210, "Session不存在或者已经失效"),
    PASSWORD_EMPTY (500211, "登录密码不能为空"),
    MOBILE_EMPTY(500212, "手机号不能为空"),
    MOBILE_ERROR(500213, "手机号格式错误"),
    MOBILE_NOT_EXIST(500214, "手机号不存在"),
    PASSWORD_ERROR(500215, "密码错误"),
    //秒杀业务
    USER_NOT_LOGIN(500800, "用户未登录"),
    SECKILL_OVER(500801, "商品已无库存"),
    SECKILL_REPEAT(500802, "秒杀商品不能重复下单"),
    ORDER_NOT_EXIST(500803, "订单不存在"),
    SECKILL_BUSY(500804, "服务繁忙，请重试"),
    ;

    private Integer code;
    private String message;

    ResultErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultErrorEnum fillErrorMsgs(Object ...args){
        String message = String.format(this.message, args);
        this.message = message;
        return this;
    }

}
