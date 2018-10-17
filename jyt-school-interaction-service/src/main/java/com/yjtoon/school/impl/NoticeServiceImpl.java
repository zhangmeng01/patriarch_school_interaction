package com.yjtoon.school.impl;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.yjtoon.framework.impl.BaseServiceImpl;
import com.yjtoon.school.INoticeService;
import com.yjtoon.school.domain.Notice;
import com.yjtoon.school.mapper.INoticeMapper;
import com.yjtoon.school.vo.NoticeExtVO;
 
@Service
public class NoticeServiceImpl extends BaseServiceImpl<INoticeMapper,Notice> implements INoticeService{
   
	public List<NoticeExtVO> getNoticeByFilterForPublisher(long currentUserId, List<Long> classIdList, List<Integer> noTypeList, int pageNumber, int pageSize){
		PageHelper.startPage(pageNumber, pageSize);
		List<NoticeExtVO> list = this.mapper.getNoticeByFilterForPublisher(currentUserId,classIdList,noTypeList);
		return list;
	}
	
	public List<NoticeExtVO> getNoticeByFilterForReceiver(long currentUserId, List<Integer> noTypeList, Integer userType ,int pageNumber, int pageSize){
		PageHelper.startPage(pageNumber, pageSize);
		List<NoticeExtVO> list = this.mapper.getNoticeByFilterForReceiver(currentUserId,noTypeList,userType);
		return list;
	}

	/*@Override
	public List<Notice> getNoticeForReceiver(long currentUserId,int pageNumber, int pageSize) {
		PageHelper.startPage(pageNumber, pageSize);
		List<Notice> list = this.mapper.getNoticeForReceiver(currentUserId);
		return list;
	}*/
	
}