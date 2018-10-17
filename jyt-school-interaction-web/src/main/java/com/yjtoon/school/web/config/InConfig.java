package com.yjtoon.school.web.config;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Author: gll
 * @Description: 发布通知的参数
 * @CreateDate: 2018/5/3 18:04
*/
@Component
@DisconfFile(filename = "messageset.properties")
public class InConfig {
    private Long appId;
    private String appCode;
    private String url;

    @DisconfFileItem(name = "net.jyt.school.appId", associateField = "appId")
    public Long getAppId(){
        return  appId;
    }

    @DisconfFileItem(name = "net.jyt.school.appCode", associateField = "appCode")
    public String getAppCode(){
        return  appCode;
    }

    @DisconfFileItem(name = "net.jyt.school.message.url", associateField = "url")
    public String getUrl(){
        return  url;
    }


}
