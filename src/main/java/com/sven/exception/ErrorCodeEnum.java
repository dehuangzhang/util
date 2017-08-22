package com.sven.exception;

/**
 * Created by sven on 2017/7/4.
 */
public enum ErrorCodeEnum {
    NULL_ERROR("0001", "空指针错误"),
    PARAM_ERROR("0002", "参数错误"),
    TYPE_ERROR("000", "");
    private String code;
    private String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
