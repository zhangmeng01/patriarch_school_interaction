package com.yjtoon.school.vo;

import com.yjtoon.framework.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class PublishRangeVo extends BaseEntity<PublishRangeVo> {
    private Integer grId; //年级ID
    private Long ciId; //班级ID
    private Long toUserId; //接收人ID
    private String toFeedId; //接收人ID
    private String  stuName;//学生姓名
    private String parentName;//家长姓名


 
}