package com.yjtoon.school.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class AchievementDto {
    private Long acId; //自增ID
    private Long exId; //考试ID
    private Long ciId; //班级主键
    private Long usId; //学生ID
    private String usName; //学生姓名
    private String usEducationid; //教育ID
    private Long subjectId; //科目ID
    private String subjectName; //科目名称
    private Integer subjectSort; //科目顺序
    private BigDecimal score; //分数
    private String acIsDelete; //是否删除：否 'N'   是   'Y'
    private Date creatTime; //创建时间
    private Date updateTime; //修改时间
    //扩展字段
    private BigDecimal totalScore;//总分

}
