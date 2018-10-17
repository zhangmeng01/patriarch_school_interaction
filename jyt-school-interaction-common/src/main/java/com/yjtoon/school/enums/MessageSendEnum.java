package com.yjtoon.school.enums;

import java.text.MessageFormat;


public enum MessageSendEnum {
	
	APPROVE(142, "通知", "51", 1, 2);
	private Integer catalogId;
	private String catalog;
	private String msgType;
	private Integer actionType;//： 原生-无跳转； 1： URL链接；  2：原生-跳转Frame页； 3：跳转到支付；4：原生-选择名片列表页；5：跳转推荐名片；6：跳转应用；7：跳转单聊；
	private Integer showFlag;//1.不显示 2.通知中心显示 4.新朋友显示 8.Frame-通知显示 16.Frame-动态显示  
	
	private MessageSendEnum(Integer catalogId, String catalog, String msgType,
                            Integer actionType, Integer showFlag) {
		this.catalogId = catalogId;
		this.catalog = catalog;
		this.msgType = msgType;
		this.actionType = actionType;
		this.showFlag = showFlag;
	}
	public Integer getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(Integer catalogId) {
		this.catalogId = catalogId;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public Integer getActionType() {
		return actionType;
	}
	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}
	public Integer getShowFlag() {
		return showFlag;
	}
	public void setShowFlag(Integer showFlag) {
		this.showFlag = showFlag;
	}
	
	public static String format(String summary, String[] args) {
        if ( null==summary)
            return null;
        return  MessageFormat.format(summary, args);
    }
}
