package com.yjtoon.school.util;

/**
 * @ClassName Constants
 * @Description: 常量管理
 * @author: SHENZL
 * @since: 2017/11/24 10:57
 */
public class Constants {
    public static final String IS_DELETE_N = "N";//是否删除：否
    public static final String IS_DELETE_Y = "Y";//是否删除：是
    public static final String IS_MASTER_TEACHER_Y = "Y";//是否是班主任：是
    /*********end***********/
    
    /**
     * 应用Key
     */
    public static final String USER_LOGIN_KEY = "SCHOOL_INTERACTION_USER_LOGIN_KEY";
    public static final String USER_LOGIN_TOONCODE = "SCHOOL_INTERACTION_TOONCODE";
    /**
     * 用户类型
     */
    public static final Integer STUDENT_TYPE = 0;//学生类型
    public static final Integer PARENT_TYPE = 1;//家长类型
    public static final Integer TEACHER_TYPE = 2;//老师类型
    /**
     * 通知类型
     */
    public static final Integer NOTICE_NORMAL = 0;//标准通知
    public static final Integer NOTICE_HOMEWORK = 1;//作文通知
    public static final Integer NOTICE_SCORE = 2;//成绩通知

    /**
     * 固定业务名称
     */
    public static final String HOMEWORK="作业";
    public static final String EXAM_SCORE="考试成绩";
    public static final String STUDENT_GROUP="学生群";
    public static final String PARENT_GROUP="家长群";
    public static final String PICTURE = "[图片]";
    
}
