package com.yjtoon.school.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yjtoon.framework.mapper.ICrudMapper;
import com.yjtoon.school.domain.Notice;
import com.yjtoon.school.vo.NoticeExtVO;

@Repository
public interface INoticeMapper extends ICrudMapper<Notice>{
	
	public List<NoticeExtVO> getNoticeByFilterForPublisher(@Param("currentUserId") long currentUserId, @Param("classIdList") List<Long> classId, @Param("noTypeList") List<Integer> noType);
	
	public List<NoticeExtVO> getNoticeByFilterForReceiver(@Param("currentUserId") long currentUserId,@Param("noTypeList") List<Integer> noType,@Param("userType") Integer userType);
	
	//public List<Notice> getNoticeForReceiver(@Param("currentUserId") long currentUserId);
   
}