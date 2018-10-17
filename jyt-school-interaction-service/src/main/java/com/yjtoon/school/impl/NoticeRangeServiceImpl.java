package com.yjtoon.school.impl;
import com.yjtoon.school.domain.NoticeRange;
import com.yjtoon.school.mapper.INoticeRangeMapper;
import com.yjtoon.school.vo.NoticeRangeExtVO;

import java.util.List;

import org.springframework.stereotype.Service;
import com.yjtoon.school.INoticeRangeService;
import com.yjtoon.framework.impl.BaseServiceImpl;
 
@Service
public class NoticeRangeServiceImpl extends BaseServiceImpl<INoticeRangeMapper,NoticeRange> implements INoticeRangeService{
   
	@Override
	public boolean receiptNotice(long noticeId, long currentUserId) {
		int rst = this.mapper.receiptNotice(noticeId, currentUserId);
		return rst > 0;
	}

	@Override
	public boolean remindNoticeSingle(long noticeId, long userId) {
		int rst = this.mapper.remindNoticeSingle(noticeId, userId);
		return rst > 0;
	}

	@Override
	public boolean remindNoticeAll(long noticeId) {
		int rst = this.mapper.remindNoticeAll(noticeId);
		return rst > 0;
	}

	@Override
	public List<Long> getClassByNoticeId(long noticeId) {
		return this.mapper.getClassByNoticeId(noticeId);
	}

	@Override
	public List<NoticeRangeExtVO> getReceiptPersonList(long noticeId) {
		return this.mapper.getReceiptPersonList(noticeId);
	}

	@Override
	public List<NoticeRangeExtVO> getUnReceiptPersonList(long noticeId) {
		return this.mapper.getUnReceiptPersonList(noticeId);
	}

	@Override
	public List<NoticeRangeExtVO> getConsultPersonList(long noticeId) {
		return this.mapper.getConsultPersonList(noticeId);
	}

	@Override
	public List<NoticeRangeExtVO> getUnConsultPersonList(long noticeId) {
		return this.mapper.getUnConsultPersonList(noticeId);
	}
	
	@Override
	public boolean consultNotice(long noticeId, long currentUserId) {
		int rst = this.mapper.consultNotice(noticeId, currentUserId);
		return rst > 0;
	}
	
}