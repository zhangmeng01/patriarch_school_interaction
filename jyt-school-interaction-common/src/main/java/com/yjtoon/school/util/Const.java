package com.yjtoon.school.util;
/**
 * 
 * 功能描述: 系统变量 
 * @version 1.0.0
 * @author llh
 */
public interface Const {
    public final static String LOGIN_USER_SESSION="loginedAccount";  //登录用户session的KEY
    
    public final static String LOGIN_TYPE="LOGINTYPE";  //用户类型，student：学生，teacher：老师
    
    public final static String LOGIN_USER_TYPE="LOGINUSERTYPE";  //登录用户session的KEY
    
    public final static String LOGIN_USER_SUB_TYPE="LOGINUSERSUBTYPE";  //登录用户session的KEY（子角色）
    
//    public final static String LOGIN_ADMIN_ROLE="loginedAdminRole";// 管理员角色
    
    public final static String LOGIN_USER_INFO="LOGINUSERINFO";
    
    public final static String LOGIN_USER_PHONE="loginedPhone";  //登录用户session的KEY
    
//    public final static String DEFAULT_USERINFO="defaultUserInfo"; //默认用户对象
   
    public final static String TOON_LOGIN_USER_TOKEN="accessToken";  // 登陆token信息

    public final static String MENU_TYPE="menuType";  // 菜单类型
    public final static String MAIN_FRAME_TYPE="mainFrameType";  //iframe首次显示

    public final static String MENU_MODULE="menuModule"; //菜单模块
    
    public final static String lOGIN_SCHOOL_CARD="loginSchoolCard";
    // 学校信息
    public final static String SWITCHSIGN="switchSign";  // 是否开启二维码
    
//    public final static String DEFAULT_SCHOOL_USER_ID="defaultSchoolUserId";  // 默认学校user_id
    
//    public final static String DEFAULT_SCHOOL_TOON_ORG_ID="defaultSchoolToonOrgId";  // 默认学校toon组织id
    
//    public final static String TOON_LOGIN_USER_ID="toonUserId"; //toon 用户id
    
    public final static String LOGIN_SCHOOL_INFO_SESSION="sessionSchoolInfo";  //根据登录用户获取该用户登录的学校信息
    
    public final static String USER_BIND_SHCOOL="userBindSchool";
    
    public final static String DICT_CACHE_NAME="dictCache";  //字典数据ehcache名字
    
    public final static String NATIONAL_DATA_CACHE_NAME="nationalDataCache"; //全国省市县数据ehcache名字
    
    public final static String NATIONAL_DATA_PROVICE_CACHE_KEY="nationalDataProviceKey"; //全国省nationalDataCache的key
    
    public final static String NATIONAL_DATA_CITY_CACHE_KEY="nationalDataCityKey"; //全国市nationalDataCache的key
    
    public final static String NATIONAL_DATA_DISTRICT_CACHE_KEY="nationalDataDistRictKey"; //全国县nationalDataCache的key
    
/*    public final static String DIC_SCHOOL_YEAR_CACHE_KEY="DicSchoolYearCacheKy"; 
    
    public final static String DIC_SCHOOL_TYPE_CACHE_KEY="DicSchoolTypeCacheKey";
    
    public final static String DIC_SUBJECT_CATEGORY_KEY="dicSubjectCategoryCacheKey";*/
    
    public final static String DICT_GENERAL_KEY="dictGeneralCacheKey";

    public final static String USER_GRADE_NAME="gradeName";

    public final static String ROLE_ID = "role_id"; //角色id

    public final static String UT_ID = "ut_id"; //老师id

    public final static String US_ID = "us_id"; //学生id
    public final static String LOGIN_USER_ICON = "loginUserIcon"; //登录用户的头像
    public final static String LOGIN_USER_NAME = "loginUserName"; //登录用户的昵称
}
