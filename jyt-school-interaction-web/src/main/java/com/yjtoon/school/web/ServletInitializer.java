package com.yjtoon.school.web;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @ClassName ServletInitializer
 * @Description:<!-- jar2war -->  jar 修改为war 部署
 * @author: SHENZL
 * @since: 2017/12/19 15:36
 */
public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(JytSchoolInteractionWebApplication.class);
    }
}
