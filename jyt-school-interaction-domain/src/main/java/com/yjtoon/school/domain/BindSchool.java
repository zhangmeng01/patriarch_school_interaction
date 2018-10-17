package com.yjtoon.school.domain;

import java.io.Serializable;

/**
 * Created by 140799 on 2017/5/26.
 */
public class BindSchool implements Serializable{
	private Long sId; //学校ID
	private String sName;//学校名称
	private String sShortName;// 学校简称
	private String sIconUrl;// 学校图标url
	private String soToonOrgId;// 学校组织id

	public Long getsId() {
		return sId;
	}

	public void setsId(Long sId) {
		this.sId = sId;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getsShortName() {
		return sShortName;
	}

	public void setsShortName(String sShortName) {
		this.sShortName = sShortName;
	}

	public String getsIconUrl() {
		return sIconUrl;
	}

	public void setsIconUrl(String sIconUrl) {
		this.sIconUrl = sIconUrl;
	}

	public String getSoToonOrgId() {
		return soToonOrgId;
	}

	public void setSoToonOrgId(String soToonOrgId) {
		this.soToonOrgId = soToonOrgId;
	}
}
