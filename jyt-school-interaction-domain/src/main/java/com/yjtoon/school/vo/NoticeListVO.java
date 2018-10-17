package com.yjtoon.school.vo;

import java.util.List;

import com.yjtoon.framework.entity.BaseEntity;
import com.yjtoon.school.domain.Notice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="通知列表VO实体",description="通知按时间分组列表展示")
public class NoticeListVO extends BaseEntity<NoticeListVO>{

	@ApiModelProperty(value="日期信息",name = "dateInfo")
	private String dateInfo;
	@ApiModelProperty(value="通知列表",name = "notices")
	private List<Notice> notices; 
}
