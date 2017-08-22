package com.sven.exception;

/**
 * Created by sven on 2017/7/3.
 */
public class BizException extends RuntimeException {
    private String code;
    private String message;

    public BizException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BizException(String message) {
        super(message);
        this.message = message;
    }

    public BizException(ErrorCodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
    }
}
