package com.yjtoon.school.vo;

import java.util.List;

import com.yjtoon.framework.entity.BaseEntity;
import com.yjtoon.school.domain.Notice;
import com.yjtoon.school.domain.NoticePicture;
import com.yjtoon.school.domain.NoticeRange;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="通知表单对应的VO实体",description="用于接受前端表单内容")
public class NoticeVO extends BaseEntity<NoticeVO>{
	
	@ApiModelProperty(value="通知对象",name = "notice")
	private Notice notice;//通知对象
	@ApiModelProperty(value="通知图片对象列表",name = "noticePictures")
	private List<NoticePicture> noticePictures;//通知包含的图片对象列表
	@ApiModelProperty(value="通知范围对象",name = "noticeRange")
	private List<NoticeRange> noticeRanges;//通知范围对象
}
