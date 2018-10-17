package com.yjtoon.school.exception;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class CodeMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
     * 编码
     */
    private String code;
    
    /**
     * 消息
     */
    private String message;

    public CodeMessage() {}

    public CodeMessage(String code, String message) {
        this.setCode(code);
        this.setMessage(message);
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE, false);
    }
}
