package com.yjtoon.school.web.config;

import com.yjtoon.framework.exception.BusinessException;
import com.yjtoon.framework.exception.ReturnInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName GlobleRestExceptionHandler
 * @Description: 全局rest异常
 * @author: SHENZL
 * @since: 2017/11/10 15:25
 */
@ControllerAdvice
public class GlobleRestExceptionHandler {
private  static final Logger logger = LoggerFactory.getLogger(GlobleRestExceptionHandler.class);

	@ExceptionHandler(value = Exception.class)
//	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ReturnInfo<String> errorHandler(HttpServletRequest request, HttpServletResponse response , Exception e) throws Exception {
		ReturnInfo<String> re = new ReturnInfo<>();
		if(e instanceof BusinessException){
			re.setCode(((BusinessException) e).getCode());
			re.setMessage(e.getMessage());
		}else if (e instanceof MethodArgumentNotValidException){
			MethodArgumentNotValidException mve = (MethodArgumentNotValidException)e;
			BindingResult result = mve.getBindingResult();
			procssVolidateException(result,re);
		}else if (e instanceof BindException){
			BindException be = (BindException)e;
			BindingResult result = be.getBindingResult();
			procssVolidateException(result,re);
		}else {
			re.setCode(500);
			re.setData("");
			re.setMessage(e.getMessage()==null?"空指针异常":e.getMessage());
		}
		return re;

	}
	private void procssVolidateException(BindingResult result ,ReturnInfo<String> re){
		if(result.hasErrors()){
			String message="";
			List<ObjectError> list = result.getAllErrors();
			for(ObjectError objectError:list){
				message+=objectError.getDefaultMessage()+";  ";
			}
			re.setCode(60);
			re.setData("");
			re.setMessage(message);
		}
	}
}
