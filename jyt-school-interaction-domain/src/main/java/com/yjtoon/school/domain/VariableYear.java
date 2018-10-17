package com.yjtoon.school.domain;
import com.yjtoon.framework.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
@Data 
public class VariableYear extends BaseEntity<VariableYear> {
     
    private Long vaId; //学年自增id
    private String vaYearNum; //学年
    private Integer vaState; //状态1=可用,2=不可用
 
}