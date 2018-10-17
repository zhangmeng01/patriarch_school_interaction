package com.yjtoon.school.dto;

import lombok.Data;

@Data
public class ExcelWarnDto {
    private int row;//行
    private int column;//列
    private String explain;//说明
}
