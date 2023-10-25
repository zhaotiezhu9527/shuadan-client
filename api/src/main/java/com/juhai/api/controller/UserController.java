package com.juhai.api.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
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

    @Autowired
    private DepositService depositService;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderCountService orderCountService;

    @Autowired
    private DayReportService dayReportService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

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
//        String clientIP = ServletUtil.getClientIP(httpServletRequest);
        String clientIP = ServletUtil.getClientIPByHeader(httpServletRequest, "x-original-forwarded-for");

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
        user.setFundsStatus(0);
        user.setRealName(null);
        user.setPhone(null);
        user.setBankName(null);
        user.setBankNo(null);
        user.setBankAddr(null);
        user.setCreditValue(100);
        user.setLevelId(1);
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
        user.setDeposit(new BigDecimal(0));
        user.setWithdraw(new BigDecimal(0));
        user.setBet(new BigDecimal(0));
        user.setIncome(new BigDecimal(0));
        user.setInviteCount(0);
        userService.save(user);

        // 上级代理累计邀请人数
//        userService.update(
//                new UpdateWrapper<User>().lambda()
//                        .eq(User::getUserName, agent.getUserName())
//                        .set(User::getInviteCount, agent.getInviteCount() + 1)
//        );
        userService.update(new UpdateWrapper<User>().lambda().setSql("invite_count = invite_count + " + 1).eq(User::getUserName, agent.getUserName()));

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

        redisTemplate.opsForValue().set(RedisKeyUtil.UserOnlineKey(user.getUserName()), token, RedisKeyUtil.USER_TOKEN_EXPIRE, TimeUnit.MINUTES);
        return R.ok().put("token", token);
    }

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public R login(@Validated LoginRequest request, HttpServletRequest httpServletRequest) {
//        String clientIP = ServletUtil.getClientIP(httpServletRequest);
        String clientIP = ServletUtil.getClientIPByHeader(httpServletRequest, "x-original-forwarded-for");

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
            return R.error(MsgUtil.get("system.login.pwderror"));
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
        // 续期在线用户
        redisTemplate.opsForValue().set(RedisKeyUtil.UserOnlineKey(user.getUserName()), token, RedisKeyUtil.USER_TOKEN_EXPIRE, TimeUnit.MINUTES);
        /** 删除密码输入错误次数 **/
        redisTemplate.delete(incKey);
        return R.ok().put("token", token);
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public R logout(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        redisTemplate.delete(RedisKeyUtil.UserTokenKey(userName));
        redisTemplate.delete(RedisKeyUtil.UserOnlineKey(userName));
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
        temp.put("withdrawFee", level == null ? 0 : level.getWithdrawFee());

        // 头像
        Avatar avatar = user.getAvatar();
        temp.put("avatarUrl", avatar == null ? "" : resourceDomain + avatar.getImgUrl());
        return R.ok().put("data", temp);
    }


    @ApiOperation(value = "VIP等级详情")
    @GetMapping("/viplv/info")
    public R vipLvInfo(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        JoinLambdaWrapper<User> wrapper = new JoinLambdaWrapper<>(User.class);
        wrapper.eq(User::getUserName, userName);
        wrapper.leftJoin(Level.class,Level::getId,User::getLevelId).oneToOneSelect(User::getLevel, Level.class).end();
        wrapper.leftJoin(Avatar.class, Avatar::getId, User::getAvatarId).oneToOneSelect(User::getAvatar, Avatar.class).end();
        User user = userService.joinGetOne(wrapper, User.class);

        JSONObject temp = new JSONObject();
        temp.put("currLevelName", user.getLevel().getLevelName());
        temp.put("dayOrderCount", user.getLevel().getDayOrderCount());

        // 头像
        Map<String, String> params = paramterService.getAllParamByMap();
        String resourceDomain = params.get("resource_domain");
        Avatar avatar = user.getAvatar();
        temp.put("avatarUrl", avatar == null ? "" : resourceDomain + avatar.getImgUrl());

        List<Level> levels = levelService.list(new LambdaQueryWrapper<Level>().orderByAsc(Level::getLevelValue));
        JSONArray levelArr = new JSONArray();
        for (Level level : levels) {
            JSONObject obj = new JSONObject();
            obj.put("levelName", level.getLevelName());
            obj.put("dayWithdrawCount", level.getDayWithdrawCount());
            obj.put("maxWithdrawAmount", level.getMaxWithdrawAmount());
            obj.put("dayOrderCount", level.getDayOrderCount());
            obj.put("commissionRate", level.getCommissionRate());
            obj.put("levelPrice", level.getLevelPrice());
            levelArr.add(obj);
        }
        temp.put("levels", levelArr);

        return R.ok().put("data", temp);
    }

    @ApiOperation(value = "用户收益详情")
    @GetMapping("/income/detail")
    public R incomeDetail(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        User user = userService.getUserByName(userName);

        JSONObject temp = new JSONObject();
        // 用户余额
        temp.put("balance", user.getBalance());
        // 冻结金额
        temp.put("freezeBalance", user.getFreezeBalance());
        temp.put("totalIncome", user.getIncome());
        // 今日收益
        CompletableFuture<Void> todayIncomeFuture = CompletableFuture.runAsync(() -> {
            DayReport todayReport = dayReportService.getOne(
                    new LambdaQueryWrapper<DayReport>()
                            .eq(DayReport::getUserName, user.getUserName())
                            .eq(DayReport::getToday, DateUtil.formatDate(new Date()))
            );
            temp.put("todayIncome", todayReport == null || todayReport.getIncome() == null ? 0 : todayReport.getIncome());
        }, threadPoolExecutor);
        // 昨日收益
        CompletableFuture<Void> yesterdayIncomeFuture = CompletableFuture.runAsync(() -> {
            DayReport yesterdayReport = dayReportService.getOne(
                    new LambdaQueryWrapper<DayReport>()
                            .eq(DayReport::getUserName, user.getUserName())
                            .eq(DayReport::getToday, DateUtil.formatDate(DateUtil.offsetDay(new Date(), -1)))
            );
            temp.put("yesterdayIncome", (yesterdayReport == null || yesterdayReport.getIncome() == null) ? 0 : yesterdayReport.getIncome());
        }, threadPoolExecutor);

        // 今日订单量
        CompletableFuture<Void> orderCountFuture = CompletableFuture.runAsync(() -> {
            OrderCount orderCount = orderCountService.getOne(
                    new LambdaQueryWrapper<OrderCount>()
                            .eq(OrderCount::getUserName, userName)
                            .eq(OrderCount::getToday, DateUtil.formatDate(new Date()))
            );
            temp.put("todayOrderCount", orderCount == null ? 0 : orderCount.getOrderCount());
        }, threadPoolExecutor);
        // 团队报表
        CompletableFuture<Void> yesterdayTeamIncomeFuture = CompletableFuture.runAsync(() -> {
            List<DayReport> teamReports = dayReportService.list(
                    new LambdaQueryWrapper<DayReport>()
                            .eq(DayReport::getToday, DateUtil.formatDate(DateUtil.offsetDay(new Date(), -1)))
                            .like(DayReport::getUserAgentNode, "|" + userName + "|")
                            .in(DayReport::getUserAgentLevel, Arrays.asList(user.getUserAgentLevel() + 1, user.getUserAgentLevel() + 2, user.getUserAgentLevel() + 3))
            );
            BigDecimal yesterdayTeamIncome = new BigDecimal(0);
            for (DayReport teamReport : teamReports) {
                yesterdayTeamIncome = NumberUtil.add(yesterdayTeamIncome, teamReport.getIncome());
            }
            temp.put("yesterdayTeamIncome", yesterdayTeamIncome);
        }, threadPoolExecutor);

        // 合并线程
        CompletableFuture[] args={ todayIncomeFuture, yesterdayIncomeFuture, orderCountFuture, yesterdayTeamIncomeFuture};
        CompletableFuture.allOf(args).join();

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
        String lang = httpServletRequest.getHeader("lang");
        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);

        PageUtils page = accountService.queryPage(params);
        List<Account> list = (List<Account>) page.getList();
        if (CollUtil.isNotEmpty(list)) {
            Map<Integer, String> typeMap = new HashMap<>();
            typeMap.put(1, MsgUtil.get("system.account.type1"));
            typeMap.put(2, MsgUtil.get("system.account.type2"));
            typeMap.put(3, MsgUtil.get("system.account.type3"));
            typeMap.put(4, MsgUtil.get("system.account.type4"));
            typeMap.put(5, MsgUtil.get("system.account.type5"));
            typeMap.put(6, MsgUtil.get("system.account.type6"));
            typeMap.put(8, MsgUtil.get("system.account.type8"));
            JSONArray arr = new JSONArray();
            for (Account temp : list) {
                JSONObject obj = new JSONObject();
                obj.put("amount", temp.getOptAmount());
                obj.put("optTime", StringUtils.equals(lang, "vi_VN") ? DateUtil.format(temp.getOptTime(), "dd-MM-yyyy HH:mm:ss") : temp.getOptTime());
                obj.put("optType", temp.getOptType());
                obj.put("optTypeStr", typeMap.getOrDefault(temp.getOptType(), "-"));
                arr.add(obj);
            }
            page.setList(arr);
        }
        return R.ok().put("page", page);
    }

    @ApiOperation(value = "用户充值记录列表")
    @GetMapping("/deposit/list")
    public R depositList(PageBaseRequest request, HttpServletRequest httpServletRequest) {
        String lang = httpServletRequest.getHeader("lang");
        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);

        PageUtils page = depositService.queryPage(params);
        List<Deposit> list = (List<Deposit>) page.getList();
        if (CollUtil.isNotEmpty(list)) {
            Map<Integer, String> statusMap = new HashMap<>();
            statusMap.put(0, MsgUtil.get("system.deposit.type0"));
            statusMap.put(1, MsgUtil.get("system.deposit.type1"));
            JSONArray arr = new JSONArray();
            for (Deposit temp : list) {
                JSONObject obj = new JSONObject();
//                obj.put("time", temp.getOrderTime());
                obj.put("time", StringUtils.equals(lang, "vi_VN") ? DateUtil.format(temp.getOrderTime(), "dd-MM-yyyy HH:mm:ss") : temp.getOrderTime());
                obj.put("status", temp.getStatus());
                obj.put("statusStr", statusMap.getOrDefault(temp.getStatus(), "-"));
                obj.put("amount", temp.getOptAmount());
                obj.put("orderNo", temp.getOrderNo());
                arr.add(obj);
            }
            page.setList(arr);
        }
        return R.ok().put("page", page);
    }


    @ApiOperation(value = "用户提现记录列表")
    @GetMapping("/withdraw/list")
    public R withdrawList(PageBaseRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        String lang = httpServletRequest.getHeader("lang");
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);

        PageUtils page = withdrawService.queryPage(params);
        List<Withdraw> list = (List<Withdraw>) page.getList();
        if (CollUtil.isNotEmpty(list)) {
            Map<Integer, String> statusMap = new HashMap<>();
            statusMap.put(0, MsgUtil.get("system.withdraw.type0"));
            statusMap.put(1, MsgUtil.get("system.withdraw.type1"));
            statusMap.put(2, MsgUtil.get("system.withdraw.type2"));
            JSONArray arr = new JSONArray();
            for (Withdraw temp : list) {
                JSONObject obj = new JSONObject();
//                obj.put("time", temp.getOrderTime());
                obj.put("time", StringUtils.equals(lang, "vi_VN") ? DateUtil.format(temp.getOrderTime(), "dd-MM-yyyy HH:mm:ss") : temp.getOrderTime());
                obj.put("status", temp.getStatus());
                obj.put("statusStr", statusMap.getOrDefault(temp.getStatus(), "-"));
                obj.put("amount", temp.getOptAmount());
                obj.put("orderNo", temp.getOrderNo());
                arr.add(obj);
            }
            page.setList(arr);
        }
        return R.ok().put("page", page);
    }


    @ApiOperation(value = "根据层级获取用户团队成员")
    @GetMapping("/team/{level}")
    public R teamList(HttpServletRequest httpServletRequest, @PathVariable(value = "level") Integer level) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        String lang = httpServletRequest.getHeader("lang");
        User user = userService.getUserByName(userName);
        List<User> teams = userService.list(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUserAgentLevel, user.getUserAgentLevel() + level)
                        .like(User::getUserAgentNode, "|" + userName + "|")
                        .orderByDesc(User::getDeposit, User::getRegisterTime)
        );
        JSONArray array = new JSONArray();
        for (User temp : teams) {
            JSONObject obj = new JSONObject();
            obj.put("nickName", temp.getNickName());
            obj.put("phone", DesensitizedUtil.mobilePhone(temp.getPhone()));
            obj.put("deposit", temp.getDeposit());
            obj.put("withdraw", temp.getWithdraw());
            obj.put("inviteCount", temp.getInviteCount());
//            obj.put("registerTime", temp.getRegisterTime());
            obj.put("registerTime", StringUtils.equals(lang, "vi_VN") ? DateUtil.format(temp.getRegisterTime(), "dd-MM-yyyy HH:mm:ss") : temp.getRegisterTime());
            array.add(obj);
        }
        return R.ok().put("data", array);
    }

    @ApiOperation(value = "团队报表")
    @GetMapping("/teamReport")
    public R teamReport(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        User user = userService.getUserByName(userName);

        JSONObject obj = new JSONObject();
        obj.put("inviteCount", user.getInviteCount());
        CompletableFuture<Void> teamFuture = CompletableFuture.runAsync(()-> {
            // 团队人数
            List<User> teams = userService.list(
                    new LambdaQueryWrapper<User>()
                            .select(User::getUserName, User::getBalance, User::getDeposit, User::getWithdraw)
                            .like(User::getUserAgentNode, "|" + userName + "|")
                            .gt(User::getUserAgentLevel, user.getUserAgentLevel())
            );
            // 团队余额
            BigDecimal teamBalance = new BigDecimal(0);
            BigDecimal teamDeposit = new BigDecimal(0);
            BigDecimal teamWithdraw = new BigDecimal(0);
            BigDecimal teamBet = new BigDecimal(0);
            BigDecimal teamIncome = new BigDecimal(0);
            Set<String> depositUserSets = new HashSet<>();
            for (User temp : teams) {
                teamBalance = NumberUtil.add(teamBalance, temp.getBalance());
                teamDeposit = NumberUtil.add(teamDeposit, temp.getDeposit());
                teamWithdraw = NumberUtil.add(teamWithdraw, temp.getWithdraw());
                teamBet = NumberUtil.add(teamBet, temp.getBet());
                teamIncome = NumberUtil.add(teamIncome, temp.getIncome());
                if (temp.getDeposit().doubleValue() > 0) {
                    depositUserSets.add(temp.getUserName());
                }
            }
            obj.put("teamBet", teamBet);
            obj.put("teamIncome", teamIncome);
            obj.put("teamMemberCount", teams.size());
            obj.put("teamBalance", teamBalance);
            obj.put("teamDeposit", teamDeposit);
            obj.put("teamWithdraw", teamWithdraw);
            obj.put("depositCount", depositUserSets.size());
        }, threadPoolExecutor);

        Date now = new Date();
        // 今日注册
        CompletableFuture<Void> registerFuture = CompletableFuture.runAsync(() -> {
            long newRegisterCount = userService.count(
                    new LambdaQueryWrapper<User>()
                            .between(User::getRegisterTime, DateUtil.beginOfDay(now), DateUtil.endOfDay(now))
                            .like(User::getUserAgentNode, "|" + userName + "|")
                            .gt(User::getUserAgentLevel, user.getUserAgentLevel())
            );
            obj.put("newRegisterCount", newRegisterCount);
        }, threadPoolExecutor);

        // 活动人数(今日登陆)
        CompletableFuture<Void> activeCountFuture = CompletableFuture.runAsync(() -> {
            long activeCount = userService.count(
                    new LambdaQueryWrapper<User>()
                            .between(User::getLastTime, DateUtil.beginOfDay(now), DateUtil.endOfDay(now))
                            .like(User::getUserAgentNode, "|" + userName + "|")
                            .gt(User::getUserAgentLevel, user.getUserAgentLevel())
            );
            obj.put("activeCount", activeCount);
        }, threadPoolExecutor);

        // 合并线程
        CompletableFuture[] args={ teamFuture, registerFuture, activeCountFuture };
        CompletableFuture.allOf(args).join();

        return R.ok().put("data", obj);
    }

    @ApiOperation(value = "用户订单列表")
    @GetMapping("/order/list")
    public R orderList(OrderListRequest request, HttpServletRequest httpServletRequest) {
        String lang = httpServletRequest.getHeader("lang");
        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);
        params.put("status", request.getStatus());

        PageUtils page = orderService.queryPage(params);
        List<Order> list = (List<Order>) page.getList();
        if (CollUtil.isNotEmpty(list)) {
            Map<String, String> paramsMap = paramterService.getAllParamByMap();
            String resourceDomain = paramsMap.get("resource_domain");

            Map<Integer, String> statusMap = new HashMap<>();
            statusMap.put(0, MsgUtil.get("system.order.type0"));
            statusMap.put(1, MsgUtil.get("system.order.type1"));
            statusMap.put(2, MsgUtil.get("system.order.type2"));
            statusMap.put(3, MsgUtil.get("system.order.type3"));
            statusMap.put(4, MsgUtil.get("system.order.type4"));
            JSONArray arr = new JSONArray();
            for (Order temp : list) {
                JSONObject obj = new JSONObject();
//                obj.put("orderTime", temp.getOrderTime());
                obj.put("orderTime", StringUtils.equals(lang, "vi_VN") ? DateUtil.format(temp.getOrderTime(), "dd-MM-yyyy HH:mm:ss") : temp.getOrderTime());
                obj.put("orderNo", temp.getOrderNo());
                obj.put("dayOrderCount", temp.getCountNum());
                temp.setStatus(temp.getStatus().intValue() == 4 ? 0 : temp.getStatus());
                obj.put("status", temp.getStatus());
                obj.put("statusStr", statusMap.getOrDefault(temp.getStatus(), "-"));
                obj.put("orderAmount", temp.getOrderAmount());
                BigDecimal commissionRate = NumberUtil.div(temp.getCommissionRate(), 100);
                BigDecimal commission = NumberUtil.mul(temp.getOrderAmount(), commissionRate, temp.getCommissionMul());
                obj.put("commission", commission.stripTrailingZeros());
                obj.put("returnAmount", NumberUtil.add(temp.getOrderAmount(), commission).stripTrailingZeros());
                obj.put("goodsCount", temp.getGoodsCount());
                obj.put("goodsPrice", temp.getGoodsPrice());
                Goods goods = temp.getGoods();
                obj.put("goodsName", goods == null ? "" : goods.getGoodsName());
                obj.put("goodsImg", goods == null ? "" : resourceDomain + goods.getGoodsImg());
                obj.put("orderType", temp.getOrderType());
                obj.put("commissionMul", temp.getCommissionMul());
                obj.put("promptText", temp.getPromptText());
                arr.add(obj);
            }
            page.setList(arr);
        }
        return R.ok().put("page", page);
    }


    @Transactional
    @ApiOperation(value = "用户提现")
    @PostMapping("/withdraw")
    public R withdraw(@Validated WithdrawRequest request, HttpServletRequest httpServletRequest) throws Exception {

        Date now = new Date();
        Map<String, String> params = paramterService.getAllParamByMap();
        // 验证时间段
        String withdrawTimeStr = params.get("withdraw_time");
        if (StringUtils.isNotBlank(withdrawTimeStr)) {
            String today = DateUtil.formatDate(now);
            String[] timeArr = withdrawTimeStr.split("-");
            Date beginTime = DateUtil.parse(today + " " + timeArr[0]);
            Date endTime = DateUtil.parse(today + " " + timeArr[1]);
            if (!DateUtil.isIn(now, beginTime, endTime)) {
                return R.error(MsgUtil.get("system.withdraw.time") + ":" + withdrawTimeStr);
            }
        }

        String userName = JwtUtils.getUserName(httpServletRequest);

        // 用户信息
        JoinLambdaWrapper<User> wrapper = new JoinLambdaWrapper<>(User.class);
        wrapper.eq(User::getUserName, userName);
        wrapper.leftJoin(Level.class,Level::getId,User::getLevelId).oneToOneSelect(User::getLevel, Level.class).end();
        User user = userService.joinGetOne(wrapper, User.class);

        // 验证资金冻结
        if (user.getFundsStatus().intValue() == 1) {
            return R.error(MsgUtil.get("system.user.enable"));
        }

        // 验证提现金额
        BigDecimal amount = new BigDecimal(request.getAmount());

        Double leastWithdrawAmount = user.getLevel().getMinWithdrawAmount().doubleValue();
        Double largestWithdrawAmount = user.getLevel().getMaxWithdrawAmount().doubleValue();
        if ((leastWithdrawAmount != 0 && amount.doubleValue() < leastWithdrawAmount)
                || (largestWithdrawAmount != 0 && amount.doubleValue() > largestWithdrawAmount)) {
            return R.error(StrUtil.format(MsgUtil.get("system.withdraw.limitamount"), leastWithdrawAmount, largestWithdrawAmount));
        }

        // 验证支付密码
        String pwd = SecureUtil.md5(request.getPwd());
        if (!StringUtils.equals(pwd, user.getPayPwd())) {
            return R.error(MsgUtil.get("system.order.paypwderror"));
        }

        if (StringUtils.isBlank(user.getRealName()) || StringUtils.isBlank(user.getBankNo())) {
            return R.error(MsgUtil.get("system.withdraw.nobank"));
        }
        if (user.getStatus().intValue() == 1) {
            return R.error(MsgUtil.get("system.user.enable"));
        }
        if (user.getBalance().doubleValue() < amount.doubleValue()) {
            return R.error(MsgUtil.get("system.order.balance"));
        }

        // 查询是否还有待审核的订单
        long noFinish = withdrawService.count(
                new LambdaQueryWrapper<Withdraw>()
                        .eq(Withdraw::getUserName, user.getUserName())
                        .eq(Withdraw::getStatus, 0)
        );
        if (noFinish > 0) {
            return R.error(MsgUtil.get("system.withdraw.hasorder"));
        }

        // 校验今日订单量是否满足
        OrderCount orderCount = orderCountService.getOne(
                new LambdaQueryWrapper<OrderCount>()
                        .eq(OrderCount::getUserName, userName)
                        .eq(OrderCount::getToday, DateUtil.formatDate(now))
        );
        int countNum = orderCount == null ? 0 : orderCount.getOrderCount();
        if (user.getLevel().getWithdrawOrderCount() > countNum) {
            String msg = StrUtil.format(MsgUtil.get("system.withdraw.ordercount"), user.getLevel().getWithdrawOrderCount() - countNum);
            return R.error(msg);
        }

        // 校验今日提现次数
        long finish = withdrawService.count(
                new LambdaQueryWrapper<Withdraw>()
                        .eq(Withdraw::getUserName, user.getUserName())
                        .eq(Withdraw::getStatus, 1)
                        .between(Withdraw::getOrderTime, DateUtil.beginOfDay(now), DateUtil.endOfDay(now))
        );
        if (finish >= user.getLevel().getDayWithdrawCount()) {
            return R.error(MsgUtil.get("system.withdraw.maxorder"));
        }

        // 扣钱
        userService.updateUserBalance(userName, amount.negate());

        String orderNo = IdUtil.getSnowflakeNextIdStr();
        // 提现记录
        Withdraw withdraw = new Withdraw();
        withdraw.setOrderNo(orderNo);
        withdraw.setUserName(user.getUserName());
        withdraw.setNickName(user.getNickName());
        withdraw.setOptAmount(amount);
        BigDecimal feeRate = user.getLevel().getWithdrawFee();
        withdraw.setFeeRate(feeRate);
        BigDecimal fee = NumberUtil.mul(amount, NumberUtil.div(feeRate, 100));
        withdraw.setRealAmount(NumberUtil.sub(amount, fee));
        withdraw.setBankName(user.getBankName());
        withdraw.setRealName(user.getRealName());
        withdraw.setBankNo(user.getBankNo());
        withdraw.setPhone(user.getPhone());
        withdraw.setOrderTime(now);
        withdraw.setCheckTime(null);
        withdraw.setStatus(0);
        withdraw.setUpdateBy(null);
        withdraw.setRemark(null);
        withdraw.setUserAgent(user.getUserAgent());
        withdraw.setUserAgentNode(user.getUserAgentNode());
        withdraw.setUserAgentLevel(user.getUserAgentLevel());
        withdrawService.save(withdraw);

        // 添加流水记录
        Account account = new Account();
        account.setAccountNo(IdUtil.getSnowflakeNextIdStr());
        account.setUserName(userName);
        account.setOptAmount(amount.negate());
        account.setType(2);
        account.setOptType(2);
        account.setUserAgent(user.getUserAgent());
        account.setUserAgentNode(user.getUserAgentNode());
        account.setUserAgentLevel(user.getUserAgentLevel());
        account.setRefNo(orderNo);
        account.setOptTime(now);
        account.setRemark("提现金额:" + amount + "元");
        accountService.save(account);
        return R.ok(MsgUtil.get("system.withdraw.success"));
    }
}
