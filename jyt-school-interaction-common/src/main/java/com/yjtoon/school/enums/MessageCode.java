package com.yjtoon.school.enums;

import com.yjtoon.framework.exception.ReturnTypeInterface;
import org.apache.tools.ant.taskdefs.EchoXML;

/**
 * Created by tingfeng on 2017/11/23.
 */
public enum MessageCode implements ReturnTypeInterface{
    PARAM_ERROR(1001 ,"参数为空") ,
    TOON_FEED_EMPTY(3001,"获取FEED信息失败，请重试"),
    NO_DATA_RETURN(1002 ,"没有有数据为空") ,
    DUPLICATE_JOIN(1003 ,"您已报名过，不要重复提交") ,
    ACTIVITY_READY(1004 ,"活动未开始") ,
    ACTIVITY_FINISH(1005 ,"已结束") ,
    ENROLL_UNTIME(1006 ,"未到录取时间") ,
    IDNUM_YEAR_EXITS(1007 , "检测到当前身份信息已存在报名记录，请重试。如有疑问，请拨打 0830-2522069 。") ,
    ENROLL_OVER(1008 ,"报名数量到达上限") ,
    IDCARD_ERROR(1009 ,"身份证号错误") ,
    IDCARD_BIRTHDAY_ERROR(1010 ,"身份证信息错误") ,
    AUTH_NOT_LOGIN(4001,"请先登录"),
    AUTH_NOT_OPERATE(9001,"无操作权限"),
    PARAMETER_ERROR(6000, "参数不正确"),
    //家校互动需要的
    EXCEL_IMPORT_SUCCESS(101,"导入成功"),
    EXCEL_IMPORT_FAIL(102,"以上数据不符合规范，请修改后在重新导入"),
    EXCEL_IMPORT_FAIL_UTID_IS_NULL(103,"请登陆系统导入数据"),
    EXCEL_IMPORT_FAIL_CLASSINFO_IS_NULL(104,"请使用班主任账号导入成绩"),
    EXAM_IS_EXISTS(201,"考试已经存在"),
    EXAM_NOT_EXISTS(202,"改考试不存在"),
    ADD_ACHIEVEMENT_SUCCESS(301,"考试成绩添加成功"),
    ADD_ACHIEVEMENT_FAIL(302,"科目分数不能大于满分，请修改后在添加"),


    DATA_ERROR(9002,"数据错误"),
    SYSTEM_ERROR(9003,"系统错误"),
    DATE_FORMAT_ERROR(9003,"日期格式化错误"),
    SYSTEM_ERROR_IDCARD(9004,"身份证号解析出错，请联系管理员");


    private Integer code;
    private String message;

    MessageCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return this.message;
    }
}
