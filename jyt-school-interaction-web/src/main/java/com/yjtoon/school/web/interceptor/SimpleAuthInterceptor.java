package com.yjtoon.school.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.yjtoon.framework.exception.BusinessException;
import com.yjtoon.framework.tooncode.ToonCode;
import com.yjtoon.school.enums.MessageCode;
import com.yjtoon.school.util.Constants;

/**
 * @ClassName SimpleAuthInterceptor
 * @Description: 用户信息过滤器
 * @author: SHENZL
 * @since: 2017/11/23 14:14
 */
public class SimpleAuthInterceptor implements HandlerInterceptor {

    private static  final Logger logger = LoggerFactory.getLogger(SimpleAuthInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        ToonCode toonCode = (ToonCode) request.getSession().getAttribute(Constants.USER_LOGIN_KEY);
        if(toonCode==null){
        	logger.error("未登录，请先登录");
            throw new BusinessException(MessageCode.AUTH_NOT_LOGIN);
        }
        // 只有返回true才会继续向下执行，返回false取消当前请求
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//        System.out.println(">>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//        System.out.println(">>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
    }
}
