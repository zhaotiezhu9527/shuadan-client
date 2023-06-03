package com.juhai.api.utils;

import cn.hutool.core.util.StrUtil;

public class DataDesensitizeUtils {

    public static String desensitize(String data, int front, int end) {
        if (StrUtil.isBlank(data)) {
            return StrUtil.EMPTY;
        }
        if ((front + end) > data.length()) {
            return StrUtil.EMPTY;
        }
        if (front < 0 || end < 0) {
            return StrUtil.EMPTY;
        }
        return data.substring(0,front) + " **** **** " + data.substring(data.length() - end);
    }
}
