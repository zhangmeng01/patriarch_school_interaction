package com.yjtoon.school.domain;
import com.yjtoon.framework.entity.BaseEntity;
import java.util.Date;
import lombok.Data;
@Data 
public class NoticePicture extends BaseEntity<NoticePicture> {
     
    private Long npId; //自增ID
    private Long noId; //通知ID
    private String npUrl; //图片路径
    private Integer npSort; //图片顺序
    private Date createTime; //创建时间
    private Date updateTime; //修改时间
 
}