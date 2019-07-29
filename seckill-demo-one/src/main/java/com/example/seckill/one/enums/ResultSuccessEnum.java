package com.example.seckill.one.enums;

import lombok.Getter;

@Getter
public enum ResultSuccessEnum {

    SECKILL_PENDING(200101, "秒杀排队中"),
    SECKILL_OVER(200102, "秒杀失败，宝贝卖完了"),
    ;

    private Integer code;
    private String message;

    ResultSuccessEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
