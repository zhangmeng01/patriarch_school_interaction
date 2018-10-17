package com.yjtoon.school.web.config;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @ClassName AppInfoConfig
 * @Description: 应用信息描述
 * @author: SHENZL
 * @since: 2017/11/27 10:56
 */
@Component
@Scope("singleton")
@DisconfFile(filename = "jytschoolinteraction.properties")
@DisconfUpdateService(classes={AppInfoConfig.class})
public class AppInfoConfig {
    private Long appId;
    private String appKey;

    @DisconfFileItem(name = "app.appId")
    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
    @DisconfFileItem(name = "app.appKey")
    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
