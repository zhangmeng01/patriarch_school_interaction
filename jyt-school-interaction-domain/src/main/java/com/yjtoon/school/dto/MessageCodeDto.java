package com.yjtoon.school.dto;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
@Data
public class MessageCodeDto {

    private Integer code;
    private String  message;
    private T data;
}
