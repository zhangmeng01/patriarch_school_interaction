package com.yjtoon.school.dto;

import com.github.pagehelper.PageInfo;
import com.yjtoon.school.domain.ExamSubject;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class AchievementListDto {
    private List<ExamSubject> examSubjectList;
    private PageInfo<Map<String,Object>> pageInfo;
}
