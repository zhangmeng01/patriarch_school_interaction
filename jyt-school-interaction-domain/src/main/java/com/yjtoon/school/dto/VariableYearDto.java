package com.yjtoon.school.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: gll
 * @Description:添加学年入参
 * @CreateDate: 2018/4/27 15:21
*/
@Data
@ApiModel(value="添加学年", description = "VariableYearDto")
public class VariableYearDto implements Serializable
{
    private static final long serialVersionUID = 7718695103011607882L;
    @ApiModelProperty(value="学年",name = "vaYearNum" ,required = true)
    private String vaYearNum; //学年
}