package com.yjtoon.school.dto;

import lombok.Data;

@Data
public class NoticeRangeDto {
    private String grName;//年级名称
    private String ciName;//班级名称
    private StAndUpDto stAndUpDto;//学生和家长集合
}
