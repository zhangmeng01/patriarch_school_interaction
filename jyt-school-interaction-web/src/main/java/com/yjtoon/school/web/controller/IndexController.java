package com.yjtoon.school.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName LoginController
 * @Description: 用户登录接口
 * @author: SHENZL
 * @since: 2017/11/27 14:11
 */
@Controller
@RequestMapping("/")
@ApiIgnore
public class IndexController {
    private  final  static Logger logger = LoggerFactory.getLogger(IndexController.class);
    @GetMapping("/")
    public void index(HttpServletResponse response) throws IOException {
         response.sendRedirect("/html/index.html");
    }
}
