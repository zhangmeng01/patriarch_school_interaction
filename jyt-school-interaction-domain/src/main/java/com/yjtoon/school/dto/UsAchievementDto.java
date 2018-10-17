package com.yjtoon.school.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class UsAchievementDto {
    private Long exId;//考试ID
//    private String usName;//学生姓名
    private Long usId;//学生ID
//    private String  usEducationid;//教育ID
//    private String usCode;//学号
//    private Long ciId;//班级ID
    private List<SubjectScore> subjectScoreList;
}
