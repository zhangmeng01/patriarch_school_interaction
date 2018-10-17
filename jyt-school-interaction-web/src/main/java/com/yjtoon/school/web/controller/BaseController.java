package com.yjtoon.school.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.StringWriter;

public abstract class BaseController {
	protected Logger LOGGER= LoggerFactory.getLogger(getClass());
//	public int pageNum=1;
//	public final static int pageSize=10;
	
	/** 
	 * 日志对象
	 */
	 
	public final static String SUCCESS="success";
	public final static String FAILER="failer";
	//返回错误信息的标志
	public final static String RESULT_ERROR_MSG="errorMessage";
	
	/**
	 * 获取分页参数-页数
	 */
	protected Integer getPageNum(Integer pageNum){
		if(pageNum==null){
			pageNum=Integer.valueOf(1);
		}
		return pageNum;
	}
	/**
	 * 获取分页参数-数量
	 */
	protected Integer getPageSize(Integer pageSize){
		if(pageSize==null){
			pageSize=Integer.valueOf(10);
		}
		return pageSize;
	}
}
