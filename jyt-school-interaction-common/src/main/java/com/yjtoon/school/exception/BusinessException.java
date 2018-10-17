package com.yjtoon.school.exception;

import java.io.Serializable;

public class BusinessException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String message;

    public BusinessException() {

    }

    public BusinessException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(CommonResult cm) {
        this.code = cm.getMeta().getCode();
        this.message = cm.getMeta().getMessage();
    }

    public BusinessException(CommonResult cm, Throwable cause) {
        super(cause);
        this.code = cm.getMeta().getCode();
        this.message = cm.getMeta().getMessage();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CommonResult getCodeMessage() {
        return new CommonResult(code, message);
    }
}
