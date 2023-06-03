package com.juhai.api.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    /**
     * jwt 登录密钥
     */
    private static final String LOGIN_SECRET = "ELsFI#pY!yDQW6E4dmZioONpntc!MZxN";

    /**
     * jwt 支付密钥
     */
    private static final String PAY_SECRET = "HRZZQKhw1jw0Hv5Mmnovk7vc#*I%c*K7";

    /**
     * 登录token 默认一天过期
     */
    public static String getToken(Map<String, String> map) {
        return getToken(map, LOGIN_SECRET, null);
    }

    /**
     * 支付token
     */
    public static String getPayToken(Map<String, String> map, Date date) {
        return getToken(map, PAY_SECRET, date);
    }

    /**
     * 验证登录token
     */
    public static DecodedJWT verify(String token) {
        return verify(token, LOGIN_SECRET);
    }

    /**
     * 验证支付token
     */
    public static DecodedJWT verifyPay(String token) {
        return verify(token, PAY_SECRET);
    }

    /**
     * 生产token
     */
    public static String getToken(Map<String, String> map, String secret, Date date) {
        JWTCreator.Builder builder = JWT.create();

        //payload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });
        //指定令牌的过期时间
        builder.withExpiresAt(date);
        return builder.sign(Algorithm.HMAC256(secret));
    }

    /**
     * 生产token
     */
    public static String getToken(Map<String, String> map, Date date) {
        return getToken(map, LOGIN_SECRET, date);
    }

    /**
     * 验证token
     */
    public static DecodedJWT verify(String token, String secret) {
        //如果有任何验证异常，此处都会抛出异常
        try {
            return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }

    public static String getUserName(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            return null;
        }
        DecodedJWT jwt = JwtUtils.verify(token);
        if (jwt == null) {
            return null;
        }
        return jwt.getClaim("userName").asString();
    }
}
