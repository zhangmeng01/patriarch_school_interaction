package com.yjtoon.school.domain;
import com.yjtoon.framework.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
@Data 
public class ExamDetail extends BaseEntity<ExamDetail> {
     
    private Long edId; //自增ID
    private Long exId; //考试ID
    private Long usId; //学生ID
    private String usName; //学生姓名
    private String edAchievement; //成绩
    private String usEducationid; //教育ID
    private String usCode; //学号
    private String usTotalScore; //总分
    private String edIsDelete; //是否删除：否 'N'   是   'Y'
    private Date createTime; //创建时间
    private Date updateTime; //修改时间
 
}