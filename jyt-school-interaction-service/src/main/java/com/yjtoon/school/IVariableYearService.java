package com.yjtoon.school;
import com.yjtoon.school.domain.VariableYear;
import com.yjtoon.framework.IBaseService;
import com.yjtoon.school.vo.VariableYearVo;

import java.util.List;

public interface IVariableYearService extends IBaseService<VariableYear>{
    /**
     * @Author: gll
     * @Description:获取学年列表
     * @CreateDate: 2018/4/27 14:49
    */
    public List<VariableYearVo> getSchoolYear();
    public boolean saveOrUpdateYear (Long vaId, String vaYearNum);


}