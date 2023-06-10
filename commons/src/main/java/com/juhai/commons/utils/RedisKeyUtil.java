package com.juhai.commons.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Date;

public class RedisKeyUtil {

    /**
     * 用户过期时间
     */
    public static final int USER_TOKEN_EXPIRE = 15;

    /**
     * 用户登录token key
     * @param userId 用户Id
     * @return
     */
    public static String UserTokenKey(Object userId) {
        String template = "user:login:token:{}";
        return StrUtil.format(template, userId);
    }

    public static String UserOnlineKey(Object userId) {
        String template = "user:online:token:{}";
        return StrUtil.format(template, userId);
    }

    public static String UserOrderCount(Object userName) {
        String template = "user:order:{}:{}";
        return StrUtil.format(template, DateUtil.format(new Date(), "yyyyMMdd"), userName);
    }

    /**
     * 用户提现通知 key
     * @return
     */
    public static String UserWithdrawNotice() {
        String template = "user_withdraw_notice";
        return template;
    }

    /**
     * 用户充值通知 key
     * @return
     */
    public static String UserTopupNotice() {
        String template = "user_topup_notice";
        return template;
    }

    /**
     * 发送注册短信key
     * @param phone 手机号
     * @return
     */
    public static String SmsRegisterKey(String phone) {
        String template = "sms:register:{}";
        return StrUtil.format(template, phone);
    }

    /**
     * 发送登录短信key
     * @param phone 手机号
     * @return
     */
    public static String SmsLoginKey(String phone) {
        String template = "sms:login:{}";
        return StrUtil.format(template, phone);
    }

    /**
     * 发送忘记密码短信key
     * @param phone 手机号
     * @return
     */
    public static String SmsForgotPwdKey(String phone) {
        String template = "sms:forgotpwd:{}";
        return StrUtil.format(template, phone);
    }

    /**
     * 发送忘记提现密码短信key
     * @param phone 手机号
     * @return
     */
    public static String SmsWithdrawPwdKey(String phone) {
        String template = "sms:withdraw:{}";
        return StrUtil.format(template, phone);
    }

    /**
     * 发送忘记提币绑定地址短信key
     * @param phone 手机号
     * @return
     */
    public static String SmsWithdrawBindAddrKey(String phone) {
        String template = "sms:withdrawbindadd:{}";
        return StrUtil.format(template, phone);
    }

    /**
     * 发送提现短信key
     * @param phone 手机号
     * @return
     */
    public static String SmsWithdrawKey(String phone) {
        String template = "sms:withdraw:{}";
        return StrUtil.format(template, phone);
    }

    /**
     * 每日发送短信上限 key
     * @param phone 手机号
     * @return
     */
    public static String SmsDayMaxKey(String phone) {
        String template = "sms:inc:{}:{}";
        return StrUtil.format(template, DateUtil.today(), phone);
    }

    /**
     * 用户密码登录错误次数上限 key
     * @param phone 手机号
     * @return
     */
    public static String LoginPwdErrorKey(String phone) {
        String template = "user:password:error:{}:{}";
        return StrUtil.format(template, DateUtil.today(), phone);
    }

    /**
     * 用户每日抢单收益 key
     * @param phone
     * @return
     */
    public static String UserDayGrabCommissionKey(String phone) {
        String template = "user:grab:commission:{}";
        return StrUtil.format(template, phone);
    }

    /**
     * 用户每日抢单量 key
     * @param phone
     * @return
     */
    public static String UserDayGrabCountKey(String phone) {
        String template = "user:grab:count:{}";
        return StrUtil.format(template, phone);
    }

    /**
     * 用户每日充值量 key
     * @param phone
     * @return
     */
    public static String UserDayTopUpKey(String phone) {
        String template = "user:topup:{}";
        return StrUtil.format(template, phone);
    }

    /**
     * 用户每日彩金量 key
     * @param phone
     * @return
     */
    public static String UserDayRechargeKey(String phone) {
        String template = "user:recharge:{}";
        return StrUtil.format(template, phone, DateUtil.today());
    }

    /**
     * 用户每日提现量 key
     * @param phone
     * @return
     */
    public static String UserDayWithdrawKey(String phone) {
        String template = "user:withdraw:{}";
        return StrUtil.format(template, phone);
    }

    /**
     * 用户注册验证码 key
     * @param uuid
     * @return
     */
    public static String RegKaptchaKey(String uuid) {
        String template = "reg:kaptcha:{}";
        return StrUtil.format(template, uuid);
    }

    /**
     * 用户签到
     * @param userName
     * @return
     */
    public static String UserSignKey(String userName) {
        String template = "user:sign:{}:{}";
        return StrUtil.format(template, DateUtil.today(), userName);
    }
}
