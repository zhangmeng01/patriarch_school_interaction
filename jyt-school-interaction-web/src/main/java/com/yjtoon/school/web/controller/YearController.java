package com.yjtoon.school.web.controller;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.yjtoon.framework.annotations.LogWrapper;
import com.yjtoon.framework.annotations.ReturnWrapper;
import com.yjtoon.framework.exception.BusinessException;
import com.yjtoon.framework.exception.ReturnInfo;
import com.yjtoon.school.IVariableYearService;
import com.yjtoon.school.domain.VariableYear;
import com.yjtoon.school.enums.BubbleEnum;
import com.yjtoon.school.enums.HeadFlagEnum;
import com.yjtoon.school.enums.MessageCode;
import com.yjtoon.school.enums.MessageSendEnum;
import com.yjtoon.school.enums.StateEnum;
import com.yjtoon.school.impl.CommonMessageService;
import com.yjtoon.school.vo.UserBean;
import com.yjtoon.school.vo.VariableYearVo;
import com.yjtoon.school.web.config.AppInfoConfig;

import im.MsgContent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@LogWrapper
@ReturnWrapper
@Validated
@RestController
@RequestMapping("/jxhd/year")
@Api(value = "/jxhd/year", description = "学年设置")
public class YearController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(YearController.class);
    @Autowired
    private IVariableYearService yearService;
    @Autowired
    private CommonMessageService commonMessageService ;
    @Autowired
    private AppInfoConfig appInfo;
    private static Gson gson = new Gson();

    /**
     * @Author: gll
     * @Description:获取学年列表
     * @CreateDate: 2018/4/27 14:47
    */
    @PostMapping(value = "/getSchoolYear")
    @ApiOperation(value = "获取学年列表", httpMethod = "POST", notes = "获取学年列表")
    public ReturnInfo<List<VariableYearVo>> getSchoolYear() {
        ReturnInfo<List<VariableYearVo>> ri = new ReturnInfo<>();
        List<VariableYearVo> listData= yearService.getSchoolYear();
        ri.setData(listData);
        return ri;
    }
    /**
     * @Author: gll
     * @Description:保存或修改学年
     * @CreateDate: 2018/4/27 15:45
    */
    @PostMapping(value = "/saveOrUpdateYear")
    @ApiOperation(value = "保存或修改学年", httpMethod = "POST", notes = "保存或修改学年")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",value = "学年",name = "vaYearNum",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",value = "标识id",name = "vaId",required = false,dataType = "Long")
    })
    public ReturnInfo<Boolean> saveOrUpdateYear(Long vaId, String vaYearNum) {
        ReturnInfo<Boolean> result = new ReturnInfo<>();
        boolean flag = yearService.saveOrUpdateYear(vaId,vaYearNum);
        result.setData(flag);
        return result;
    }
/**
 * @Author: gll
 * @Description: 删除方法
 * @CreateDate: 2018/4/27 18:10
*/
    @PostMapping(value = "/deleteYear")
    @ApiOperation(value = "删除学年", httpMethod = "POST", notes = "删除学年")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",value = "标识id",name = "vaId",required = true,dataType = "Long")
    })
    public ReturnInfo<Boolean> deleteYear(Long vaId) {
        ReturnInfo<Boolean> result = new ReturnInfo<>();
        VariableYear year = new VariableYear();
        year.setVaId(vaId);
        year.setVaState(StateEnum.DISABLE.getNumCode());
        boolean flag = yearService.updateById(year);
        result.setData(flag);
        return result;
    }
  
 /**
  * @Author: gll
  * @Description:给toon发通知
  * @CreateDate: 2018/4/28 15:50
 */
    public ReturnInfo<Boolean> sendToonNotice(UserBean userBean) {
    	ReturnInfo<Boolean> ri =  new ReturnInfo<>();
    	boolean flag = true;
    	if (userBean == null || userBean.getToonUserId() == null || StringUtils.isBlank(userBean.getToToonFeed())|| StringUtils.isBlank(userBean.getFromToonFeed())) {
            throw new BusinessException(MessageCode.PARAMETER_ERROR);
        }
        String businessContent = userBean.getContent();
        if (StringUtils.isNotBlank(businessContent) && businessContent.length()>12) {
            businessContent=businessContent.substring(0,12)+"...";
        }
        String from = userBean.getFromToonFeed();
        String to = userBean.getToToonFeed();
        String toUserId = userBean.getToonUserId().toString();
        Integer bubbleFlag = BubbleEnum.IMPORTANT.getNumCode();
        Integer headFlag = HeadFlagEnum.HAS_ONE_HEAD.getNumCode();
        Integer finishFlag = 0;
        String bizNo = "interaction_" + to + UUID.randomUUID().toString();
        String title = userBean.getTitle();
        String summary = businessContent;
        //String code = userBean.getToonCode();
        Long appId= appInfo.getAppId();
        String appCode = appInfo.getAppKey();
        //Long appId= 768L;
        //String appCode = "754060139e734486817e45bcd456f11e";
        String url = userBean.getUrl();//查看详情的连接
        try {
            //String url = messageUrl +"?entry=classDetail&noId="+userBean.getNoId()+"&code="+code+"&param="+gson.toJson(map);
            MsgContent msg = new MsgContent(url, "查看详情");
            String content = gson.toJson(msg);
			flag = commonMessageService.sendMessage(MessageSendEnum.APPROVE, appId, appCode, from, to, toUserId, title,
					summary, bizNo, content, -1L, bubbleFlag, headFlag, finishFlag);
        } catch (Exception e) {
        	flag = false;
        }
		logger.info(MessageFormat.format("通知结果：{0},bizNo:{1}", flag, bizNo));
        ri.setData(flag);
        return ri;
    }


}
