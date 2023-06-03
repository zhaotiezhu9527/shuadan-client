package com.juhai.api.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juhai.api.controller.request.OrderRequest;
import com.juhai.api.utils.JwtUtils;
import com.juhai.commons.entity.*;
import com.juhai.commons.service.*;
import com.juhai.commons.utils.MsgUtil;
import com.juhai.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Slf4j
@Api(value = "订单相关", tags = "订单相关")
@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private ParamterService paramterService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserReportService userReportService;

    @Transactional
    @ApiOperation(value = "投资")
    @PostMapping("/execute")
    public R execute(@Validated OrderRequest request, HttpServletRequest httpServletRequest) throws Exception {
        Date now = new Date();

        // 查询项目信息
        Project project = projectService.getById(request.getProjectId());
        if (project == null) {
            return R.error(MsgUtil.get("system.project.invalid"));
        }
        if (!DateUtil.isIn(now, project.getStartTime(), project.getEndTime()) || project.getStatus().intValue() == 1) {
            return R.error(MsgUtil.get("system.project.finshed"));
        }
        // 操作金额
        BigDecimal amount = new BigDecimal(request.getAmount());
        if (amount.doubleValue() < project.getMinAmount().doubleValue()) {
            return R.error(StrUtil.format(MsgUtil.get("system.order.minamount"), project.getMinAmount()));
        }

        // 查询用户信息
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

        // 订单号

//        String orderNo = IdUtil.getSnowflakeNextIdStr();
        String orderNo = DateUtil.format(now, "yyMMddHHmmssSSS");
        // 扣钱
        userService.updateUserBalance(userName, amount.negate());

        // 添加订单记录
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserName(userName);
        order.setProjectId(project.getId());
        order.setProjectName(project.getProjectName());
        order.setRealName(user.getRealName());
        order.setAmount(amount);
        order.setIncomeRate(project.getIncomeRate());
        order.setLimitTime(project.getLimitTime());
        order.setStartTime(project.getStartTime());
        order.setEndTime(project.getEndTime());
        order.setForecastReturnTime(DateUtil.offsetMinute(now, project.getLimitTime()));
        order.setActualReturnTime(order.getForecastReturnTime());
        // 计算利息
        BigDecimal income = NumberUtil.mul(amount, NumberUtil.div(project.getIncomeRate(), 100));
        order.setForecastReturnAmount(income);
        order.setActualReturnAmount(income);
        order.setStatus(0);
        order.setUserAgent(user.getUserAgent());
        order.setOrderTime(now);
        orderService.save(order);
        // 添加流水记录
        Account account = new Account();
        account.setAccountNo(IdUtil.getSnowflakeNextIdStr());
        account.setUserName(userName);
        account.setOptAmount(amount.negate());
        account.setBeforeAmount(user.getBalance());
        account.setAfterAmount(NumberUtil.sub(user.getBalance(), amount));
        account.setType(2);
        account.setOptType(4);
        account.setOptTime(new Date());
        account.setUserAgent(user.getUserAgent());
        account.setRefNo(orderNo);
        account.setRemark("投资项目:" + project.getProjectName() + ",使用余额" + amount + "元");
        accountService.save(account);

        // 记录报表
        UserReport report = new UserReport();
        report.setUserName(userName);
        report.setToday(DateUtil.format(now, "yyyyMMdd"));
        report.setDepositAmount(new BigDecimal("0"));
        report.setWithdrawAmount(new BigDecimal("0"));
        report.setInvestmentAmount(amount);
        report.setIncomeAmount(new BigDecimal("0"));
        userReportService.insertOrUpdate(report);
        return R.ok();
    }


    @ApiOperation(value = "注单详情")
    @GetMapping("/detail/{orderNo}")
    public R detail(HttpServletRequest httpServletRequest, @PathVariable("orderNo") String orderNo) {
        String userName = JwtUtils.getUserName(httpServletRequest);
        Order order = orderService.getOne(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNo, orderNo)
                        .eq(Order::getUserName, userName)
        );
        if (order == null) {
            return R.error(MsgUtil.get("system.order.noorder"));
        }

        User user = userService.getUserByName(userName);
        Map<String, String> params = paramterService.getAllParamByMap();

        JSONObject obj = new JSONObject();
        obj.put("orderNo", order.getOrderNo());
        obj.put("userName", order.getUserName());
        obj.put("projectName", order.getProjectName());
        obj.put("realName", order.getRealName());
        obj.put("amount", order.getAmount());
        obj.put("incomeRate", order.getIncomeRate());
        obj.put("limitTime", order.getLimitTime());
        obj.put("forecastReturnAmount", order.getForecastReturnAmount());
        obj.put("IdCardNo", user.getIdCard());
        obj.put("orderTime", order.getOrderTime());
        obj.put("forecastReturnTime", order.getForecastReturnTime());
        obj.put("returnTime", order.getForecastReturnTime());
        obj.put("guaranteeCompany", params.get("guarantee_company"));
        obj.put("ourCompany", params.get("our_company"));
        obj.put("status", order.getStatus());
        obj.put("abbreviation", params.get("jiancheng"));
        return R.ok().put("data", obj);
    }
}
