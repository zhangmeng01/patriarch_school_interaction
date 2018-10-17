package com.yjtoon.school.exception;

/**
 * 校验参数异常
 */
public class CheckParamException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    private String name; //参数名称

    public CheckParamException() {
        super();
    }

    public CheckParamException(String name, String message) {
        super(message);
        this.setName(name);
    }

    public CheckParamException(String message) {
        super(message);
    }

    public CheckParamException(Throwable cause) {
        super(cause);
    }

    public CheckParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}