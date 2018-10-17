package com.yjtoon.school.cache;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @ClassName RedisProperties
 * @Description: redis配置
 * @author: SHENZL
 * @since: 2017/12/27 10:44
 */
@Component("redisProperties")
@Scope("singleton")
@DisconfFile(filename = "common.properties")
@DisconfUpdateService(classes={RedisProperties.class})
public class RedisProperties {
    private String host;
    private int port;

    /**
     * 地址
     *
     * @return
     */
    @DisconfFileItem(name = "yjtoon.redis.host")
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 端口
     *
     * @return
     */
    @DisconfFileItem(name = "yjtoon.redis.port")
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
