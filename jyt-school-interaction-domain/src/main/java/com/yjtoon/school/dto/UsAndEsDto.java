package com.yjtoon.school.dto;

import com.jyt.user.service.entity.UserStudent;
import com.yjtoon.school.domain.ExamSubject;
import lombok.Data;

import java.util.List;

@Data
public class UsAndEsDto {
    private List<UserStudent> userStudentList;
    private List<ExamSubject> examSubjectList;
}
