package com.yjtoon.school.impl;
import com.yjtoon.school.domain.VariableYear;
import com.yjtoon.school.enums.StateEnum;
import com.yjtoon.school.mapper.IVariableYearMapper;
import com.yjtoon.school.vo.VariableYearVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yjtoon.school.IVariableYearService;
import com.yjtoon.framework.impl.BaseServiceImpl;

import java.util.List;

@Service
public class VariableYearServiceImpl extends BaseServiceImpl<IVariableYearMapper,VariableYear> implements IVariableYearService{
    @Autowired
    private IVariableYearMapper mapper;
    /**
     * @Author: gll
     * @Description:获取学年列表
     * @CreateDate: 2018/4/27 14:50
    */
    @Override
    public List<VariableYearVo> getSchoolYear() {
        VariableYear year = new VariableYear();
        year.setVaState(StateEnum.ENABLE.getNumCode());
        List<VariableYearVo> vaList = mapper.getSchoolYear(year);
        return vaList;
    }

    @Override
    public boolean saveOrUpdateYear(Long vaId, String vaYearNum) {
        VariableYear year = new VariableYear();
        year.setVaYearNum(vaYearNum);
        if(vaId!=null){
            year.setVaId(vaId);
            mapper.updateById(year);
        }else{
            mapper.insert(year);
        }
        return true;
    }
}