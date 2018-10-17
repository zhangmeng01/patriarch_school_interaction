
package com.yjtoon.school.exception;

/**
 * 错误返回码
 *
 * @author guosl
 */
@SuppressWarnings("rawtypes")
public class ExceptionCodes {
    /**
     * 这个返回的data有异常.
     * 如果没有返回值.必须这样写: ExceptionCodes.NORMAL.setData(null) 或 new CommonResult()
     */
    public final static CommonResult NORMAL = new CommonResult("0", "操作成功");
    public final static CommonResult FAIL = new CommonResult("-1", "操作失败");
    /**
     * 业务系统前缀（三位大写字母） +2位错误类别    认证权限类10 参数错误类20  10~59系统保留   60~99业务自行定义    +3位错误编码
     */
    public static final CommonResult VALIDATE_NO_PERMISSIONS_000 = new CommonResult("UUM10000", "无权查看");
    public final static CommonResult UNKOWN = new CommonResult("UUM60000", "未知异常");

    /**********************参数校验***********************/
    public static final CommonResult PARAM_REQUEST_000 = new CommonResult("UUM20000", "请求参数不能为空");
    public static final CommonResult PARAM_ADMIN_REQUEST_ACCOUNT_001 = new CommonResult("UUM20001", "用户名不能为空");
    public static final CommonResult PARAM_ADMIN_REQUEST_PWD_002 = new CommonResult("UUM20002", "密码不能为空");
    public static final CommonResult PARAM_ADMIN_EXIST_003 = new CommonResult("UUM20003", "用户名已注册");
    public static final CommonResult PARAM_ADMIN_ID_004 = new CommonResult("UUM20004", "请输入正确的管理员ID");
    public static final CommonResult PARAM_ORG_ID_BLANK_005 = new CommonResult("UUM20005", "组织ID不能为空");
    public static final CommonResult PARAM_USER_TYPE_006 = new CommonResult("UUM20006", "用户类型不能为空");
    public static final CommonResult PARAM_REQUEST_IDTYPE_007 = new CommonResult("UUM20007", "请求参数nodetype值不正确");
    public static final CommonResult PARAM_ID_BLANK_008 = new CommonResult("UUM20008", "请求参数ID不能为空");
    public static final CommonResult PARAM_REQUEST_VALUE_009 = new CommonResult("UUM20009", "请求参数值不正确");
    public static final CommonResult PARAM_REQUEST_DATA_EMPTY_010 = new CommonResult("UUM20010", "请求数据不存在");
    public static final CommonResult PARAM_ORG_EXISTS_011 = new CommonResult("UUM20011", "组织已经存在");
    public static final CommonResult PARAM_USER_FEEDID_BLANK_011 = new CommonResult("UUM20012", "用户FeedId为空");
    public static final CommonResult PARAM_MAX_PAGE_SIZE_013 = new CommonResult("UUM20013", "每页条数不能大于");


    public static final CommonResult UPLOAD_PICTURE_BLANK = new CommonResult("UUM60044", "上传文件不能为空");
    public static final CommonResult UPLOAD_PICTURE_FILENAME_BLANK = new CommonResult("UUM60045", "文件名为空");
    public static final CommonResult UPLOAD_PICTURE_FILE_TYPE_NO = new CommonResult("UUM60046", "不支持文件类型");
    public static final CommonResult UPLOAD_PICTURE_FILENAME_NOTPHONE = new CommonResult("UUM60047", "文件名称非手机号码");
    public static final CommonResult UPLOAD_PICTURE_FILE_SIZE = new CommonResult("UUM60048", "单个文件大小字节数超过限制");
    public static final CommonResult UPLOAD_FILE_NOT_ZIP = new CommonResult("UUM60049", "上传文件非zip格式");
    public static final CommonResult UPLOAD_PICTURE_FILE_SUMSIZE = new CommonResult("UUM60050", "文件总大小超过限制");




}

