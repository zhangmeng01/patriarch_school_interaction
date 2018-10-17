package com.yjtoon.school.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class MyScoreAndRankDto {
    private List<Map<String,Object>> scoreAndRankMap;
    private int sort;
}
