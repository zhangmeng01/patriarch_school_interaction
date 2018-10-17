package com.yjtoon.school.vo;

import com.yjtoon.framework.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="发布范围",description="发布范围")
public class PublishRangeListVo extends BaseEntity<PublishRangeListVo>{
	@ApiModelProperty(value="发布范围",name = "发布范围")
	private String groupName;
	private List<PublishRangeVo> publishRanges;
}
