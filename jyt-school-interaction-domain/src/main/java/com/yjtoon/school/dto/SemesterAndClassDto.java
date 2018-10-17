package com.yjtoon.school.dto;

import com.yjtoon.school.domain.VariableYear;
import lombok.Data;

import java.util.List;

@Data
public class SemesterAndClassDto {

    private String ciName;
    private String grName;
    private List<VariableYear> variableYearList;
}
