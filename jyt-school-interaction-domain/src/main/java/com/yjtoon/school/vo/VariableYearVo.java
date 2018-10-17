package com.yjtoon.school.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: gll
 * @Description:出参
 * @CreateDate: 2018/4/27 15:12
*/
@Data
public class VariableYearVo implements Serializable {
    private static final long serialVersionUID = -4943536268859881092L;
     
    private Long vaId; //学年自增id
    private String vaYearNum; //学年

}