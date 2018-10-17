package com.yjtoon.school.impl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyt.user.service.IUserService;
import com.jyt.user.service.entity.UserStudent;
import com.jyt.user.service.response.CommonResponse;
import com.yjtoon.school.IExamService;
import com.yjtoon.school.IExamSubjectService;
import com.yjtoon.school.domain.Exam;
import com.yjtoon.school.domain.ExamSubject;
import com.yjtoon.school.domain.Achievement;
import com.yjtoon.school.dto.*;
import com.yjtoon.school.mapper.IAchievementMapper;
import com.yjtoon.school.mapper.IExamSubjectMapper;
import com.yjtoon.school.util.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yjtoon.school.IAchievementService;
import com.yjtoon.framework.impl.BaseServiceImpl;

import java.math.BigDecimal;
import java.util.*;

@Service
public class AchievementServiceImpl extends BaseServiceImpl<IAchievementMapper,Achievement> implements IAchievementService{

    @Autowired
    private IExamSubjectService iExamSubjectService;
    @Autowired
    private IAchievementMapper iAchievementMapper;
    @Autowired
    private IExamSubjectMapper iExamSubjectMapper;
    @Autowired
    private IExamService iExamService;
//    @Autowired
//    private IUserService iUserService;

    /**添加学生考试成绩
     * @Author:xzl
     * @Description
     * @Date 11:35 2018/4/27
     */
    @Override
    public void creatAchievement(String param) {
        try {
            JSONArray jsonArray = new JSONArray(param);
            List<Long> subjectList = new ArrayList();
            for(int i=0;i<jsonArray.length();i++){
                Long subjectId = Long.parseLong(((org.json.JSONObject)jsonArray.get(i)).get("subjectId").toString());
                subjectList.add(subjectId);
            }
            List<ExamSubject> examSubjectList = iExamSubjectService.findByIds(subjectList.toArray(new Long[subjectList.size()]));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**修改学生考试成绩
     * @Author:xzl
     * @Description
     * @Date 11:36 2018/4/27
     */
    @Override
    public void updateAchievement(String param) {
        ExamSubject examSubject = new ExamSubject();
        List<ExamSubject> examSubjectList = iExamSubjectService.findList(examSubject);
        try {
            JSONArray jsonArray = new JSONArray(param);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageInfo<Map<String,Object>> findAchievementList(List<ExamSubject> examSubjectList, Long exId, int pageNum, int pageSize) {

        List<String> subjectNameList = new ArrayList<String>();
        for(int i=0;i<examSubjectList.size();i++){
            subjectNameList.add(examSubjectList.get(i).getSubjectName());
        }
        PageHelper.startPage(pageNum, pageSize,true);
        PageInfo<Map<String,Object>> page = new PageInfo<Map<String,Object>>(iAchievementMapper.findAchievementDtoList(exId,subjectNameList));
        return page;
    }

    @Override
    public List<Achievement> findAchievementByStudent(Long exId, Long usId) {
        Achievement achievement = new Achievement();
        achievement.setExId(exId);
        achievement.setUsId(usId);
        List<Achievement> achievementList = iAchievementMapper.findList(achievement);
        return achievementList;
    }

    @Override
    public Boolean deleteAchievement(Long exId, Long usId) {
        Boolean  flag = false;
        Achievement achievement = new Achievement();
        achievement.setAcIsDelete(Constants.IS_DELETE_Y);
        achievement.setExId(exId);
        achievement.setUsId(usId);
        int i = iAchievementMapper.deleteAchievement(achievement);
        if(i>0)flag = true;
        return flag;
    }

    @Override
    public Boolean updateUsAchievement(UsAchievementDto usAchievementDto) {
        Boolean flag = false;
        Long exId = usAchievementDto.getExId();
        Long usId = usAchievementDto.getUsId();
        List<SubjectScore> subjectScoreList = usAchievementDto.getSubjectScoreList();
        List<Achievement> achievementList = new ArrayList<Achievement>();
        for(int i=0;i<subjectScoreList.size();i++){
            Achievement achievement = new Achievement();
            achievement.setExId(exId);
            achievement.setScore(subjectScoreList.get(i).getScore());
            achievement.setUsId(usId);
            achievement.setSubjectName(subjectScoreList.get(i).getSubjectName());
            achievementList.add(achievement);
        }
        for (int i=0;i<achievementList.size();i++){
            int j = iAchievementMapper.updateAchievement(achievementList.get(i));
            if(j>0){
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public boolean addUsAchievement(UsAchievementDto usAchievementDto,IUserService iuserService) {
        Boolean flag = false;
        Long exId = usAchievementDto.getExId();
        Long usId = usAchievementDto.getUsId();
        //这部分等dubbo好了之后替换下边这部分
        CommonResponse<UserStudent> comUserStudent = iuserService.findUserStudentById(usId);
        UserStudent userStudent = comUserStudent.getData();
        //dubbo好了之后去掉
//        UserStudent userStudent = new UserStudent();
        userStudent.setUsId(userStudent.getUsId());
        userStudent.setUsName(userStudent.getUsName());
        userStudent.setUsCode(userStudent.getUsCode());
        userStudent.setUsXjh(userStudent.getUsXjh());
        userStudent.setCiId(userStudent.getCiId());
        List<SubjectScore> subjectScoreList = usAchievementDto.getSubjectScoreList();
        List<Achievement> achievementList = new ArrayList<Achievement>();
        for(int i=0;i<subjectScoreList.size();i++){
            Achievement achievement = new Achievement();
            achievement.setExId(exId);
            achievement.setUsId(usId);
            achievement.setUsName(userStudent.getUsName());
            achievement.setUsEducationid(userStudent.getUsXjh());
            achievement.setUsCode(userStudent.getUsCode());
            achievement.setCiId(userStudent.getCiId());
            achievement.setSubjectName(subjectScoreList.get(i).getSubjectName());
            achievement.setScore(subjectScoreList.get(i).getScore());
            achievement.setAcIsDelete(Constants.IS_DELETE_N);
            achievementList.add(achievement);
        }
        for (int i=0;i<achievementList.size();i++){
            int j = iAchievementMapper.insert(achievementList.get(i));
            if(j>0){
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public List<HistoreScoreDto> findHistoryScore( Long ciId, Long usId) {
        List<Map<String,Object>> map_myscore = iAchievementMapper.findMyScore(usId);
        List<Map<String,Object>> map_maScore = iAchievementMapper.findMaxAndAvgScore(ciId);
        List<Long> exIdList = new ArrayList<>();
        for(int i=0;i<map_maScore.size();i++){
            exIdList.add(Long.parseLong(String.valueOf(map_maScore.get(i).get("ex_id"))));
        }
        List<Map<String,Object>> map_totalScore = new ArrayList<Map<String,Object>>();
        if(exIdList.size()>0){
            map_totalScore = iExamSubjectMapper.findTotalScore(exIdList);
        }
        Exam exam = new Exam();
        exam.setExIsDelete(Constants.IS_DELETE_N);
        exam.setOrderBy(" ex_id desc ");
        List<Exam> examList = iExamService.getExamByIdsLimit(exIdList, 0, 6);
        List<HistoreScoreDto> historeScoreDtoList = new ArrayList<HistoreScoreDto>();
        Map<Long,HistoreScoreDto> map = new HashMap<>();
        List<Long> exIds = new ArrayList<>();
        for(int i=0;i<map_maScore.size();i++){
            HistoreScoreDto historeScoreDto = new HistoreScoreDto();
            map.put(Long.parseLong(String.valueOf(map_maScore.get(i).get("ex_id"))), historeScoreDto);
            exIds.add(Long.parseLong(String.valueOf(map_maScore.get(i).get("ex_id"))));
        }
        for(int i=0;i<map_myscore.size();i++){
            HistoreScoreDto historeScoreDto = new HistoreScoreDto();
            BigDecimal myScore = new BigDecimal(String.valueOf(map_myscore.get(i).get("score")));
            historeScoreDto.setMyScore(myScore);
            map.get(Long.parseLong(String.valueOf(map_myscore.get(i).get("ex_id")))).setMyScore(myScore);
        }
        for(int i=0;i<map_maScore.size();i++){
            BigDecimal max_score = new BigDecimal(String.valueOf(map_maScore.get(i).get("max_score")));
            BigDecimal avg_score = new BigDecimal(String.valueOf(map_maScore.get(i).get("avg_score")));
            avg_score = avg_score.setScale(1,BigDecimal.ROUND_HALF_UP);
            map.get(Long.parseLong(String.valueOf(map_maScore.get(i).get("ex_id")))).setMax_score(max_score);
            map.get(Long.parseLong(String.valueOf(map_maScore.get(i).get("ex_id")))).setAvg_score(avg_score);
        }
        for(int i=0;i<map_totalScore.size();i++){
            BigDecimal total_score = new BigDecimal(String.valueOf(map_totalScore.get(i).get("total_score")));
            map.get(Long.parseLong(String.valueOf(map_totalScore.get(i).get("ex_id")))).setTotal_score(total_score);
        }
        for(int i=0;i<examList.size();i++){
            map.get(examList.get(i).getExId()).setExam(examList.get(i));
        }
        for (int i=0;i<exIds.size();i++){
            historeScoreDtoList.add(map.get(exIds.get(i)));
        }
        return historeScoreDtoList;
    }

    @Override
    public MyScoreAndRankDto findMyScoreAndRank(Long exId, Long usId) {
        ExamSubject examSubject = new ExamSubject();
        examSubject.setExId(exId);
        examSubject.setEsIsDelete(Constants.IS_DELETE_N);
        examSubject.setOrderBy(" subject_sort asc ");
        List<ExamSubject>  examSubjectList = iExamSubjectService.findList(examSubject);
        List<String> subjectNameList = new ArrayList<String>();
        for(int i=0;i<examSubjectList.size();i++){
            subjectNameList.add(examSubjectList.get(i).getSubjectName());
        }
        List<Map<String,Object>> mapList = iAchievementMapper.findMyScoreAndRank(exId,usId,subjectNameList);
        List<Map<String,Object>> mySortList = iAchievementMapper.findMySort(exId);
//        String mySortstr = String.valueOf(mySortList.get(2).get("sort"));
        for(int i=0;i<mapList.size();i++){
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(mapList.get(i).get("avgscore")));
            bigDecimal = bigDecimal.setScale(1,BigDecimal.ROUND_HALF_UP);
            mapList.get(i).put("avgscore", bigDecimal);
        }
        Integer  mySort = null;
        for(int i=0;i<mySortList.size();i++){
            Long tmpSort = Long.parseLong(String.valueOf(mySortList.get(i).get("usId")));
            if(usId.equals(tmpSort)){
                String str = String.valueOf(mySortList.get(i).get("sort"));
                double d = Double.parseDouble(str);
                mySort = (int) d;
            }
        }
        MyScoreAndRankDto myScoreAndRankDto = new MyScoreAndRankDto();
        myScoreAndRankDto.setScoreAndRankMap(mapList);
        myScoreAndRankDto.setSort(mySort);
        return myScoreAndRankDto;
    }

    @Override
    public List<Long> getUsidByExid(Long exId) {
        List<Map<String,Object>> mapList = iAchievementMapper.getUsidByExid(exId);
        List<Long> usidList = new ArrayList<>();
        for(int i=0;i<mapList.size();i++){
            Long usId = Long.parseLong(String.valueOf(mapList.get(i).get("ut_id")));
            usidList.add(usId);
        }
        return usidList;
    }
}