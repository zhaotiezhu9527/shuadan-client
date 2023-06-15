package com.juhai.api.aop;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.juhai.api.utils.JwtUtils;
import com.juhai.commons.enums.ResultEnum;
import com.juhai.commons.utils.MsgUtil;
import com.juhai.commons.utils.R;
import com.juhai.commons.utils.RedisKeyUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
@Component
@Aspect
@ConfigurationProperties(prefix = "login.auth")
public class LoginAspect {

    @Value("${token.expire}")
    private int expire;

    private List<String> urls;

    private static AntPathMatcher matcher = new AntPathMatcher();

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut("execution(public * com.juhai.api.controller.*.*(..))")
    public void loginAsp(){}

    @Around("loginAsp()")
    public Object login(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        log.info("请求地址:" + request.getRequestURL());
        log.info("请求方式:" + request.getMethod());
        log.info("请求参数:" + JSONUtil.toJsonStr(request.getParameterMap()));
        for (String pattern : urls) {
            boolean match = matcher.match(pattern, request.getRequestURI());
            if (match) {
                return joinPoint.proceed();
            }
        }

        /** 校验token合法性 **/
        String token = request.getHeader("token");

        DecodedJWT jwt = JwtUtils.verify(token);
        if (jwt == null) {
            log.info("token解析失败,重新登录..");
            return R.error(ResultEnum.INVALID_TOKEN.getCode(), MsgUtil.get("system.token.invalid"));
        }

        String jwtUserPhone = jwt.getClaim("userName").asString();

        String tokenKey = RedisKeyUtil.UserTokenKey(jwtUserPhone);
        String cacheToken = redisTemplate.opsForValue().get(tokenKey);

        if (StringUtils.isEmpty(cacheToken) || !StringUtils.equals(cacheToken, token)) {
            log.info("用户token不存在或为空,重新登录..");
            return R.error(ResultEnum.INVALID_TOKEN.getCode(),MsgUtil.get("system.token.invalid"));
        }

        // token续期
        redisTemplate.expireAt(tokenKey, DateUtil.offsetMinute(new Date(), expire));
        // 在线用户续期
        redisTemplate.opsForValue().set(RedisKeyUtil.UserOnlineKey(jwtUserPhone), token, RedisKeyUtil.USER_TOKEN_EXPIRE, TimeUnit.MINUTES);
        return joinPoint.proceed();
    }

    @AfterReturning(value = "loginAsp()", returning="returnValue")
    public void logMethodCall(JoinPoint jp, Object returnValue) throws Throwable {
        log.info("请求响应:{}", JSONUtil.toJsonStr(returnValue));
    }
}
