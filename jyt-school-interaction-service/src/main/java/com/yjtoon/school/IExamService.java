package com.yjtoon.school;
import com.github.pagehelper.PageInfo;
import com.yjtoon.framework.exception.ReturnInfo;
import com.yjtoon.school.domain.Exam;
import com.yjtoon.framework.IBaseService;
import com.yjtoon.school.domain.VariableYear;
import com.yjtoon.school.exception.CommonResult;

import java.util.List;

public interface IExamService extends IBaseService<Exam>{


    ReturnInfo<PageInfo<Exam>> findListPageByWhere(Exam exam, int pageNum, int pageSize);

    Boolean deleteExam(Long exId);
    List<VariableYear> findSemester(Long utId);
    List<Exam> getExamByIdsLimit(List<Long> exids,int pageNum,int pageSize);
}