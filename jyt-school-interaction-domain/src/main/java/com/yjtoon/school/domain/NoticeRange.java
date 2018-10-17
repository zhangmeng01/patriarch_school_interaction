package com.yjtoon.school.domain;
import com.yjtoon.framework.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
@Data 
public class NoticeRange extends BaseEntity<NoticeRange> {
     
    private Long nrId; //自增ID
    private Long noId; //通知ID
    private Long grId; //年级ID
    private Long ciId; //班级ID
    private Long nrUserId; //接收人ID
    private Integer nrType; //接收人类型：0 学生 1 家长 2 老师
    private Integer nrReceipt; //是否回执：0 未回执 1 已回执
    private Date nrReceiptTime; //回执时间
    private Integer nrRemind; //是否提醒：0 未提醒 1 已提醒
    private Date nrRemindTime; //提醒时间
    private Integer nrConsult; //是否查阅：0 未查阅 1 已查阅
    private Date nrConsultTime; //查阅时间
    private Date createTime; //创建时间
    private Date updateTime; //修改时间
 
}