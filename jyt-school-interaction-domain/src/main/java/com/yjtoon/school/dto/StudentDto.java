package com.yjtoon.school.dto;

import lombok.Data;

@Data
public class StudentDto {
    private Long nrUserId;//学生ID
    private String nrUserName;//学生名称
    private Integer nrType;//学生类型为0
    private Long ciId;//班级ID
}
