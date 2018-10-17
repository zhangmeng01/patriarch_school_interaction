package com.yjtoon.school.web.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class URLencodeUtil {
	
	public static  String encode(String fileName,String userAgent) throws UnsupportedEncodingException{
        if(userAgent != null && userAgent.toLowerCase().indexOf("firefox") > 0){
			fileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
        }else if(userAgent != null && userAgent.toLowerCase().indexOf("safari") > 0&&userAgent.toLowerCase().indexOf("chrome")==-1){
        	fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1") + "\"";  
        }else{
        	fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20").replaceAll("%28", "\\(").replaceAll("%29", "\\)").replaceAll("%3B", ";").replaceAll("%40", "@").replaceAll("%23", "\\#").replaceAll("%26", "\\&");
        }
		return fileName;
	}

}
