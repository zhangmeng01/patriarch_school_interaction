package com.yjtoon.school.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.jyt.user.service.IOrganizationService;
import com.jyt.user.service.IToonUserService;
import com.jyt.user.service.IUserService;
import com.jyt.user.service.entity.*;
import com.jyt.user.service.response.CommonResponse;
import com.yjtoon.framework.annotations.LogWrapper;
import com.yjtoon.framework.annotations.ReturnWrapper;
import com.yjtoon.framework.exception.ReturnInfo;
import com.yjtoon.framework.tooncode.ToonCode;
import com.yjtoon.framework.tooncode.ToonVisitor;
import com.yjtoon.school.IAchievementService;
import com.yjtoon.school.IExamService;
import com.yjtoon.school.IExamSubjectService;
import com.yjtoon.school.IVariableYearService;
import com.yjtoon.school.domain.BindSchool;
import com.yjtoon.school.domain.Exam;
import com.yjtoon.school.domain.ExamDetail;
import com.yjtoon.school.domain.VariableYear;
import com.yjtoon.school.dto.*;
import com.yjtoon.school.enums.MessageCode;
import com.yjtoon.school.exception.BusinessException;
import com.yjtoon.school.exception.CommonResult;
import com.yjtoon.school.util.Const;
import com.yjtoon.school.util.Constants;
import com.yjtoon.school.vo.VariableYearVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/jxhd/exam")
@LogWrapper
@ReturnWrapper
@Validated
@RestController
@Api(value = "/jxhd/exam", description = "考试")
public class ExamController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(ExamController.class);
    @Autowired
    private IExamService iExamService;
    @Autowired
    private IExamSubjectService iExamSubjectService;
    @Autowired
    private IAchievementService iAchievementService;
    @Autowired
    private IVariableYearService iVariableYearService;
    @Reference(version="1.0.0")
    private IUserService iUserService;
    @Reference(version="1.0.0")
    private IOrganizationService iOrganizationService;
    @Autowired
    private IVariableYearService yearService;
    @Reference(version = "1.0.0")
    private IToonUserService toonUserService;

    @RequestMapping(value = "/findExamList")
    @ApiOperation(value = "获取考试列表", httpMethod = "POST", notes = "获取考试列表")
    public ReturnInfo<PageInfo<Exam>> findExamList(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(name = "exName",  value = "考试标名称", required = false) @RequestParam(required=false) String exName,
            @ApiParam(name = "vaYearNum",  value = "学期", required = false) @RequestParam(required=false) String vaYearNum,
            @ApiParam(name = "exClassify",  value = "考试分类", required = false) @RequestParam(required=false) String exClassify,
            @ApiParam(name = "pageNum",  value = "当前页数", required = true) @RequestParam int pageNum,
            @ApiParam(name = "pageSize", value = "每页条数", defaultValue = "10") @RequestParam(required=false, defaultValue="10") int pageSize) {
        ReturnInfo<PageInfo<Exam>> rs = new ReturnInfo<PageInfo<Exam>>();
        HttpSession session = request.getSession();
        Long utId = (Long)session.getAttribute(Const.UT_ID);
        ClassTeacherRel classTeacherRel = new ClassTeacherRel();
        classTeacherRel.setCtrIsDelete(Constants.IS_DELETE_N);
        classTeacherRel.setCtrIsHeadmaster(Constants.IS_MASTER_TEACHER_Y);
        classTeacherRel.setUtId(utId);
        CommonResponse<List<ClassTeacherRel>> com = iUserService.findClassTeacherRelList(classTeacherRel);
        if(com.getData()!=null&&com.getData().size()>0){
            Long ciId = com.getData().get(0).getCiId();
            Exam exam = new Exam();
            exam.setCiId(ciId);
            if(StringUtils.isNotBlank(exName)){
                exam.setExName(exName);
            }
            if(StringUtils.isNotBlank(vaYearNum)){
                exam.setVaYearNum(vaYearNum);
            }
            if(StringUtils.isNotBlank(exClassify)){
                exam.setExClassify(exClassify);
            }
            exam.setExIsDelete(Constants.IS_DELETE_N);
            exam.setOrderBy(" create_time desc ");
            rs = iExamService.findListPageByWhere(exam,pageNum,pageSize);
        }
        return rs;
    }
    @RequestMapping(value = "/findExamListByApp")
        @ApiOperation(value = "获取考试列表ByAPP", httpMethod = "POST", notes = "获取考试列表ByAPP")
        public ReturnInfo<List<Exam>> findExamListByApp(HttpServletRequest request, HttpServletResponse response) {
            Long utId = getUserId(request);
            ReturnInfo<List<Exam>> rs = new ReturnInfo<List<Exam>>();
            ClassTeacherRel classTeacherRel = new ClassTeacherRel();
            classTeacherRel.setCtrIsDelete(Constants.IS_DELETE_N);
            classTeacherRel.setCtrIsHeadmaster(Constants.IS_MASTER_TEACHER_Y);
            classTeacherRel.setUtId(utId);
            CommonResponse<List<ClassTeacherRel>> com = iUserService.findClassTeacherRelList(classTeacherRel);
            if(com.getData()!=null&&com.getData().size()>0){
                Long ciId = com.getData().get(0).getCiId();
                Exam exam = new Exam();
                exam.setCiId(ciId);
                exam.setExIsDelete(Constants.IS_DELETE_N);
                exam.setOrderBy(" create_time desc ");
                List<Exam> examList = iExamService.findList(exam);
                rs.setData(examList);
            }
        return rs;
    }
    @RequestMapping(value = "/findExam")
    @ApiOperation(value = "获取考试详情", httpMethod = "POST", notes = "获取考试详情")
    public ReturnInfo<Exam> findExam(HttpServletRequest request, HttpServletResponse response,
                                     @ApiParam(name = "exId",  value = "考试ID", required = true) @RequestParam(required=true) Long exId){
        ReturnInfo<Exam> rs = new ReturnInfo<Exam>();
        Exam exam = iExamService.getById(exId);
        rs.setData(exam);
        return rs;
    }
    @RequestMapping(value = "/deleteExam")
    @ApiOperation(value = "删除考试", httpMethod = "POST", notes = "删除考试")
    public ReturnInfo<Boolean> deleteExam(HttpServletRequest request, HttpServletResponse response,
                                          @ApiParam(name = "exId",  value = "考试ID", required = true) @RequestParam(required=true) Long exId){
        ReturnInfo<Boolean> rs = new ReturnInfo<Boolean>();
        Boolean flag = iExamService.deleteExam(exId);
        rs.setData(flag);
        return rs;
    }
    @RequestMapping(value = "/findSemesterAndClass")
    @ApiOperation(value = "查询学期及其年级班级", httpMethod = "POST", notes = "查询学期及其年级班级")
    public ReturnInfo<SemesterAndClassDto> findSemesterAndClass(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Long utId = (Long)session.getAttribute(Const.UT_ID);
        //添加判断学期是否存在,并且不存在自动添加学期的逻辑
        ReturnInfo<SemesterAndClassDto> rs = new ReturnInfo<SemesterAndClassDto>();
        VariableYear vy = new VariableYear();
        vy.setVaState(1);
        List<VariableYear> VYList = iVariableYearService.findList(vy);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String currentYear = sdf.format(date);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        Date y = c.getTime();
        String yesteryear = sdf.format(y);
        String year = yesteryear+"-"+currentYear;
        Boolean flag = false;
        for(int i=0;i<VYList.size();i++){
            if(year.trim().equals(VYList.get(i).getVaYearNum().trim())){
                flag = true;
            }
        }
        if(!flag){
            Boolean f = yearService.saveOrUpdateYear(null, year);
        }
        SemesterAndClassDto semesterAndClassDto = new SemesterAndClassDto();
        List<VariableYear> variableYearList = iExamService.findSemester(utId);
        semesterAndClassDto.setVariableYearList(variableYearList);
        ClassTeacherRel classTeacherRel = new ClassTeacherRel();
        classTeacherRel.setUtId(utId);
        classTeacherRel.setCtrIsDelete(Constants.IS_DELETE_N);
        classTeacherRel.setCtrIsHeadmaster(Constants.IS_MASTER_TEACHER_Y);
        CommonResponse<List<ClassTeacherRel>> comClassTeacherRelList = iUserService.findClassTeacherRelList(classTeacherRel);
        List<ClassTeacherRel> classTeacherRelList = comClassTeacherRelList.getData();
        if(classTeacherRelList.size()>0){
            CommonResponse<ClassInfo> comClassInfo = iOrganizationService.findClassInfoById(classTeacherRelList.get(0).getCiId());
            CommonResponse<Grade> comGrade = iOrganizationService.findGradeById(classTeacherRelList.get(0).getGrId());
            semesterAndClassDto.setCiName(comClassInfo.getData().getCiName());
            semesterAndClassDto.setGrName(comGrade.getData().getGrName());
        }
        rs.setData(semesterAndClassDto);
        return rs;
    }

    @RequestMapping(value = "/findExamIsExists")
    @ApiOperation(value = "判断考试是否存在", httpMethod = "POST", notes = "判断考试是否存在")
    public ReturnInfo<MessageCodeDto> findExamIsExists(HttpServletRequest request, HttpServletResponse response,
               @ApiParam(name = "vaYearNum",  value = "学期", required = false) @RequestParam(required=false) String vaYearNum,
               @ApiParam(name = "exClassify",  value = "考试分类", required = false) @RequestParam(required=false) String exClassify,
               @ApiParam(name = "exName",  value = "考试名称", required = false) @RequestParam(required=false) String exName) {
        HttpSession session = request.getSession();
        Long utId = (Long)session.getAttribute(Const.UT_ID);
        ReturnInfo<MessageCodeDto> rs = new ReturnInfo<MessageCodeDto>();
        Exam exam = new Exam();
        ClassTeacherRel classTeacherRel = new ClassTeacherRel();
        classTeacherRel.setCtrIsDelete(Constants.IS_DELETE_N);
        classTeacherRel.setUtId(utId);
        classTeacherRel.setCtrIsHeadmaster(Constants.IS_MASTER_TEACHER_Y);
        CommonResponse<List<ClassTeacherRel>> comClassTeacherRelList = iUserService.findClassTeacherRelList(classTeacherRel);
        MessageCodeDto messageCodeDto = new MessageCodeDto();
        if(comClassTeacherRelList.getData()!=null&&comClassTeacherRelList.getData().size()>0){
            Long ciId = comClassTeacherRelList.getData().get(0).getCiId();
            exam.setCiId(ciId);
            exam.setVaYearNum(vaYearNum);
            exam.setExClassify(exClassify);
            exam.setExName(exName);
            exam.setExIsDelete(Constants.IS_DELETE_N);
            List<Exam> examList = iExamService.findList(exam);
            if(examList.size()>0){
                messageCodeDto.setCode(MessageCode.EXAM_IS_EXISTS.getCode());
                messageCodeDto.setMessage(MessageCode.EXAM_IS_EXISTS.getMessage());
            }else{
                messageCodeDto.setCode(MessageCode.EXAM_NOT_EXISTS.getCode());
                messageCodeDto.setMessage(MessageCode.EXAM_NOT_EXISTS.getMessage());
            }
        }else{
            messageCodeDto.setCode(MessageCode.EXAM_NOT_EXISTS.getCode());
            messageCodeDto.setMessage(MessageCode.EXAM_NOT_EXISTS.getMessage());
        }
        rs.setData(messageCodeDto);
        return rs;
    }
    /**
     * 获取当前用户ID
     *
     * @param request
     * @return
     */
    private Long getUserId(HttpServletRequest request) {
        Long userId = null;
        String feedId = null;
        ToonCode toonCode = (ToonCode) request.getSession().getAttribute(Constants.USER_LOGIN_KEY);
        if (toonCode != null) {
            ToonVisitor visitor = toonCode.getVisitor();
            if (visitor != null) {
                feedId = visitor.getFeed_id();
                CommonResponse<List<TNPToonCard>> jytUserInfo = toonUserService.getJytUserInfo(feedId);
                List<TNPToonCard> data = jytUserInfo.getData();
                if (data != null && data.size() > 0) {
                    userId = data.get(0).getCardEntity().getEntityId();
                }
            }
        }
        return userId;
    }
}
