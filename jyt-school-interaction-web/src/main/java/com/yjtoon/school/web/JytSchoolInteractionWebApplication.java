package com.yjtoon.school.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan(basePackages = "com.yjtoon")
//用webfilter 需要加次注解
@ServletComponentScan
@ImportResource(locations = {"classpath:disconf.xml","classpath:dubbo-customer.xml"})//引入disconf
public class JytSchoolInteractionWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(JytSchoolInteractionWebApplication.class, args);
	}
}
