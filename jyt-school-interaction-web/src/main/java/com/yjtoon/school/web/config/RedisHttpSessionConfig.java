package com.yjtoon.school.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * @ClassName RedisHttpSessionConfig
 * @Description: 实现session redis 共享
 * @author: SHENZL
 * @since: 2017/11/10 11:28
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 30*60)
public class RedisHttpSessionConfig {
	/**
	 * session 传递策略
	 * @return
	 */
	@Bean
	public HttpSessionStrategy httpSessionStrategy() {
		CookieHttpSessionStrategy cookieHttpSessionStrategy =new CookieHttpSessionStrategy();
		DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
		defaultCookieSerializer.setCookiePath("/");
		defaultCookieSerializer.setCookieName("JSESSIONID");
		cookieHttpSessionStrategy.setCookieSerializer(defaultCookieSerializer);
		return  cookieHttpSessionStrategy;
//		HeaderHttpSessionStrategy();
	}
	@Bean("springSessionDefaultRedisSerializer")
	public RedisSerializer<Object> getSpringSessionDefaultRedisSerializer(){
		return new RcJdkSerializationRedisSerializer();
	}

}
