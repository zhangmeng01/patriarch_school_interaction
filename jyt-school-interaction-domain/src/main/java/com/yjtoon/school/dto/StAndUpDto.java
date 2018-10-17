package com.yjtoon.school.dto;

import com.jyt.user.service.entity.UserParent;
import com.jyt.user.service.entity.UserStudent;
import lombok.Data;

import java.util.List;
@Data
public class StAndUpDto {
    private List<StudentDto> studentList;//学生集合
    private List<ParentDto> parentList;//家长集合
}
