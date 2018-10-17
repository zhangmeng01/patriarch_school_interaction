package com.yjtoon.school.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName StartupRunner
 * @Description: 程序启动完成后 初始化一些配置
 * @author: SHENZL
 * @since: 2017/11/12 9:55
 */
@Configuration
public class StartupRunner implements CommandLineRunner {
	private  static  final Logger logger = LoggerFactory.getLogger(StartupRunner.class);
	@Override
    public void run(String... strings) throws Exception {

		logger.info("项目启动完成");
	}
}
