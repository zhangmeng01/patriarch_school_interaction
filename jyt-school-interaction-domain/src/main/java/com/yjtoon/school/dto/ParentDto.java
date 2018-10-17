package com.yjtoon.school.dto;

import lombok.Data;

@Data
public class ParentDto {
    private Long nrUserId;//家长ID
    private String nrUserName;//家长名称
    private Integer nrType;//家长类型为1
    private Long ciId;//班级ID
}
