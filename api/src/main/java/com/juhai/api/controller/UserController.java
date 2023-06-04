package com.juhai.api.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.juhai.api.controller.request.LoginRequest;
import com.juhai.api.controller.request.UserRegisterRequest;
import com.juhai.api.utils.JwtUtils;
import com.juhai.commons.entity.Avatar;
import com.juhai.commons.entity.User;
import com.juhai.commons.entity.UserLog;
import com.juhai.commons.service.AvatarService;
import com.juhai.commons.service.ParamterService;
import com.juhai.commons.service.UserLogService;
import com.juhai.commons.service.UserService;
import com.juhai.commons.utils.MsgUtil;
import com.juhai.commons.utils.R;
import com.juhai.commons.utils.RedisKeyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Api(value = "用户相关", tags = "用户")
@RequestMapping("/user")
@RestController
public class UserController {

    @Value("${token.expire}")
    private int expire;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserLogService userLogService;

    @Autowired
    private ParamterService paramterService;

    @Autowired
    private AvatarService avatarService;

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public R register(@Validated UserRegisterRequest request, HttpServletRequest httpServletRequest) {
        // 查询用户名是否存在
        long exist = userService.count(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUserName, request.getUserName())
        );
        if (exist > 0) {
            return R.error(MsgUtil.get("system.user.register.exist"));
        }

        // 查询代理用户
        User agent = userService.getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getInviteCode, request.getInviteCode())
        );
        if (agent == null) {
            return R.error(MsgUtil.get("system.user.register.invitecode"));
        }

        Date now = new Date();
        String clientIP = ServletUtil.getClientIP(httpServletRequest);

        // 获取头像列表
        List<Avatar> avatars = avatarService.list(
                new LambdaQueryWrapper<Avatar>()
                        .eq(Avatar::getStatus, 0)
        );
        Collections.shuffle(avatars);

        User user = new User();
        user.setUserName(request.getUserName());
        user.setNickName(request.getNickName());
        user.setBalance(new BigDecimal("0"));
        user.setFreezeBalance(new BigDecimal("0"));
        user.setLoginPwd(SecureUtil.md5(request.getLoginPwd()));
        user.setPayPwd(SecureUtil.md5(request.getPayPwd()));
        user.setStatus(0);
        user.setRealName(null);
        user.setPhone(null);
        user.setBankName(null);
        user.setBankNo(null);
        user.setBankAddr(null);
        user.setCreditValue(100);
        user.setLevelId(0);
        user.setAvatarId(avatars.get(0).getId());
        user.setInviteCode(RandomUtil.randomNumbers(6));
        user.setUserAgent(agent.getUserName());
        user.setUserAgentNode(agent.getUserAgentNode() + request.getUserName() + "|");
        user.setUserAgentLevel(agent.getUserAgentLevel() + 1);
        user.setRegisterTime(now);
        user.setRegisterIp(clientIP);
        user.setLastTime(now);
        user.setLastIp(clientIP);
        user.setCreateTime(now);
        user.setCreateBy("system");
        user.setUpdateTime(null);
        user.setUpdateBy(null);
        user.setRemake(null);
        user.setUpdateOrder(1);
        userService.save(user);

        // 登录日志
        UserLog log = new UserLog();
        log.setUserName(user.getUserName());
        log.setIp(clientIP);
        log.setIpDetail(null);
        log.setLoginTime(new Date());
        userLogService.save(log);

        /** 保存token **/
        Map<String, String> map = new HashMap<>();
        map.put("userName", user.getUserName());
        map.put("userIp", clientIP);
        map.put("random", RandomUtil.randomString(6));
        String token = JwtUtils.getToken(map);
        redisTemplate.opsForValue().set(RedisKeyUtil.UserTokenKey(user.getUserName()), token, RedisKeyUtil.USER_TOKEN_EXPIRE, TimeUnit.MINUTES);
        return R.ok().put("token", token);
    }

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public R login(@Validated LoginRequest request, HttpServletRequest httpServletRequest) {
        String clientIP = ServletUtil.getClientIP(httpServletRequest);

        // 查询用户信息
        User user = userService.getUserByName(request.getUserName());
        if (user == null) {
            return R.error(MsgUtil.get("system.user.login.noexist"));
        }

        if (user.getStatus().intValue() == 1) {
            return R.error(MsgUtil.get("system.user.enable"));
        }

        // 获取所有参数配置
        Map<String, String> paramsMap = paramterService.getAllParamByMap();

        String incKey = RedisKeyUtil.LoginPwdErrorKey(user.getUserName());
        /** 每日错误次数上限 **/
        String dayCount = redisTemplate.opsForValue().get(incKey);
        int count = NumberUtils.toInt(dayCount, 0);
        Integer pwdErrCount = MapUtil.getInt(paramsMap, "login_pwd_error", 0);
        if (pwdErrCount > 0 && count >= pwdErrCount) {
            return R.error(MsgUtil.get("system.user.login.pwd.limit"));
        }

        // 验证密码正确
        String pwd = SecureUtil.md5(request.getLoginPwd());
        if (!StringUtils.equals(pwd, user.getLoginPwd())) {
            /** 累计密码错误 **/
            redisTemplate.opsForValue().increment(incKey);
            redisTemplate.expire(incKey, 1, TimeUnit.DAYS);
            return R.error("密码错误");
        }
        if (user.getStatus().intValue() == 1) {
            return R.error(MsgUtil.get("system.user.enable"));
        }

        Date now = new Date();
        /** 更新最后登录时间 **/
        userService.update(
                new UpdateWrapper<User>().lambda()
                        .eq(User::getId, user.getId())
                        .set(User::getLastIp, clientIP)
                        .set(User::getLastTime, now)
        );

        // 登录日志
        UserLog log = new UserLog();
        log.setUserName(user.getUserName());
        log.setIp(clientIP);
        log.setIpDetail(null);
        log.setLoginTime(new Date());
        userLogService.save(log);

        // 多端登录
        String token = redisTemplate.opsForValue().get(RedisKeyUtil.UserTokenKey(user.getUserName()));
        if (StringUtils.isBlank(token)) {
            /** 保存token **/
            Map<String, String> map = new HashMap<>();
            map.put("userName", user.getUserName());
            map.put("userIp", clientIP);
            map.put("random", RandomUtil.randomString(6));
            token = JwtUtils.getToken(map);
            redisTemplate.opsForValue().set(RedisKeyUtil.UserTokenKey(user.getUserName()), token, expire, TimeUnit.MINUTES);
        }
        /** 删除密码输入错误次数 **/
        redisTemplate.delete(incKey);
        return R.ok().put("token", token);
    }
}
