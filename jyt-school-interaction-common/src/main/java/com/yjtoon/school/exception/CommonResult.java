package com.yjtoon.school.exception;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 返回时封装消息体
 *
 * @author guosl
 */
public class CommonResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private CodeMessage meta;
    private T data;

    public CommonResult() {
        this(null);
    }

    public CommonResult(String code, String message, T data) {
        this.meta = new CodeMessage();
        this.meta.setCode(code);
        this.meta.setMessage(message);
        this.data = data;
    }

    public CommonResult(String code, String message) {
        this(code, message, null);
    }

    public CommonResult(T data) {
        this("0", "操作成功", data);
    }


    public CodeMessage getMeta() {
        return meta;
    }

    public void setMeta(CodeMessage meta) {
        this.meta = meta;
    }
    
    @JsonIgnore()
    @JSONField(serialize=false)
    @ApiModelProperty(hidden = true)
    public String getCode() {
        return this.meta.getCode();
    }

    public CommonResult<T> setCode(String code) {
        this.meta.setCode(code);
        return this;
    }
    
    @JsonIgnore()
    @JSONField(serialize=false)
    @ApiModelProperty(hidden = true)
    public String getMessage() {
        return this.meta.getMessage();
    }

    public CommonResult<T> setMessage(String message) {
        this.meta.setMessage(message);
        return this;
    }

    public T getData() {
        return data;
    }

    public CommonResult<T> setData(T data) {
        this.data = data;
        return this;
    }
    
    @JsonIgnore()
    @JSONField(serialize=false)
    @ApiModelProperty(hidden = true)
    public boolean isSuccess() {
        return "0".equals(this.meta.getCode());
    }

    public CommonResult<T> me() {
        return new CommonResult<T>(this.data);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE, false);
    }
}