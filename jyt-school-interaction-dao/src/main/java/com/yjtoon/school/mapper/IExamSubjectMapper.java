package com.yjtoon.school.mapper;
import com.yjtoon.framework.mapper.ICrudMapper;
import com.yjtoon.school.domain.ExamSubject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IExamSubjectMapper extends ICrudMapper<ExamSubject>{

    int updateByExId(ExamSubject examSubject);

    List<Map<String,Object>> findTotalScore(@Param("exIdList")List<Long> exIdList);

}