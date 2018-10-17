package com.yjtoon.school.vo;
import com.yjtoon.framework.entity.BaseEntity;
import lombok.Data;

import java.util.Date;
@Data
public class UserBean extends BaseEntity<UserBean> {
    private Long noId;//通知id
    private String title;//通知标题
    private String content;//通知内容
    private String fromToonFeed;// toon用户feed 消息发送者的名片的feedID
    private String toToonFeed;// toon用户feed 消息接收者的名片的feedID
    private Long toonUserId; //toon用户id  接收消息的用户的UserID
    private String url;//查看详情url
    private String toonCode;//
}