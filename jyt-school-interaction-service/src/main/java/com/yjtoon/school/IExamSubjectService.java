package com.yjtoon.school;
import com.yjtoon.school.domain.ExamSubject;
import com.yjtoon.framework.IBaseService;

import java.util.List;

public interface IExamSubjectService extends IBaseService<ExamSubject>{

    List<ExamSubject> findByIds(Long[] ids);


}