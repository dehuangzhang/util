package com.sven.exception;

/**
 * Created by sven on 2017/7/3.
 */
public class BizException extends RuntimeException {
    private String code;
    private String msg;

    public BizException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BizException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
