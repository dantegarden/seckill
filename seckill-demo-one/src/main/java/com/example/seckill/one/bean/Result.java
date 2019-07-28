package com.example.seckill.one.bean;


import com.example.seckill.one.enums.HttpStatusCodeEnum;
import com.example.seckill.one.enums.ResultErrorEnum;

public class Result<T> {
    private Boolean status = true;
    private int code = 200;
    private String message;
    private T data;

    public static Result ok() {
        return new Result();
    }

    public static Result ok(Object data) {
        return new Result(data);
    }

    public static Result fail() {
        return new Result(false, null);
    }

    public static Result fail(String message) {
        return new Result(false, message);
    }

    public static Result fail(String message, int code) {
        return new Result(false, message, code);
    }

    public static Result failByParam(String message) {
        return new Result(false, message, HttpStatusCodeEnum.PARAM_ERROR.getCode());
    }
    public static Result failByEnum(ResultErrorEnum resultEnum) {
        return new Result(false, resultEnum.getMessage(), resultEnum.getCode());
    }


    public Result(T data) {
        super();
        this.data = data;
    }

    public Result(boolean status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public Result(boolean status, String message, int code) {
        super();
        this.status = status;
        this.message = message;
        this.code = code;
    }


    public Result() {
        super();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
