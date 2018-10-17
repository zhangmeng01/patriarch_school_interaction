package com.yjtoon.school.web.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *
 * @Description： cookie工具
 * @author: SHENZL
 * @since: 2017/11/16 10:12
 */
public abstract class CookieUtil {
	
    /**
     * 
     * 功能描述: 从cookie中获取值，如果无值则返回null
     * 
     * @param request
     * @param cookieName
     * @return String
     * @version 1.0.0
     * @author shenzll
     */
    public static String getCookieValue(final HttpServletRequest request, String cookieName) {
        final Cookie cookie = getCookie(request, cookieName);
        if (cookie == null) {
            return null;
        }
        try {
            return URLDecoder.decode(cookie.getValue(), "UTF-8");
        } catch (Exception ex) {
            return cookie.getValue();
        }
    }

    /**
     * 
     * 功能描述: 从cookie中获取值，如果无值则返回null
     * 
     * @param request
     * @param name
     * @return Cookie
     * @version 1.0.0
     * @author shenzll
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }
    
    /**
     * 
     * 功能描述: 向response中添加cookie
     * 
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param cookieDomain
     * @param cookiePath
     * @param isCookieSecure
     * @param maxAge void
     * @version 1.0.0
     * @author shenzll
     */
    public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue,
                                 String cookieDomain, String cookiePath, boolean isCookieSecure, Integer maxAge) {
        String cookieValueEncoded = cookieValue;
        try {
            cookieValueEncoded = URLEncoder.encode(cookieValue, "UTF-8");
        } catch (Exception ex) {
            //do nothing
        }
        Cookie cookie = new Cookie(cookieName, cookieValueEncoded);
        if (cookieDomain != null) {
            cookie.setDomain(cookieDomain);
        }
        cookie.setPath(cookiePath);
        if (maxAge != null) {
            cookie.setMaxAge(maxAge);
        }
        if (isCookieSecure) {
            cookie.setSecure(true);
        }
        response.addCookie(cookie);
    }

}
