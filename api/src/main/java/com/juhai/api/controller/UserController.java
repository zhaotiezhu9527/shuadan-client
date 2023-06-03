package com.juhai.api.controller;

import cn.hutool.core.collection.CollUtil;
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
import com.juhai.api.utils.DataDesensitizeUtils;
import com.juhai.api.utils.JwtUtils;
import com.juhai.commons.constants.Constant;
import com.juhai.commons.entity.*;
import com.juhai.commons.service.*;
import com.juhai.commons.utils.MsgUtil;
import com.juhai.commons.utils.PageUtils;
import com.juhai.commons.utils.R;
import com.juhai.commons.utils.RedisKeyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Api(value = "用户相关", tags = "用户")
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ParamterService paramterService;

    @Autowired
    private UserLogService userLogService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private UserReportService userReportService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation(value = "用户信息")
    @GetMapping("/info")
    public R info(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        User user = userService.getUserByName(userName);
        JSONObject temp = new JSONObject();
        temp.put("userId", user.getId());
        temp.put("userName", user.getUserName());
        temp.put("balance", user.getBalance());
        temp.put("realName", user.getRealName());
        temp.put("idCard", DesensitizedUtil.idCardNum(user.getIdCard(), 4, 4));
        temp.put("inviteCode", user.getInviteCode());
        temp.put("walletAddr", DataDesensitizeUtils.desensitize(user.getWalletAddr(), 4 , 4));
        temp.put("bankCardNum", DesensitizedUtil.bankCard(user.getBankCardNum()));
        temp.put("bankName", user.getBankName());
        temp.put("bankAddr", user.getBankAddr());
        temp.put("userLevelName", "普通用户");
        int isRealName = 1;
        if (StringUtils.isNotBlank(user.getRealName()) && StringUtils.isNotBlank(user.getIdCard())) {
            isRealName = 0;
        }

        Map<String, String> params = paramterService.getAllParamByMap();
        temp.put("isRealName", isRealName);
        temp.put("integral", 0);
        temp.put("usdtAmount", NumberUtil.div(user.getBalance(), MapUtil.getDouble(params, "usdt_rate"), 2, RoundingMode.DOWN));

        List<Order> list = orderService.list(
                new LambdaQueryWrapper<Order>()
                        .select(Order::getAmount, Order::getForecastReturnAmount)
                        .eq(Order::getUserName, userName)
                        .eq(Order::getStatus, 0)
        );
        // 待回收利息
        BigDecimal waitReturnInterest = new BigDecimal(0);
        // 待回收本金
        BigDecimal waitReturnPrincipal = new BigDecimal(0);
        for (Order order : list) {
            waitReturnInterest = NumberUtil.add(waitReturnInterest, order.getForecastReturnAmount());
            waitReturnPrincipal = NumberUtil.add(waitReturnPrincipal, order.getAmount());
        }
        
        temp.put("waitReturnInterest", waitReturnInterest);
        temp.put("waitReturnPrincipal", waitReturnPrincipal);

        return R.ok().put("data", temp);
    }

//    @Transactional
    @ApiOperation(value = "用户签到")
    @GetMapping("/sign")
    public R sign(HttpServletRequest httpServletRequest) throws Exception {
        String userName = JwtUtils.getUserName(httpServletRequest);

        String key = "user:sign:lock:" + userName;
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(key, "lock");
        redisTemplate.expire(key, 5, TimeUnit.SECONDS);
        if (lock) {
            String signKey = RedisKeyUtil.UserSignKey(userName);
            // 已经完成签到
            if (redisTemplate.hasKey(signKey)) {
                return R.error(MsgUtil.get("system.user.sign.exist"));
            }
            Map<String, String> paramMap = paramterService.getAllParamByMap();
            // 签到金额小于等于0
            Double signAmount = MapUtil.getDouble(paramMap, "sign_amount", 0.0);
            if (signAmount.doubleValue() > 0) {
                User user = userService.getUserByName(userName);
                // 给用户加钱
                userService.updateUserBalance(userName, new BigDecimal(signAmount));
                // 添加流水记录
                Account account = new Account();
                account.setAccountNo(IdUtil.getSnowflakeNextIdStr());
                account.setUserName(user.getUserName());
                account.setOptAmount(new BigDecimal(signAmount));
                account.setBeforeAmount(user.getBalance());
                account.setAfterAmount(NumberUtil.add(user.getBalance(), signAmount));
                account.setType(1);
                account.setOptType(6);
                account.setOptTime(new Date());
                account.setUserAgent(user.getUserAgent());
                account.setRefNo(null);
                account.setRemark("每日签到,获得奖励" + signAmount + "元");
                accountService.save(account);
                // 设置为今日已签到
                redisTemplate.opsForValue().set(signKey, "sign", 1, TimeUnit.DAYS);
            }
            return R.ok(MsgUtil.get("system.user.sign.success"));
        }
        redisTemplate.delete(key);
        return R.error(MsgUtil.get("system.user.sign.exist"));
    }

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public R register(@Validated UserRegisterRequest request, HttpServletRequest httpServletRequest) {
//        Map<String, String> paramsMap = paramterService.getAllParamByMap();

        // 校验两次密码一致
        if (!StringUtils.equals(request.getConfirmLoginPwd(), request.getLoginPwd())) {
            return R.error(MsgUtil.get("system.user.register.pwd"));
        }
        
        // 查询用户名是否存在
        long exist = userService.count(new LambdaQueryWrapper<User>().eq(User::getUserName, request.getUserName()));
        if (exist > 0) {
            return R.error(MsgUtil.get("system.user.register.exist"));
        }

        // 查询代理用户
        User agent = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getInviteCode, request.getInviteCode()));
        if (agent == null) {
            return R.error(MsgUtil.get("system.user.register.invitecode"));
        }

        String clientIP = ServletUtil.getClientIP(httpServletRequest);

        User user = new User();
        user.setUserName(request.getUserName());
        user.setBalance(new BigDecimal("0"));
        user.setRealName(null);
        user.setIdCard(null);
        user.setLoginPwd(SecureUtil.md5(request.getLoginPwd()));
        user.setPayPwd(SecureUtil.md5(request.getPayPwd()));
        user.setInviteCode(RandomUtil.randomNumbers(6));
        user.setWalletAddr(null);
        user.setBankCardNum(null);
        user.setBankName(null);
        user.setBankAddr(null);
        user.setUserStatus(0);
        user.setUserLevelId(0);
        user.setUserAgent(agent.getUserName());
        user.setRegisterTime(new Date());
        user.setRegisterIp(clientIP);
        user.setLastTime(new Date());
        user.setLastIp(clientIP);
        user.setModifyTime(new Date());
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

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public R logout(HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        redisTemplate.delete(RedisKeyUtil.UserTokenKey(userName));
        return R.ok();
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

        if (user.getUserStatus().intValue() == 1) {
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
        if (user.getUserStatus().intValue() == 1) {
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
            redisTemplate.opsForValue().set(RedisKeyUtil.UserTokenKey(user.getUserName()), token, 15, TimeUnit.MINUTES);
        }
        /** 删除密码输入错误次数 **/
        redisTemplate.delete(incKey);
        return R.ok().put("token", token);
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
            JSONArray arr = new JSONArray();
            for (Account temp : list) {
                JSONObject obj = new JSONObject();
                obj.put("remark", temp.getRemark());
                obj.put("amount", temp.getOptAmount());
                obj.put("optTime", temp.getOptTime());
                arr.add(obj);
            }
            page.setList(arr);
        }
        return R.ok().put("page", page);
    }

    @ApiOperation(value = "用户投资记录列表")
    @GetMapping("/invest/list")
    public R investList(PageBaseRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);

        PageUtils page = orderService.queryPage(params);
        List<Order> list = (List<Order>) page.getList();
        if (CollUtil.isNotEmpty(list)) {
            JSONArray arr = new JSONArray();
            for (Order temp : list) {
                JSONObject obj = new JSONObject();
                obj.put("projectName", temp.getProjectName());
                obj.put("orderNo", temp.getOrderNo());
                obj.put("amount", temp.getAmount());
                obj.put("status", temp.getStatus());
                arr.add(obj);
            }
            page.setList(arr);
        }
        return R.ok().put("page", page);
    }

    @ApiOperation(value = "用户收益记录列表")
    @GetMapping("/profit/list")
    public R profitList(PageBaseRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);
        params.put("status", 1);

        PageUtils page = orderService.queryPage(params);
        List<Order> list = (List<Order>) page.getList();
        if (CollUtil.isNotEmpty(list)) {
            JSONArray arr = new JSONArray();
            for (Order temp : list) {
                JSONObject obj = new JSONObject();
                obj.put("orderNo", temp.getOrderNo());
                obj.put("projectName", temp.getProjectName());
                obj.put("returnTime", temp.getActualReturnTime());
                obj.put("status", temp.getStatus());
                obj.put("amount", NumberUtil.add(temp.getAmount(), temp.getActualReturnAmount()));
                arr.add(obj);
            }
            page.setList(arr);
        }
        return R.ok().put("page", page);
    }

    @ApiOperation(value = "用户充值记录列表")
    @GetMapping("/deposit/list")
    public R depositList(PageBaseRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);

        PageUtils page = depositService.queryPage(params);
        List<Deposit> list = (List<Deposit>) page.getList();
        if (CollUtil.isNotEmpty(list)) {
            JSONArray arr = new JSONArray();
            for (Deposit temp : list) {
                JSONObject obj = new JSONObject();
                obj.put("typeStr", "系统充值");
                obj.put("time", temp.getOptTime());
                obj.put("status", temp.getStatus());
                obj.put("amount", temp.getOptAmount());
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

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, request.getPage());
        params.put(Constant.LIMIT, request.getLimit());
        params.put("userName", userName);

        PageUtils page = withdrawService.queryPage(params);
        List<Withdraw> list = (List<Withdraw>) page.getList();
        if (CollUtil.isNotEmpty(list)) {
            JSONArray arr = new JSONArray();
            for (Withdraw temp : list) {
                JSONObject obj = new JSONObject();
                obj.put("time", temp.getOptTime());
                obj.put("status", temp.getStatus());
                obj.put("amount", temp.getOptAmount());
                arr.add(obj);
            }
            page.setList(arr);
        }
        return R.ok().put("page", page);
    }

    @ApiOperation(value = "用户绑定USDT")
    @PostMapping("/bindUsdt")
    public R bindUsdt(@Validated BindUsdtRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        User user = userService.getUserByName(userName);
        if (StringUtils.isNotBlank(user.getWalletAddr())) {
            return R.error(MsgUtil.get("system.user.bindusdt"));
        }

        userService.update(
                new UpdateWrapper<User>().lambda()
                        .set(User::getWalletAddr, request.getAddr())
                        .set(User::getModifyTime, new Date())
                        .eq(User::getUserName, userName)
        );

        return R.ok();
    }

    @ApiOperation(value = "用户绑定银行卡")
    @PostMapping("/bindBank")
    public R bindBank(@Validated BindBankRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

        User user = userService.getUserByName(userName);
        if (StringUtils.isNotBlank(user.getBankCardNum())) {
            return R.error(MsgUtil.get("system.user.bindbank"));
        }

        userService.update(
                new UpdateWrapper<User>().lambda()
                        .set(User::getBankName, request.getBankName())
                        .set(User::getBankCardNum, request.getCardNo())
                        .set(User::getBankAddr, request.getAddr())
                        .set(User::getModifyTime, new Date())
                        .eq(User::getUserName, userName)
        );

        return R.ok();
    }


    @ApiOperation(value = "用户实名认证")
    @PostMapping("/realName")
    public R realName(@Validated RealNameRequest request, HttpServletRequest httpServletRequest) {
        String userName = JwtUtils.getUserName(httpServletRequest);

//        if (!IdcardUtil.isValidCard(request.getIdCardNo())) {
//            return R.error(MsgUtil.get("system.user.idcard"));
//        }

        if (StringUtils.isBlank(request.getIdCardNo())) {
            return R.error(MsgUtil.get("system.user.idcard"));
        }

        User user = userService.getUserByName(userName);
        if (StringUtils.isNotBlank(user.getRealName()) && StringUtils.isNotBlank(user.getIdCard())) {
            return R.error(MsgUtil.get("system.user.realname"));
        }

        userService.update(
                new UpdateWrapper<User>().lambda()
                        .set(User::getRealName, request.getRealName())
                        .set(User::getIdCard, request.getIdCardNo())
                        .set(User::getModifyTime, new Date())
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
                        .set(User::getModifyTime, new Date())
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
                        .set(User::getModifyTime, new Date())
                        .eq(User::getUserName, userName)
        );

        return R.ok();
    }

    @Transactional
    @ApiOperation(value = "用户提现")
    @PostMapping("/withdraw")
    public R withdraw(@Validated WithdrawRequest request, HttpServletRequest httpServletRequest) throws Exception {
        // 验证类型
        if (!StringUtils.equals(request.getType(), "1") && !StringUtils.equals(request.getType(), "2")) {
            return R.error(MsgUtil.get("system.param.err"));
        }

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

        // 验证提现金额
        BigDecimal amount = new BigDecimal(request.getAmount());
        // 如果提现U  换算成实际金额
        if (StringUtils.equals(request.getType(), "2")) {
            amount = NumberUtil.mul(amount, MapUtil.getDouble(params, "usdt_rate"));
        }

        Double leastWithdrawAmount = MapUtil.getDouble(params, "least_withdraw_amount", 0.0);
        Double largestWithdrawAmount = MapUtil.getDouble(params, "largest_withdraw_amount", 0.0);
        if ((leastWithdrawAmount != 0 && amount.doubleValue() < leastWithdrawAmount)
                || (largestWithdrawAmount != 0 && amount.doubleValue() > largestWithdrawAmount)) {
            return R.error(StrUtil.format(MsgUtil.get("system.withdraw.limitamount"), leastWithdrawAmount, largestWithdrawAmount));
        }

        String userName = JwtUtils.getUserName(httpServletRequest);
        User user = userService.getUserByName(userName);
        // 验证支付密码
        String pwd = SecureUtil.md5(request.getPwd());
        if (!StringUtils.equals(pwd, user.getPayPwd())) {
            return R.error(MsgUtil.get("system.order.paypwderror"));
        }

        if (StringUtils.isBlank(user.getRealName()) || StringUtils.isBlank(user.getIdCard())) {
            return R.error(MsgUtil.get("system.order.realname"));
        }
        if (user.getUserStatus().intValue() == 1) {
            return R.error(MsgUtil.get("system.user.enable"));
        }
        if (user.getBalance().doubleValue() < amount.doubleValue()) {
            return R.error(MsgUtil.get("system.order.balance"));
        }

        // 查询是否还有待审核的订单
        long count = withdrawService.count(
                new LambdaQueryWrapper<Withdraw>()
                        .eq(Withdraw::getUserName, user.getUserName())
                        .eq(Withdraw::getStatus, 0)
        );
        if (count > 0) {
            return R.error(MsgUtil.get("system.withdraw.hasorder"));
        }

        // 扣钱
        userService.updateUserBalance(userName, amount.negate());

        String orderNo = IdUtil.getSnowflakeNextIdStr();
        // 提现记录
        Withdraw withdraw = new Withdraw();
        withdraw.setOrderNo(orderNo);
        withdraw.setUserName(userName);
        withdraw.setOptAmount(amount);
        withdraw.setUsdtAmount(StringUtils.equals(request.getType(), "2") ? new BigDecimal(request.getAmount()) : null);
        withdraw.setBeforeAmount(user.getBalance());
        withdraw.setAfterAmount(NumberUtil.sub(user.getBalance(), amount));
        withdraw.setWalletAddr(StringUtils.equals(request.getType(), "2") ? user.getWalletAddr() : null);
        withdraw.setBankCardNum(StringUtils.equals(request.getType(), "2") ? null : user.getBankCardNum());
        withdraw.setRealName(user.getRealName());
        withdraw.setBankName(StringUtils.equals(request.getType(), "2") ? null : user.getBankName());
        withdraw.setBankAddr(StringUtils.equals(request.getType(), "2") ? null : user.getBankAddr());
        withdraw.setOptType(NumberUtils.toInt(request.getType()));
        withdraw.setStatus(0);
        withdraw.setOptTime(now);
        withdraw.setCheckTime(now);
        withdraw.setOperator(null);
        withdraw.setUserAgent(user.getUserAgent());
        withdraw.setRemark(null);
        withdrawService.save(withdraw);

        // 添加流水记录
        Account account = new Account();
        account.setAccountNo(IdUtil.getSnowflakeNextIdStr());
        account.setUserName(userName);
        account.setOptAmount(amount.negate());
        account.setBeforeAmount(user.getBalance());
        account.setAfterAmount(NumberUtil.sub(user.getBalance(), amount));
        account.setType(2);
        account.setOptType(3);
        account.setOptTime(now);
        account.setUserAgent(user.getUserAgent());
        account.setRefNo(orderNo);
        account.setRemark("提现金额:" + amount + "元");
        accountService.save(account);

        // 记录报表
        UserReport report = new UserReport();
        report.setUserName(user.getUserName());
        report.setToday(DateUtil.format(now, "yyyyMMdd"));
        report.setDepositAmount(new BigDecimal("0"));
        report.setWithdrawAmount(amount);
        report.setInvestmentAmount(new BigDecimal("0"));
        report.setIncomeAmount(new BigDecimal("0"));
        userReportService.insertOrUpdate(report);

        redisTemplate.opsForValue().set("user:withdraw:notice", "1");
        return R.ok();
    }
}
