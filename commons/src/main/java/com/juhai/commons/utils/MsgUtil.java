package com.juhai.commons.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.juhai.commons.enums.ResultEnum;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @author Administrator
 */
public class MsgUtil {

    public static String get(String code) {
        try {
            MessageSource messageSource = SpringUtil.getBean("messageSource");
            Locale local = LocaleContextHolder.getLocale();
            //String language = local.getLanguage();
            //if (StringUtils.equals(language, "es")) {
            //    return LangConstant.LANG_ES_MAP.get(code);
            //}else {
            //    return LangConstant.LANG_EN_MAP.get(code);
            //}
            return messageSource.getMessage(code, null, local);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultEnum.ERROR.getMsg();
    }
}
