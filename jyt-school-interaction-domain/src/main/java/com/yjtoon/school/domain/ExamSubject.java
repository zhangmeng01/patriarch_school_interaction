package com.yjtoon.school.domain;
import com.yjtoon.framework.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
@Data 
public class ExamSubject extends BaseEntity<ExamSubject> {
     
    private Long esId; //自增ID
    private Long exId; //考试ID
    private String subjectName; //科目名称
    private Integer subjectSort; //科目顺序
    private String averageStore; //平均分
    private BigDecimal totalScore; //总分
    private String esIsDelete; //是否删除：否 'N'   是   'Y'
    private Date createTime; //创建时间
    private Date updateTime; //修改时间

}