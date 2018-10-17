package com.yjtoon.school.impl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyt.user.service.IUserService;
import com.jyt.user.service.entity.ClassTeacherRel;
import com.yjtoon.framework.exception.ReturnInfo;
import com.yjtoon.framework.sqlplus.EntityWrapper;
import com.yjtoon.school.IExamSubjectService;
import com.yjtoon.school.IVariableYearService;
import com.yjtoon.school.domain.Achievement;
import com.yjtoon.school.domain.Exam;
import com.yjtoon.school.domain.ExamSubject;
import com.yjtoon.school.domain.VariableYear;
import com.yjtoon.school.exception.CommonResult;
import com.yjtoon.school.mapper.IAchievementMapper;
import com.yjtoon.school.mapper.IExamMapper;
import com.yjtoon.school.mapper.IExamSubjectMapper;
import com.yjtoon.school.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yjtoon.school.IExamService;
import com.yjtoon.framework.impl.BaseServiceImpl;

import java.util.List;

@Service
public class ExamServiceImpl extends BaseServiceImpl<IExamMapper,Exam> implements IExamService{

    @Autowired
    private IExamMapper iExamMapper;
    @Autowired
    private IAchievementMapper iAchievementMapper;
    @Autowired
    private IExamSubjectMapper iExamSubjectMapper;
    @Autowired
    private IVariableYearService iVariableYearService;

    @Override
    public ReturnInfo<PageInfo<Exam>> findListPageByWhere(Exam exam, int pageNum, int pageSize) {
        ReturnInfo<PageInfo<Exam>> rs = new ReturnInfo<PageInfo<Exam>>();
        PageHelper.startPage(pageNum, pageSize,true);
        PageInfo<Exam> page = new PageInfo<Exam>(
        iExamMapper.findListByWhere(exam));
        rs.setData(page);
        return rs;
    }

    @Override
    public Boolean deleteExam(Long exId) {
        //删除这次考试对应的成绩
        Achievement achievement = new Achievement();
        achievement.setExId(exId);
        achievement.setAcIsDelete(Constants.IS_DELETE_Y);
        int i = iAchievementMapper.updateByExId(achievement);
        //删除这次考试对应的学科
        ExamSubject examSubject = new ExamSubject();
        examSubject.setExId(exId);
        examSubject.setEsIsDelete(Constants.IS_DELETE_Y);
        int j = iExamSubjectMapper.updateByExId(examSubject);
        //删除这次考试
        Exam exam = new Exam();
        exam.setExId(exId);
        exam.setExIsDelete(Constants.IS_DELETE_Y);
        int k = iExamMapper.updateById(exam);
        return k>0;
    }

    @Override
    public List<VariableYear> findSemester(Long utId) {
        VariableYear variableYear = new VariableYear();
        variableYear.setOrderBy(" va_id desc limit 0,3 ");
        List<VariableYear> variableYearList = iVariableYearService.findList(variableYear);
        return variableYearList;
    }

    @Override
    public List<Exam> getExamByIdsLimit(List<Long> exids, int pageNum, int pageSize) {
        return iExamMapper.getExamByIdsLimit(exids,pageNum,pageSize);
    }
}