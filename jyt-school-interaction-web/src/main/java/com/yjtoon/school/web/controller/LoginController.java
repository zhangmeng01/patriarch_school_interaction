package com.yjtoon.school.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.jyt.user.service.IToonUserService;
import com.jyt.user.service.IUserService;
import com.jyt.user.service.entity.ClassTeacherRel;
import com.jyt.user.service.entity.TNPToonCard;
import com.jyt.user.service.entity.ToonCardEntity;
import com.jyt.user.service.response.CommonResponse;
import com.yjtoon.framework.annotations.LogWrapper;
import com.yjtoon.framework.annotations.ReturnWrapper;
import com.yjtoon.framework.exception.AppRetrunType;
import com.yjtoon.framework.exception.BusinessException;
import com.yjtoon.framework.exception.ReturnInfo;
import com.yjtoon.framework.tooncode.ToonCode;
import com.yjtoon.framework.tooncode.ToonDesUtils;
import com.yjtoon.framework.tooncode.ToonVisitor;
import com.yjtoon.school.util.Constants;
import com.yjtoon.school.web.config.AppInfoConfig;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName LoginController
 * @Description: 用户登录接口
 * @author: SHENZL
 * @since: 2017/11/27 14:11
 */
@RestController
@LogWrapper
@ReturnWrapper
@RequestMapping("/")
@Api(tags = "LoginController", description = "用户登录相关接口")
public class LoginController {
    private  final  static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AppInfoConfig appInfoConfig;
    @Reference(version = "1.0.0")
	private IToonUserService toonUserService;
    @Reference(version = "1.0.0")
	private IUserService userService;
    @PostMapping("/loginAppWithCode")
    @ApiOperation(value = "用户登录接口", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(paramType = "query", value = "tooncode", name = "toonCode", required = true, dataType = "String"),
    	@ApiImplicitParam(paramType = "query", value = "userType", name = "userType", required = true, dataType = "Integer")
    })
	public ReturnInfo<Integer> loginAppWithCode(HttpServletRequest request,Integer userType, HttpServletResponse response,String toonCode) {
		ReturnInfo<Integer> ri = new ReturnInfo<>();
		if (StringUtils.isBlank(toonCode) || userType == null) {
			logger.error("请求登录接口参数不合法");
			throw new BusinessException(AppRetrunType.PARAMIllEGAL);
		}
		//解密toonCode
		String tooninfo = ToonDesUtils.decryptWithSecret(toonCode, appInfoConfig.getAppKey());
		logger.info("解密后toon信息[" + tooninfo+"]");
		ToonCode code = JSONObject.parseObject(tooninfo, ToonCode.class);
		if (code != null) {
			ToonVisitor visitor = code.getVisitor();
			Long userId = visitor.getUser_id();
			String feedId = visitor.getFeed_id();
			if (visitor != null && StringUtils.isNotEmpty(feedId)) {
				request.getSession().setAttribute(Constants.USER_LOGIN_KEY, code);
				request.getSession().setAttribute(String.valueOf(userId), userType);
				//通过FeedId获取utId(或者usId、upId)
				CommonResponse<List<TNPToonCard>> jytUserInfo = toonUserService.getJytUserInfo(feedId);
				List<TNPToonCard> tnpToonCard = jytUserInfo.getData();
				logger.info("tnpToonCard信息["+tnpToonCard+"]");
				if (tnpToonCard != null && tnpToonCard.size() > 0) {
					ToonCardEntity cardEntity = tnpToonCard.get(0).getCardEntity();
					if (cardEntity != null) {
						userId = cardEntity.getEntityId();
					}
				}
				if (userType.intValue() == Constants.TEACHER_TYPE.intValue()) {
					ClassTeacherRel classteacherrel = new ClassTeacherRel();
					classteacherrel.setUtId(userId);
					classteacherrel.setCtrIsHeadmaster(Constants.IS_MASTER_TEACHER_Y);
					CommonResponse<List<ClassTeacherRel>> classTeacherRelList = userService.findClassTeacherRelList(classteacherrel);
					List<ClassTeacherRel> ctrList = classTeacherRelList.getData();
					logger.info("班级老师关系列表信息["+ctrList+"]");
					if (ctrList != null && ctrList.size() > 0) {
						ri.setData(1);
					}else {
						ri.setData(0);
					}
				}else {
					ri.setData(0);
				}
			}
		}
		return ri;
	}
    
    /*private UserType getUserType(Integer type) {
    	UserType userType = null;
		// 0 学生 1 家长 2 老师
		if (type == Constants.STUDENT_TYPE) {
			userType = UserType.STUDENT;
		} else if (type == Constants.PARENT_TYPE) {
			userType = UserType.PARENT;
		} else {
			userType = UserType.TEACHER;
		}
		return userType;
    }*/
    public static void main(String [] args){
    	String toonCode1 = "CZuWXtpoTWdcQUoLAm+lVt1Ywy7YhOP9ZvhHpISqrU4jE6a9dFfPAYLmF/Zv7mpawomKQaCxv/U5/gBR5OCi7w==";
		String toonCode2 = "CZuWXtpoTWdcQUoLAm+lVt1Ywy7YhOP9cyXM08u1weYJPw9znM7IGR+jdJNPtq9ZwomKQaCxv/XZLnsaEHMnNg==";
		String toonCode3 = "CZuWXtpoTWdcQUoLAm+lVt1Ywy7YhOP9w8cajuL4IBmCDB+EsvkxrgfVVf0EMQ6oShFHmx8wqtrZy3LlElC6FQ==";
		String toonCode4 = "CZuWXtpoTWdcQUoLAm+lVt1Ywy7YhOP9ZvhHpISqrU4bIs0bnBYYj0D+3OlJIRtmWjaL0lLTISBt1f6qCyMnoA==";
		String toonCode5 = "CZuWXtpoTWdcQUoLAm+lVt1Ywy7YhOP9bNdjJBS3kcwWkO49vu/DaYLmF/Zv7mpaehtaxbcuDNJt1f6qCyMnoA==";
		String toonCode6 = "sBRFI0Vqo1CCB2IEuaMYtkA2hYOl4sQJP7I1Kw65sPPPDVuEQ+Ve7hzYu7RRogPGPQ6FZ4u2K3pqi1PXCahax42C0hzupDg7N//MJZjZTEX75RRYfviEwlkUoUWoT25gHPnDY4FrynEKXmwlmjO0AaHASK8LbA2Be0Uo6tkl3ZZKEUebHzCq2vk0uqq/7W55KyUp8XBx8Os9HShbfOJR+K6KmrTwMjVueNxBnvg9PCE=";
		String appKey = "cdc095af4dd14c64949a865a2d004be5";
		String tooninfo1 = ToonDesUtils.decryptWithSecret(toonCode1, appKey);
		String tooninfo2 = ToonDesUtils.decryptWithSecret(toonCode2, appKey);
		String tooninfo3 = ToonDesUtils.decryptWithSecret(toonCode3, appKey);
		String tooninfo4 = ToonDesUtils.decryptWithSecret(toonCode4, appKey);
		String tooninfo5 = ToonDesUtils.decryptWithSecret(toonCode5, appKey);
		String tooninfo6 = ToonDesUtils.decryptWithSecret(toonCode6, appKey);
		System.out.println(tooninfo1);
		System.out.println(tooninfo2);
		System.out.println(tooninfo3);
		System.out.println(tooninfo4);
		System.out.println(tooninfo5);
		System.out.println(tooninfo6);
	}
}
