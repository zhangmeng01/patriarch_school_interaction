package com.yjtoon.school.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @Description： 访问网址：http://ip:端口/swagger-ui.html
 * @author: SHENZL
 * @since: 2017/11/16 8:34
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.basePackage}")
    private String basePackage;

    @Value("${swagger.title}")
    private String title;

    @Value("${swagger.description}")
    private String description;

    @Value("${swagger.contact}")
    private String contact;

    @Value("${swagger.version}")
    private String version;

    @Value("${swagger.enable}")
    private boolean enable;

    @Bean
    public Docket createRedisApi() {
        return new Docket(DocumentationType.SWAGGER_2).enable(enable).apiInfo(apiInfo()).select()// 函数返回一个ApiSelectorBuilder实例用来控制哪些接口暴露给Swagger来展现
                .apis(RequestHandlerSelectors.basePackage(basePackage)).paths(PathSelectors.any()).build();
    }

    // apiInfo()用来创建该Api的基本信息（这些基本信息会展现在文档页面中）
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(title) // 大标题
                .description(description).contact(contact).version(version).build();
    }
}
