package com.yjtoon.school.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yjtoon.framework.mapper.ICrudMapper;
import com.yjtoon.school.domain.NoticeRange;
import com.yjtoon.school.vo.NoticeRangeExtVO;

@Repository
public interface INoticeRangeMapper extends ICrudMapper<NoticeRange>{
	
	public int receiptNotice(@Param("noId") long noticeId,@Param("nrUserId") long currentUserId);
	
	public int remindNoticeSingle(@Param("noId") long noticeId,@Param("nrUserId") long userId);
	
	public int remindNoticeAll(@Param("noId") long noticeId);
	
	public List<Long> getClassByNoticeId(@Param("noId") long noticeId); 
	
	public List<NoticeRangeExtVO> getReceiptPersonList(@Param("noId") long noId); 
	
	public List<NoticeRangeExtVO> getUnReceiptPersonList(@Param("noId") long noId);
	
	public List<NoticeRangeExtVO> getConsultPersonList(@Param("noId") long noId); 
	
	public List<NoticeRangeExtVO> getUnConsultPersonList(@Param("noId") long noId);

	public int consultNotice(@Param("noId") long noticeId,@Param("nrUserId") long currentUserId); 
}