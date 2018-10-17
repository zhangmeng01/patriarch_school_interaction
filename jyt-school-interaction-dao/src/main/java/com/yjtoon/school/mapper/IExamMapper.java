package com.yjtoon.school.mapper;
import com.yjtoon.framework.mapper.ICrudMapper;
import com.yjtoon.school.domain.Exam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IExamMapper extends ICrudMapper<Exam>{

    List<Exam> findListByWhere(Exam exam);
    List<Exam> getExamByIdsLimit(@Param("exIdList")List<Long> exIdList,@Param("pageNum")int  pageNum,@Param("pageSize")int pageSize);
   
}