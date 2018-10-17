package com.yjtoon.school.impl;
import com.yjtoon.school.enums.HeadFlagEnum;
import com.yjtoon.school.enums.MessageSendEnum;
import org.springframework.stereotype.Service;
import com.systoon.im.client.MessageClient;
import java.util.UUID;

@Service
public class CommonMessageService {
	public boolean sendMessage(MessageSendEnum messageSendEnum,Long appId,String appCode, String from, String to, String toUserId,
							   String subCatalog, String summary, String bizNo, String content, Long expireTime, Integer bubbleFlag, Integer headFlag, Integer finishFlag)
	{
		String msgId = UUID.randomUUID().toString();
		String pushInfo = null;
		String headFeed = null;
		if (null == headFlag) {
			headFlag = HeadFlagEnum.NO_HEAD.getNumCode();
		}
		if (headFlag.equals(HeadFlagEnum.HAS_ONE_HEAD.getNumCode())) {
			headFeed = from;
		}
		if (null == bizNo) {
			bizNo = msgId;
		}
		Integer priority = 0;
		String msgType = messageSendEnum.getMsgType();
		Integer catalogId = messageSendEnum.getCatalogId();
		String catalog = messageSendEnum.getCatalog();
		Integer subCatalogId = null;
		Integer actionType = messageSendEnum.getActionType();
		Integer showFlag = messageSendEnum.getShowFlag();
		return MessageClient.sendNoticeMessage(appId, appCode, msgId, msgType, from, to, toUserId, pushInfo, priority, catalogId, catalog, subCatalogId, subCatalog, summary, headFlag, headFeed, finishFlag, actionType, bubbleFlag, bizNo, showFlag, expireTime, content);

	}
}
