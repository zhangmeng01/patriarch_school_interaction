package com.yjtoon.school;
import java.util.List;

import com.yjtoon.framework.IBaseService;
import com.yjtoon.school.domain.Notice;
import com.yjtoon.school.vo.NoticeExtVO;
 
public interface INoticeService extends IBaseService<Notice>{
     
	public List<NoticeExtVO> getNoticeByFilterForPublisher(long currentUserId, List<Long> classIdList, List<Integer> noTypeList, int pageNumber, int pageSize);
	
	public List<NoticeExtVO> getNoticeByFilterForReceiver(long currentUserId, List<Integer> noTypeList, Integer userType,int pageNumber, int pageSize);
	
	//public List<Notice> getNoticeForReceiver(long currentUserId,int pageNumber, int pageSize);
}