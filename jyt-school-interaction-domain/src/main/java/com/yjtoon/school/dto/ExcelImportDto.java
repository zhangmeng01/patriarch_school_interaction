package com.yjtoon.school.dto;

import lombok.Data;

import java.util.List;
@Data
public class ExcelImportDto {

    private Integer code;
    private String message;
    private List<ExcelWarnDto> excelWarnDtoList;
}
