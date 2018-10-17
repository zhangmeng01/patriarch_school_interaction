package com.yjtoon.school.web.config;

import com.yjtoon.school.web.interceptor.SimpleAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @ClassName WebMvcConfig
 * @Description: 修改spring mvc web 配置  根据业务实现不同接口
 * @author: SHENZL
 * @since: 2017/11/7 17:03
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/html/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/html/");
		registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
		super.addResourceHandlers(registry);
	}

	@Bean
	public  SimpleAuthInterceptor getSimpleAuthInterceptor(){
		return  new SimpleAuthInterceptor();
	}
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getSimpleAuthInterceptor()).addPathPatterns("/jxhd/notice/**","/jxhd/achievement/findHistoryScore","/jxhd/achievement/findMyScoreAndRank");
		//excludePathPatterns("/jxhd/notice/noticeDetail/reveiver","/jxhd/notice/noticeDetail/reveiver/score");
//		registry.addInterceptor(new SimpleAuthInterceptor()).addPathPatterns("/**").excludePathPatterns("/swagger-resources/**", "/v2/**");
		super.addInterceptors(registry);

	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*")
				.allowedMethods("GET", "HEAD", "POST","PUT", "DELETE", "OPTIONS")
				.allowCredentials(true).maxAge(3600);
	}


/*	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("UTF-8");
		resolver.setMaxUploadSize(10485760000L);
		resolver.setMaxInMemorySize(40960);
		return resolver;
	}*/
}
