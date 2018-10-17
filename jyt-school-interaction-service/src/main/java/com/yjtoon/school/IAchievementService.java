package com.yjtoon.school;
import com.github.pagehelper.PageInfo;
import com.jyt.user.service.IUserService;
import com.yjtoon.school.domain.Achievement;
import com.yjtoon.framework.IBaseService;
import com.yjtoon.school.domain.ExamSubject;
import com.yjtoon.school.dto.AchievementDto;
import com.yjtoon.school.dto.HistoreScoreDto;
import com.yjtoon.school.dto.MyScoreAndRankDto;
import com.yjtoon.school.dto.UsAchievementDto;

import java.util.List;
import java.util.Map;

public interface IAchievementService extends IBaseService<Achievement>{

    void creatAchievement(String param);
    void updateAchievement(String param);
    PageInfo<Map<String,Object>> findAchievementList(List<ExamSubject> examSubjectList, Long exId, int page, int pageSize);
    List<Achievement> findAchievementByStudent(Long exId,Long usId);
    Boolean deleteAchievement(Long exId,Long usEducationid);
    Boolean updateUsAchievement(UsAchievementDto usAchievementDto);
    boolean addUsAchievement(UsAchievementDto usAchievementDto,IUserService iUserService);
    List<HistoreScoreDto> findHistoryScore(Long ciId,Long usId);
    MyScoreAndRankDto findMyScoreAndRank(Long exId, Long usId);
    List<Long> getUsidByExid(Long exId);
}