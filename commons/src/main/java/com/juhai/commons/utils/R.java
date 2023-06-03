/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.juhai.commons.utils;


import com.juhai.commons.enums.ResultEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	private R() {
		put("code", ResultEnum.SUCCESS.getCode());
		put("msg", MsgUtil.get("system.success"));
	}
	
	public static R error() {

		return error(ResultEnum.ERROR.getCode(), MsgUtil.get("system.failure"));
	}

	public static R error(ResultEnum result) {

		return error(result.getCode(), result.getMsg());
	}

	public static R error(String msg) {

		return error(ResultEnum.ERROR.getCode(), msg);
	}
	
	public static R error(String code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}
	
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}
	
	public static R ok() {
		return new R();
	}

	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
