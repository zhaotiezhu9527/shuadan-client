package com.juhai.api.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class LanguageResolver implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale locale = null;
        //例如 zh_CN  en_US
        final String language = request.getHeader("lang");
        if(StringUtils.isNotEmpty(language)){
            final String[] s = language.split("_");
            // s[0]:国家 s[1]:地区
            locale = new Locale(s[0],s[1]);
            return locale;
        }
        // 如果没有请求参数就是默认的
        locale = Locale.getDefault();
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
