package com.yjtoon.school.impl;
import com.yjtoon.school.domain.ExamSubject;
import com.yjtoon.school.mapper.IExamSubjectMapper;
import org.springframework.stereotype.Service;
import com.yjtoon.school.IExamSubjectService;
import com.yjtoon.framework.impl.BaseServiceImpl;

import java.util.List;

@Service
public class ExamSubjectServiceImpl extends BaseServiceImpl<IExamSubjectMapper,ExamSubject> implements IExamSubjectService{


    public List<ExamSubject> findByIds(Long[] ids) {
        return null;
    }
}