package com.yjtoon.school.enums;

/**
 * 通知是否显示头像枚举
 *
 */
public enum HeadFlagEnum {
	
	NO_HEAD(0, "NO_HEAD", "不显示头像"), 
	HAS_ONE_HEAD(1, "HAS_ONE_HEAD", "只显示左边头像"), 
	HAS_HEAD(2, "HAS_ONE_HEAD", "显示两个头像");
	
    private Integer numCode;
    private String code;
    private String name;
    
	private HeadFlagEnum(Integer numCode, String code, String name) {
		this.numCode = numCode;
		this.code = code;
		this.name = name;
	}
	public Integer getNumCode() {
		return numCode;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
}
