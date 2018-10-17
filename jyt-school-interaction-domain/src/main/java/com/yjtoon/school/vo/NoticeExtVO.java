package com.yjtoon.school.vo;

import com.yjtoon.school.domain.Notice;

import lombok.Data;

@Data
public class NoticeExtVO extends Notice{
	
	private String className;//班级名称
	private Integer nrConsult; //是否查阅：0 未查阅 1 已查阅
	private Integer nrReceipt; //是否回执：0 未回执 1 已回执
	private String publisher;//发布人姓名
}
