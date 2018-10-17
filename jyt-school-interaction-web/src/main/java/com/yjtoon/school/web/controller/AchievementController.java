package com.yjtoon.school.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.jyt.user.service.IToonUserService;
import com.jyt.user.service.IUserService;
import com.jyt.user.service.entity.*;
import com.jyt.user.service.enums.UserType;
import com.jyt.user.service.response.CommonResponse;
import com.yjtoon.framework.annotations.LogWrapper;
import com.yjtoon.framework.annotations.ReturnWrapper;
import com.yjtoon.framework.exception.ReturnInfo;
import com.yjtoon.framework.tooncode.ToonCode;
import com.yjtoon.framework.tooncode.ToonVisitor;
import com.yjtoon.school.IAchievementService;
import com.yjtoon.school.IExamSubjectService;
import com.yjtoon.school.domain.Achievement;
import com.yjtoon.school.domain.Exam;
import com.yjtoon.school.domain.ExamSubject;
import com.yjtoon.school.dto.*;
import com.yjtoon.school.enums.MessageCode;
import com.yjtoon.school.exception.BusinessException;
import com.yjtoon.school.exception.CommonResult;
import com.yjtoon.school.util.Const;
import com.yjtoon.school.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/jxhd/achievement")
@LogWrapper
@ReturnWrapper
@Validated
@RestController
@Api(value = "/jxhd/achievement", description = "考试成绩")
public class AchievementController {
    private static Logger logger = LoggerFactory.getLogger(AchievementController.class);
    @Autowired
    private IAchievementService iAchievementService;
    @Autowired
    private IExamSubjectService iExamSubjectService;
    @Reference(version="1.0.0")
    private IUserService iUserService;
    @Reference(version = "1.0.0")
    private IToonUserService toonUserService;

    @RequestMapping(value = "/findAchievementList")
    @ApiOperation(value = "获取考试成绩列表", httpMethod = "POST", notes = "获取考试成绩列表")
    public ReturnInfo<AchievementListDto> findAchievementList(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(name = "exId",  value = "考试ID", required = true) @RequestParam Long exId,
            @ApiParam(name = "page",  value = "当前页数", required = true) @RequestParam int page,
            @ApiParam(name = "pageSize", value = "每页条数", defaultValue = "10") @RequestParam(required=false, defaultValue="10") int pageSize) {
        ExamSubject examSubject = new ExamSubject();
        examSubject.setExId(exId);
        examSubject.setEsIsDelete(Constants.IS_DELETE_N);
        examSubject.setOrderBy(" subject_sort asc ");
        List<ExamSubject>  examSubjectList = iExamSubjectService.findList(examSubject);
//        ReturnInfo<PageInfo<Map<String,Object>>> rs = new ReturnInfo<PageInfo<Map<String,Object>>>();
        PageInfo<Map<String,Object>> pageInfo =  iAchievementService.findAchievementList(examSubjectList,exId,page,pageSize);
        AchievementListDto achievementListDto = new AchievementListDto();
        achievementListDto.setExamSubjectList(examSubjectList);
        achievementListDto.setPageInfo(pageInfo);
        ReturnInfo<AchievementListDto> rs = new ReturnInfo<AchievementListDto>();
        rs.setData(achievementListDto);
        return rs;
    }
    @RequestMapping(value = "/findAchievement")
    @ApiOperation(value = "获取考试成绩详情", httpMethod = "POST", notes = "获取考试成绩详情")
    public ReturnInfo<List<Achievement>> findAchievement(HttpServletRequest request, HttpServletResponse response,
           @ApiParam(name = "exId",  value = "考试ID", required = true) @RequestParam Long exId,
           @ApiParam(name = "usId",  value = "学生ID", required = true) @RequestParam Long usId) {
        List<Achievement> achievementList = iAchievementService.findAchievementByStudent(exId,usId);
        ReturnInfo<List<Achievement>> rs = new ReturnInfo<List<Achievement>>();
        rs.setData(achievementList);
        return rs;
    }
    @RequestMapping(value = "/toAaddAchievement")
    @ApiOperation(value = "跳转到添加成绩页面", httpMethod = "POST", notes = "跳转到添加成绩页面")
    public ReturnInfo<UsAndEsDto> toAaddAchievement(HttpServletRequest request, HttpServletResponse response,
             @ApiParam(name = "exId",  value = "考试ID", required = true) @RequestParam Long exId) {
        ReturnInfo<UsAndEsDto> rs = new ReturnInfo<UsAndEsDto>();
        List<UserStudent> userStudentList = new ArrayList<UserStudent>();
        HttpSession session = request.getSession();
        Long utId = (Long)session.getAttribute(Const.UT_ID);
        ClassTeacherRel classTeacherRel = new ClassTeacherRel();
        classTeacherRel.setCtrIsDelete(Constants.IS_DELETE_N);
        classTeacherRel.setUtId(utId);
        classTeacherRel.setCtrIsHeadmaster(Constants.IS_MASTER_TEACHER_Y);
        CommonResponse<List<ClassTeacherRel>> comClassTeacherRelList = iUserService.findClassTeacherRelList(classTeacherRel);
        MessageCodeDto messageCodeDto = new MessageCodeDto();
        if(comClassTeacherRelList.getData()!=null&&comClassTeacherRelList.getData().size()>0) {
            Long ciId = comClassTeacherRelList.getData().get(0).getCiId();
            UserStudent userStudent = new UserStudent();
            userStudent.setUsIsDelete(Constants.IS_DELETE_N);
            userStudent.setCiId(ciId);
            CommonResponse<List<UserStudent>> comUserStudentList = iUserService.findUserStudentList(userStudent);
            if(comUserStudentList.getData()!=null&&comUserStudentList.getData().size()>0){
                for(int i=0;i<comUserStudentList.getData().size();i++){
                    UserStudent student = new UserStudent();
                    student.setUsName(comUserStudentList.getData().get(i).getUsName());
                    student.setUsId(comUserStudentList.getData().get(i).getUsId());
                    userStudentList.add(student);
                }

            }
        }
        List<Long> usIdList = iAchievementService.getUsidByExid(exId);
        for(int i=0;i<usIdList.size();i++){
            for(int j=0;j<userStudentList.size();j++){
                if(usIdList.get(i)!=null&&usIdList.get(i).longValue()==userStudentList.get(j).getUsId().longValue()){
                    userStudentList.remove(j);
                }
            }
        }

        ExamSubject examSubject = new ExamSubject();
        examSubject.setExId(exId);
        examSubject.setEsIsDelete(Constants.IS_DELETE_N);
        examSubject.setOrderBy(" subject_sort asc ");
        List<ExamSubject>  examSubjectList = iExamSubjectService.findList(examSubject);
        UsAndEsDto usAndEsDto = new UsAndEsDto();
        usAndEsDto.setExamSubjectList(examSubjectList);
        usAndEsDto.setUserStudentList(userStudentList);
        rs.setData(usAndEsDto);
        return rs;
    }
    @RequestMapping(value = "/addAchievement")
    @ApiOperation(value = "添加考试成绩", httpMethod = "POST", notes = "添加考试成绩")
    public ReturnInfo<MessageCodeDto> AddAchievement(HttpServletRequest request, HttpServletResponse response,
                                                  @ApiParam(name = "UsAchievementDto",  value = "我的成绩对象", required = true) @RequestBody UsAchievementDto usAchievementDto) {
        ReturnInfo<MessageCodeDto> rs = new ReturnInfo<MessageCodeDto>();
        MessageCodeDto messageCodeDto = new MessageCodeDto();
        List<SubjectScore> subjectScoreList = usAchievementDto.getSubjectScoreList();
        Long exId = usAchievementDto.getExId();
        ExamSubject examSubject = new ExamSubject();
        examSubject.setEsIsDelete(Constants.IS_DELETE_N);
        examSubject.setExId(exId);
        List<ExamSubject> examSubjectList = iExamSubjectService.findList(examSubject);
        Boolean isFlag = true;
        for(int i=0;i<examSubjectList.size();i++){
            ExamSubject subject = examSubjectList.get(i);
            for(int j=0;j<subjectScoreList.size();j++){
                if(subject.getSubjectName()!=null&&subject.getSubjectName().equals(subjectScoreList.get(j).getSubjectName())){
                    if(-1==subject.getTotalScore().compareTo(subjectScoreList.get(j).getScore())){
                        isFlag = false;
                    }
                }
            }
        }
        if(isFlag){
            Boolean flag = iAchievementService.addUsAchievement(usAchievementDto,iUserService);
            messageCodeDto.setCode(MessageCode.ADD_ACHIEVEMENT_SUCCESS.getCode());
            messageCodeDto.setMessage(MessageCode.ADD_ACHIEVEMENT_SUCCESS.getMessage());
        }else{
            messageCodeDto.setCode(MessageCode.ADD_ACHIEVEMENT_FAIL.getCode());
            messageCodeDto.setMessage(MessageCode.ADD_ACHIEVEMENT_FAIL.getMessage());
        }
        rs.setData(messageCodeDto);
        return rs;
    }
    @RequestMapping(value = "/updateAchievement")
    @ApiOperation(value = "修改考试成绩", httpMethod = "POST", notes = "修改考试成绩")
    public ReturnInfo<MessageCodeDto> updateAchievement(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(name = "UsAchievementDto",  value = "我的成绩对象", required = true) @RequestBody UsAchievementDto usAchievementDto) {
//        ReturnInfo<MessageCodeDto> rs = new ReturnInfo<MessageCodeDto>();
//        Boolean flag = iAchievementService.updateUsAchievement(usAchievementDto);
//        rs.setData(flag);
        ReturnInfo<MessageCodeDto> rs = new ReturnInfo<MessageCodeDto>();
        MessageCodeDto messageCodeDto = new MessageCodeDto();
        List<SubjectScore> subjectScoreList = usAchievementDto.getSubjectScoreList();
        Long exId = usAchievementDto.getExId();
        ExamSubject examSubject = new ExamSubject();
        examSubject.setEsIsDelete(Constants.IS_DELETE_N);
        examSubject.setExId(exId);
        List<ExamSubject> examSubjectList = iExamSubjectService.findList(examSubject);
        Boolean isFlag = true;
        for(int i=0;i<examSubjectList.size();i++){
            ExamSubject subject = examSubjectList.get(i);
            for(int j=0;j<subjectScoreList.size();j++){
                if(subject.getSubjectName()!=null&&subject.getSubjectName().equals(subjectScoreList.get(j).getSubjectName())){
                    if(-1==subject.getTotalScore().compareTo(subjectScoreList.get(j).getScore())){
                        isFlag = false;
                    }
                }
            }
        }
        if(isFlag){
            Boolean flag = iAchievementService.updateUsAchievement(usAchievementDto);
            messageCodeDto.setCode(MessageCode.ADD_ACHIEVEMENT_SUCCESS.getCode());
            messageCodeDto.setMessage(MessageCode.ADD_ACHIEVEMENT_SUCCESS.getMessage());
        }else{
            messageCodeDto.setCode(MessageCode.ADD_ACHIEVEMENT_FAIL.getCode());
            messageCodeDto.setMessage(MessageCode.ADD_ACHIEVEMENT_FAIL.getMessage());
        }
        rs.setData(messageCodeDto);
        return rs;
    }
    @RequestMapping(value = "/deleteAchievement")
    @ApiOperation(value = "删除考试成绩", httpMethod = "POST", notes = "删除考试成绩")
    public ReturnInfo<Boolean> deleteAchievement(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(name = "exId",  value = "考试ID", required = true) @RequestParam Long exId,
            @ApiParam(name = "usId",  value = "学生ID", required = true) @RequestParam Long usId) {
        ReturnInfo<Boolean> rs = new ReturnInfo<Boolean>();
        Boolean flag = iAchievementService.deleteAchievement(exId,usId);
        rs.setData(flag);
        return rs;
    }
    @RequestMapping(value = "/findHistoryScore")
    @ApiOperation(value = "历史成绩查询接口", httpMethod = "POST", notes = "历史成绩查询接口")
    public ReturnInfo<List<HistoreScoreDto>> findHistoryScore(HttpServletRequest request, HttpServletResponse response) {
        ReturnInfo<List<HistoreScoreDto>> rs = new ReturnInfo<List<HistoreScoreDto>>();
        Long usId = null;
        TNPToonCard TNPToonCard = getTNPToonCard(request);
        if(TNPToonCard!=null&&TNPToonCard.getCardEntity()!=null&&TNPToonCard.getCardEntity().getEntityId()!=null){
            Long userId = TNPToonCard.getCardEntity().getEntityId();
            String userType = TNPToonCard.getCardEntity().getEntityType();
            if(UserType.PARENT.getUserTypeCode().equals(userType)){
//                usId = TNPToonCard.getStudents().get(0).getUsId();  因为用户中心的代码逻辑错误，暂时注释掉
                StudentParentRel studentParentRel = new StudentParentRel();
                studentParentRel.setUpId(userId);
                studentParentRel.setSpIsDelete(Constants.IS_DELETE_N);
                CommonResponse<List<StudentParentRel>> comSPRList = iUserService.findStudentParentRelList(studentParentRel);
                if(comSPRList.getData()!=null&&comSPRList.getData().size()>0&&comSPRList.getData().get(0).getUsId()!=null&&comSPRList.getData().get(0).getUsId().longValue()!=0){
                    usId = comSPRList.getData().get(0).getUsId();
                }
            }else{//因为只能是家长或者学生身份查看历史成绩，所以不是家长则默认是学生身份
                usId = TNPToonCard.getCardEntity().getEntityId();
            }
        }
        if(usId!=null){
            CommonResponse<UserStudent> comUserStudent = iUserService.findUserStudentById(usId);
            Long ciId = comUserStudent.getData().getCiId();
            List<HistoreScoreDto> historeScoreDtoList = iAchievementService.findHistoryScore(ciId,usId);
            rs.setData(historeScoreDtoList);
        }
        return rs;
    }
    @RequestMapping(value = "/findMyScoreAndRank")
    @ApiOperation(value = "我的成绩、班级平均成绩、我的排名", httpMethod = "POST", notes = "我的成绩、班级平均成绩、我的排名")
    public ReturnInfo<MyScoreAndRankDto> findMyScore(HttpServletRequest request, HttpServletResponse response,
//          @ApiParam(name = "usId",  value = "学生ID", required = true) @RequestParam Long usId,
    @ApiParam(name = "exId",  value = "考试ID", required = true) @RequestParam Long exId) {
        Long usId = getUserId(request);
        ReturnInfo<MyScoreAndRankDto> rs = new ReturnInfo<MyScoreAndRankDto>();
        MyScoreAndRankDto myScoreAndRankDto = iAchievementService.findMyScoreAndRank(exId,usId);
        rs.setData(myScoreAndRankDto);
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
    private TNPToonCard getTNPToonCard(HttpServletRequest request) {
        TNPToonCard TNPToonCard = null;
        String feedId = null;
        ToonCode toonCode = (ToonCode) request.getSession().getAttribute(Constants.USER_LOGIN_KEY);
        if (toonCode != null) {
            ToonVisitor visitor = toonCode.getVisitor();
            if (visitor != null) {
                feedId = visitor.getFeed_id();
                CommonResponse<List<TNPToonCard>> jytUserInfo = toonUserService.getJytUserInfo(feedId);
                List<TNPToonCard> data = jytUserInfo.getData();
                List<TNPToonCard> dataList = new ArrayList<>();
                for(int i=0;i<data.size();i++){
                    if(null==data.get(i).getCardEntity().getEntityId()||0==data.get(i).getCardEntity().getEntityId()){
                    }else{
                        dataList.add(data.get(i));
                    }
                }
                if (dataList != null && dataList.size() > 0) {
                    TNPToonCard = data.get(0);
                }
            }
        }
        return TNPToonCard;
    }
}
