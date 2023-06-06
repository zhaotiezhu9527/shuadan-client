package com.juhai.api.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.juhai.api.controller.request.*;
import com.juhai.api.utils.JwtUtils;
import com.juhai.commons.constants.Constant;
import com.juhai.commons.entity.*;
import com.juhai.commons.service.*;
import com.juhai.commons.utils.MsgUtil;
import com.juhai.commons.utils.PageUtils;
import com.juhai.commons.utils.R;
import com.juhai.commons.utils.RedisKeyUtil;
import icu.mhb.mybatisplus.plugln.core.JoinLambdaWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @Autowired
    private LevelService levelService;

    @Autowired
    private AccountService accountService;

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

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public R logout(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        redisTemplate.delete(RedisKeyUtil.UserTokenKey(userName));
        return R.ok();
    }

    @ApiOperation(value = "用户信息")
    @GetMapping("/info")
    public R info(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        JoinLambdaWrapper<User> wrapper = new JoinLambdaWrapper<>(User.class);
        wrapper.eq(User::getUserName, userName);
        wrapper.leftJoin(Level.class,Level::getId,User::getLevelId).oneToOneSelect(User::getLevel, Level.class).end();
        wrapper.leftJoin(Avatar.class, Avatar::getId, User::getAvatarId).oneToOneSelect(User::getAvatar, Avatar.class).end();
        JSONObject temp = new JSONObject();
        User user = userService.joinGetOne(wrapper, User.class);

        temp.put("userName", user.getUserName());
        temp.put("nickName", user.getNickName());
        temp.put("balance", user.getBalance());
        temp.put("freezeBalance", user.getFreezeBalance());
        temp.put("realName", user.getRealName());
        temp.put("phone", DesensitizedUtil.mobilePhone(user.getPhone()));
        temp.put("bankName", user.getBankName());
        temp.put("bankNo", DesensitizedUtil.bankCard(user.getBankNo()));
        temp.put("bankAddr", user.getBankAddr());
        temp.put("creditValue", user.getCreditValue());
        temp.put("inviteCode", user.getInviteCode());

        Map<String, String> params = paramterService.getAllParamByMap();
        String resourceDomain = params.get("resource_domain");
        // 等级信息
        Level level = user.getLevel();
        temp.put("levelName", level == null ? "" : level.getLevelName());
        temp.put("levelIcon", level == null ? "" : resourceDomain + level.getLevelIcon());

        // 头像
        Avatar avatar = user.getAvatar();
        temp.put("avatarUrl", avatar == null ? "" : resourceDomain + avatar.getImgUrl());

        return R.ok().put("data", temp);
    }


    @ApiOperation(value = "用户修改昵称")
    @PostMapping("/update/nickName")
    public R bindUsdt(@Validated UpdNickNameRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        userService.update(
                new UpdateWrapper<User>().lambda()
                        .set(User::getNickName, request.getNickName())
                        .eq(User::getUserName, userName)
        );

        return R.ok();
    }

    @ApiOperation(value = "修改用户密码")
    @PostMapping("/updatePwd")
    public R updatePwd(@Validated UpdatePwdRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        User user = userService.getUserByName(userName);

        String oldPwd = SecureUtil.md5(request.getOldPwd());
        if (!StringUtils.equals(oldPwd, user.getLoginPwd())) {
            return R.error(MsgUtil.get("system.user.oldpwderror"));
        }

        userService.update(
                new UpdateWrapper<User>().lambda()
                        .set(User::getLoginPwd, SecureUtil.md5(request.getNewPwd()))
                        .eq(User::getUserName, userName)
        );

        return R.ok();
    }

    @ApiOperation(value = "修改用户支付密码")
    @PostMapping("/updatePayPwd")
    public R updatePayPwd(@Validated UpdatePwdRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        User user = userService.getUserByName(userName);

        String oldPwd = SecureUtil.md5(request.getOldPwd());
        if (!StringUtils.equals(oldPwd, user.getPayPwd())) {
            return R.error(MsgUtil.get("system.user.oldpwderror"));
        }

        userService.update(
                new UpdateWrapper<User>().lambda()
                        .set(User::getPayPwd, SecureUtil.md5(request.getNewPwd()))
                        .eq(User::getUserName, userName)
        );

        return R.ok();
    }

    @ApiOperation(value = "用户绑定银行卡")
    @PostMapping("/bindBank")
    public R bindBank(@Validated BindBankRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        User user = userService.getUserByName(userName);
        if (StringUtils.isNotBlank(user.getBankNo())) {
            return R.error(MsgUtil.get("system.user.bindbank"));
        }

        userService.update(
                new UpdateWrapper<User>().lambda()
                        .set(User::getBankName, request.getBankName())
                        .set(User::getBankNo, request.getCardNo())
                        .set(User::getBankAddr, request.getAddr())
                        .set(User::getRealName, request.getRealName())
                        .set(User::getPhone, request.getPhone())
                        .eq(User::getUserName, userName)
        );

        return R.ok();
    }

    @ApiOperation(value = "用户资金流动列表")
    @GetMapping("/account/list")
    public R accountList(PageBaseRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);

        PageUtils page = accountService.queryPage(params);
        List<Account> list = (List<Account>) page.getList();
        if (CollUtil.isNotEmpty(list)) {
            Map<Integer, String> typeMap = new HashMap<>();
            typeMap.put(1, "用户充值");
            typeMap.put(2, "用户提现");
            typeMap.put(3, "用户接单");
            typeMap.put(4, "接单返佣");
            typeMap.put(5, "下级返佣");
            JSONArray arr = new JSONArray();
            for (Account temp : list) {
                JSONObject obj = new JSONObject();
                obj.put("amount", temp.getOptAmount());
                obj.put("optTime", temp.getOptTime());
                obj.put("optType", temp.getOptType());
                obj.put("optTypeStr", typeMap.getOrDefault(temp.getOptType(), "未知"));
                arr.add(obj);
            }
            page.setList(arr);
        }
        return R.ok().put("page", page);
    }
}
