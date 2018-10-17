package com.yjtoon.school.domain;
import com.yjtoon.framework.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
@Data 
public class Exam extends BaseEntity<Exam> {
     
    private Long exId; //自增ID
    private String exName; //考试名称
    private Long grId; //年级ID
    private Long ciId; //班级ID
    private String grName; //年级名称
    private String ciName; //班级名称
    private Long vaId; //
    private String vaYearNum; //学期
    private String exClassify; //考试分类
    private String exIsDelete; //是否删除：否 'N'   是   'Y'
    private Date createTime; //创建时间
    private Date updateTime; //修改时间

}