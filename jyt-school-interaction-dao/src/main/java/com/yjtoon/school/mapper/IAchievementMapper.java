package com.yjtoon.school.mapper;
import com.yjtoon.framework.mapper.ICrudMapper;
import com.yjtoon.school.domain.Achievement;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IAchievementMapper extends ICrudMapper<Achievement>{

    List<Map<String,Object>> findAchievementDtoList(@Param("exId") Long exId ,@Param("subjectNameList")List<String> subjectNameList);

    int updateByExId(Achievement achievement);

    int updateAchievement(Achievement achievement);

    int deleteAchievement(Achievement achievement);

    List<Map<String, Object>>findMyScore(@Param("usId")Long usId);

    List<Map<String, Object>> findMaxAndAvgScore(@Param("ciId")Long ciId);

    List<Map<String,Object>> findMyScoreAndRank(@Param("exId")Long exId,@Param("usId")Long usId,@Param("subjectNameList")List<String> subjectNameList);

    List<Map<String,Object>> findMySort(@Param("exId") Long exId);

    List<Map<String,Object>> getUsidByExid(@Param("exId")Long exId);


}