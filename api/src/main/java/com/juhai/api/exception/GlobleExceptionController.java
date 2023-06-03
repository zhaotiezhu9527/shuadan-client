/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.juhai.api.exception;

import cn.hutool.core.collection.CollUtil;
import com.juhai.commons.utils.MsgUtil;
import com.juhai.commons.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 异常处理器
 *
 * @author Mark sunlightcs@gmail.com
 */
@Slf4j
@RestControllerAdvice
public class GlobleExceptionController {

	@Autowired
	private MessageSource messageSource;

	/**
	 * validation 捕获 MethodArgumentNotValidException 异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public R serviceHandle(MethodArgumentNotValidException e) {
		List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
		if (CollUtil.isNotEmpty(allErrors)) {
			String message = MsgUtil.get(allErrors.get(0).getDefaultMessage());
			if (StringUtils.isBlank(message)) {
				return R.error(allErrors.get(0).getDefaultMessage());
			}
		}
		return R.error(MsgUtil.get("system.failure"));
	}
	/**
	 * validation 捕获 BindException 异常
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(BindException.class)
	public R handleBindException(BindException ex) {
		//ex.printStackTrace();
		List<ObjectError> allErrors = ex.getAllErrors();
		if (CollUtil.isNotEmpty(allErrors)) {
			String message = MsgUtil.get(allErrors.get(0).getDefaultMessage());
			if (StringUtils.isBlank(message)) {
				return R.error(allErrors.get(0).getDefaultMessage());
			}
			return R.error(message);
		}
		return R.error(MsgUtil.get("system.failure"));
	}
	/**
	 * validation拦截 ConstraintViolationException 异常
	 * 用于单个异常校验
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public R handleBindException(ConstraintViolationException ex) {
		//ex.printStackTrace();
		return R.error(MsgUtil.get("system.failure"));
	}

	@ExceptionHandler(Exception.class)
	public R handleException(Exception e){
		e.printStackTrace();
		return R.error(MsgUtil.get("system.failure"));
	}
}
