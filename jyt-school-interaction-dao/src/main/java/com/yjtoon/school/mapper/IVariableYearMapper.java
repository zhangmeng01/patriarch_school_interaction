package com.yjtoon.school.mapper;
import com.yjtoon.framework.mapper.ICrudMapper;
import com.yjtoon.school.domain.VariableYear;
import com.yjtoon.school.vo.VariableYearVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVariableYearMapper extends ICrudMapper<VariableYear>{
    List<VariableYearVo> getSchoolYear(VariableYear year);
   
}