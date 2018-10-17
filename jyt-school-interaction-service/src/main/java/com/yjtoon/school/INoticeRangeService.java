package com.yjtoon.school;
import java.util.List;

import com.yjtoon.framework.IBaseService;
import com.yjtoon.school.domain.NoticeRange;
import com.yjtoon.school.vo.NoticeRangeExtVO;
 
public interface INoticeRangeService extends IBaseService<NoticeRange>{
     
	public boolean receiptNotice(long noticeId,long currentUserId);
	
	public boolean remindNoticeSingle(long noticeId,long userId);
	
	public boolean remindNoticeAll(long noticeId);
	
	public List<Long> getClassByNoticeId(long noticeId);

	public List<NoticeRangeExtVO> getReceiptPersonList(long noticeId);
	
	public List<NoticeRangeExtVO> getUnReceiptPersonList(long noticeId);
	
	public List<NoticeRangeExtVO> getConsultPersonList(long noticeId);
	
	public List<NoticeRangeExtVO> getUnConsultPersonList(long noticeId);
	
	public boolean consultNotice(long noticeId,long currentUserId);
	
}