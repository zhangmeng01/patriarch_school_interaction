package com.yjtoon.school.domain;
import com.yjtoon.framework.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
@Data 
public class Notice extends BaseEntity<Notice> {
     
    private Long noId; //自增ID
    private String noTitle; //通知标题
    private Integer noType; //通知类型: 0通知 1作业 2成绩
    private String noContent; //通知内容
    private String noSubject; //科目
    private Long noExamId;//考试ID
    private Long noPublisherId; //发布人ID
    private Integer noReceipt; //是否回执：0 没有 1 有
    private Integer noPicture; //通知图片：0 没有  1有
    private Integer noRanking; //是否包含考试排名：0 不包含 1包含
    private Date noReceiptTime; //限时回执时间
    private String noIsDelete; //是否删除：'N' 否  'Y' 是
    private String remark; //备注
    private Date createTime; //创建时间
    private Date updateTime; //修改时间
 
}