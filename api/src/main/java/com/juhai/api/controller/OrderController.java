package com.juhai.api.controller;
import com.juhai.commons.entity.Level;
import com.juhai.commons.entity.Avatar;
import com.juhai.commons.entity.Goods;
import java.util.Date;

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
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Api(value = "订单相关", tags = "订单相关")
@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private ParamterService paramterService;

    @Autowired
    private LevelService levelService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private PrepareService prepareService;

    @Autowired
    private PrepareDetailService prepareDetailService;

    @Autowired
    private DayReportService dayReportService;

    @Transactional
    @ApiOperation(value = "匹配订单")
    @GetMapping("/match/{areaId}")
    public R match(HttpServletRequest httpServletRequest, @PathVariable(value = "areaId") String areaId) throws Exception {
        Date now = new Date();
        String userName = JwtUtils.getUserName(httpServletRequest);
        // 防止频繁提交

        // 获取用户信息
        JoinLambdaWrapper<User> wrapper = new JoinLambdaWrapper<>(User.class);
        wrapper.eq(User::getUserName, userName);
        wrapper.leftJoin(Level.class,Level::getId,User::getLevelId).oneToOneSelect(User::getLevel, Level.class).end();
        User user = userService.joinGetOne(wrapper, User.class);
        Level level = user.getLevel();

        // 获取专区信息
        JoinLambdaWrapper<Area> areaWrapper = new JoinLambdaWrapper<>(Area.class);
        areaWrapper.eq(Area::getStatus, 0);
        areaWrapper.eq(Area::getId, areaId);
        areaWrapper.leftJoin(Level.class,Level::getId,Area::getLevelId).oneToOneSelect(Area::getLevel, Level.class).end();
        areaWrapper.orderByDesc(Area::getPxh);
        Area area = areaService.joinGetOne(areaWrapper, Area.class);
        // 校验当前用户是否解锁该区域
        if (area.getLevel().getLevelValue().intValue() > level.getLevelValue().intValue()) {
            return R.error("未解锁该专区");
        }

        // 校验用户余额是否大于专区余额
        if (area.getAreaBalance().doubleValue() > user.getBalance().doubleValue()) {
            return R.error("余额低于" + area.getAreaBalance().stripTrailingZeros() + "元,无法进行交易");
        }

        // 验证是否还有订单未完成
        long noFinishCount = orderService.count(new LambdaQueryWrapper<Order>().eq(Order::getStatus, 0));
        if (noFinishCount >= 1) {
            return R.error("您还有订单未完成");
        }

        // 校验今日订单数
        String orderKey = RedisKeyUtil.UserOrderCount(userName);
        String orderCountStr = redisTemplate.opsForValue().get(orderKey);
        int countNum = NumberUtils.toInt(orderCountStr, 0);
        if (countNum > level.getDayOrderCount()) {
            return R.error("今日任务已完成");
        }

        // 预派送业务
        List<Prepare> prePares = prepareService.list(
                new LambdaQueryWrapper<Prepare>()
                        .eq(Prepare::getStatus, 0)
                        .eq(Prepare::getUserName, userName)
                        .eq(Prepare::getTriggerNum, countNum + 1)
        );
        if (CollUtil.isNotEmpty(prePares)) {
            // 执行预派送业务
            Prepare prepare = prePares.get(0);
            List<PrepareDetail> prepareDetails = prepareDetailService.list(
                    new LambdaQueryWrapper<PrepareDetail>()
                            .eq(PrepareDetail::getStatus, 0)
                            .eq(PrepareDetail::getPreId, prepare.getId())
            );
            // 获取预派送信息
            PrepareDetail prepareDetail = prepareDetails.get(0);
            Integer goodsCount = prepareDetail.getGoodsCount();
            Goods goods = goodsService.getById(prepareDetail.getGoodsId());

            String orderNo = IdUtil.getSnowflakeNextIdStr();
            // 写入订单
            Order order = new Order();
            order.setOrderNo(orderNo);
            order.setUserName(userName);
            order.setNickName(user.getNickName());
            order.setGoodsId(goods.getId());
            order.setGoodsPrice(goods.getGoodsPrice());
            order.setGoodsCount(goodsCount);
            order.setOrderAmount(NumberUtil.mul(order.getGoodsPrice(), order.getGoodsCount()));
            order.setCommissionRate(user.getLevel().getCommissionRate());
            order.setCommissionMul(prepare.getCommissionMul());
            order.setCommission(NumberUtil.mul(order.getCommissionMul(), order.getOrderAmount(), NumberUtil.div(order.getCommissionRate(), 100)));
            order.setStatus(0);
            order.setOrderTime(now);
            order.setPayTime(null);
            order.setPreId(prepareDetail.getPreId());
            order.setPreDetailId(prepareDetail.getId());
            order.setCountNum(countNum + 1);

            orderService.save(order);
            // 累加今日订单数
            redisTemplate.opsForValue().increment(orderKey);

            JSONObject object = new JSONObject();
            object.put("orderNo", orderNo);

            return R.ok().put("data", object);
        } else {
            // 取得商品(金额范围) 预派送业务 预派送订单用户金额不足 直接扣负数
            List<Goods> goodsList = goodsService.list(
                    new LambdaQueryWrapper<Goods>()
                            .eq(Goods::getStatus, 0)
            );
            // 随机取得一个商品
            Collections.shuffle(goodsList);
            Goods goods = goodsList.get(0);
            // 计算用户余额可以买多少个商品
            int goodsCount = 1;
            // 取得订单价格区间
            BigDecimal minBalance = NumberUtil.mul(user.getBalance(), NumberUtil.div(20, 100));
            BigDecimal maxBalance = NumberUtil.mul(user.getBalance(), NumberUtil.div(85, 100));
            // 在区间内随机获得一个订单价格
            BigDecimal randomGoodsPrice = RandomUtil.randomBigDecimal(minBalance, maxBalance);
            // 订单价格可以购买多少个商品
            for (Goods temp : goodsList) {
                if (temp.getGoodsPrice().doubleValue() < user.getBalance().doubleValue()) {
                    goods = temp;
                }
                BigDecimal div = NumberUtil.div(randomGoodsPrice, temp.getGoodsPrice());
                if (div.doubleValue() > 1) {
                    goodsCount = (int) div.doubleValue();
                    goods = temp;
                    break;
                }

            }

            String orderNo = IdUtil.getSnowflakeNextIdStr();
            // 写入订单
            Order order = new Order();
            order.setOrderNo(orderNo);
            order.setUserName(userName);
            order.setNickName(user.getNickName());
            order.setGoodsId(goods.getId());
            order.setGoodsPrice(goods.getGoodsPrice());
            order.setGoodsCount(goodsCount);
            order.setOrderAmount(NumberUtil.mul(order.getGoodsPrice(), order.getGoodsCount()));
            order.setCommissionRate(user.getLevel().getCommissionRate());
            order.setCommissionMul(1.0);
            order.setCommission(NumberUtil.mul(order.getCommissionMul(), order.getOrderAmount(), NumberUtil.div(order.getCommissionRate(), 100)));
            order.setStatus(0);
            order.setOrderTime(now);
            order.setPayTime(null);
            order.setPreId(0);
            order.setPreDetailId(0);
            order.setCountNum(countNum + 1);
            orderService.save(order);
            // 累计总投注额
            userService.update(
                    new UpdateWrapper<User>()
                            .setSql("bet = bet + " + order.getOrderAmount())
                            .eq("user_name", userName)
            );
            // 累计投注报表
            DayReport dayReport = new DayReport();
            dayReport.setUserName(userName);
            dayReport.setToday(order.getOrderTime());
            dayReport.setDeposit(new BigDecimal("0"));
            dayReport.setWithdraw(new BigDecimal("0"));
            dayReport.setBet(order.getOrderAmount());
            dayReport.setCommission(new BigDecimal("0"));
            dayReport.setIncome(new BigDecimal("0"));
            dayReport.setUserAgent(user.getUserAgent());
            dayReport.setUserAgentNode(user.getUserAgentNode());
            dayReport.setUserAgentLevel(user.getUserAgentLevel());
            dayReport.setCreateTime(now);
            dayReport.setUpdateTime(now);
            dayReportService.insertOrUpdate(dayReport);
            // 累加今日订单数
            redisTemplate.opsForValue().increment(orderKey);

            JSONObject object = new JSONObject();
            object.put("orderNo", orderNo);

            return R.ok().put("data", object);
        }
    }


    @Transactional
    @ApiOperation(value = "支付订单")
    @GetMapping("/pay/{orderNo}")
    public R pay(HttpServletRequest httpServletRequest, @PathVariable(value = "orderNo") String orderNo) throws Exception {
        Date now = new Date();

        String userName = JwtUtils.getUserName(httpServletRequest);
        User user = userService.getUserByName(userName);

        // 查询订单
        Order order = orderService.getOne(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNo, orderNo)
                        .eq(Order::getUserName, userName)
        );
        if (order == null) {
            return R.error("无效订单");
        }

        if (order.getPreId() == null || order.getPreId().intValue() == 0) {
            // 普通订单
            return payOrder1(user, order, now);
        } else {
            // 加急订单
        }
        return R.ok();
    }


    /**
     * 支付普通订单
     * @param user
     * @param order
     * @return
     */
    private R payOrder1(User user, Order order, Date now) throws Exception {
        if (order.getStatus().intValue() != 0) {
            return R.error("该订单已完成");
        }
        if (order.getOrderAmount().doubleValue() > user.getBalance().doubleValue()) {
            return R.error("您的余额不足");
        }

        // 修改订单状态
        orderService.update(
                new UpdateWrapper<Order>().lambda()
                        .eq(Order::getId, order.getId())
                        .set(Order::getStatus, 1)
                        .set(Order::getPayTime, now)
        );

        // 计算佣金
        BigDecimal commission = NumberUtil.mul(order.getCommissionMul(), order.getOrderAmount(), NumberUtil.div(order.getCommissionRate(), 100));
        // 增加金额、累计总收益
        List<User> users = new ArrayList<>();
        User userReport = new User();
        userReport.setUserName(user.getUserName());
        userReport.setBalance(commission);
        userReport.setDeposit(new BigDecimal("0"));
        userReport.setWithdraw(new BigDecimal("0"));
        userReport.setIncome(commission);
        userReport.setBet(new BigDecimal("0"));
        users.add(userReport);

        List<Account> accounts = new ArrayList<>();
        // 佣金流水
        String accountNo1 = IdUtil.getSnowflakeNextIdStr();
        Account account1 = new Account();
        account1.setAccountNo(accountNo1);
        account1.setUserName(user.getUserName());
        account1.setOptAmount(commission);
        account1.setType(2);
        account1.setOptType(4);
        account1.setUserAgent(user.getUserAgent());
        account1.setUserAgentNode(user.getUserAgentNode());
        account1.setUserAgentLevel(user.getUserAgentLevel());
        account1.setRefNo(order.getOrderNo());
        account1.setOptTime(now);
        account1.setRemark("订单佣金:[" + order.getOrderNo() +"]");
        accounts.add(account1);

        // 计算报表
        List<DayReport> dayReports = new ArrayList<>();
        DayReport dayReport = new DayReport();
        dayReport.setUserName(user.getUserName());
        dayReport.setToday(order.getOrderTime());
        dayReport.setDeposit(new BigDecimal("0"));
        dayReport.setWithdraw(new BigDecimal("0"));
        dayReport.setBet(new BigDecimal("0"));
        dayReport.setCommission(commission);
        dayReport.setIncome(commission);
        dayReport.setUserAgent(user.getUserAgent());
        dayReport.setUserAgentNode(user.getUserAgentNode());
        dayReport.setUserAgentLevel(user.getUserAgentLevel());
        dayReport.setCreateTime(now);
        dayReport.setUpdateTime(now);
        dayReports.add(dayReport);
        // 计算上级返佣以及报表
        if (user.getUserAgentLevel().intValue() > 0) {
            // 层级返点比例
            Map<Integer, BigDecimal> rateMap = new HashMap<>();
            rateMap.put(1, NumberUtil.div(new BigDecimal("0.8"), 100));
            rateMap.put(2, NumberUtil.div(new BigDecimal("0.5"), 100));
            rateMap.put(3, NumberUtil.div(new BigDecimal("0.3"), 100));
            // 获取当前用户的前三级
            List<User> userAgents = getUserAgents(user.getUserAgentNode());
            for (int i = 0; i < userAgents.size(); i++) {
                User userAgent = userAgents.get(i);
                // 获取返点比例
                BigDecimal rate = rateMap.get(i + 1);
                // 计算返点
                BigDecimal agentCommission = NumberUtil.mul(order.getOrderAmount(), rate);
                // 上级代理加钱
                User userReport1 = new User();
                userReport1.setUserName(userAgent.getUserName());
                userReport1.setBalance(agentCommission);
                userReport1.setDeposit(new BigDecimal("0"));
                userReport1.setWithdraw(new BigDecimal("0"));
                userReport1.setIncome(agentCommission);
                userReport1.setBet(new BigDecimal("0"));
                users.add(userReport1);
                // 记录代理流水
                Account agentAccount = new Account();
                agentAccount.setAccountNo(IdUtil.getSnowflakeNextIdStr());
                agentAccount.setUserName(userAgent.getUserName());
                agentAccount.setOptAmount(agentCommission);
                agentAccount.setType(1);
                agentAccount.setOptType(5);
                agentAccount.setUserAgent(userAgent.getUserAgent());
                agentAccount.setUserAgentNode(userAgent.getUserAgentNode());
                agentAccount.setUserAgentLevel(userAgent.getUserAgentLevel());
                agentAccount.setRefNo(order.getOrderNo());
                agentAccount.setOptTime(now);
                agentAccount.setRemark("下级返点");
                accounts.add(agentAccount);
                // 记录代理报表
                DayReport agentDayReport = new DayReport();
                agentDayReport.setUserName(userAgent.getUserName());
                agentDayReport.setToday(order.getOrderTime());
                agentDayReport.setDeposit(new BigDecimal("0"));
                agentDayReport.setWithdraw(new BigDecimal("0"));
                agentDayReport.setBet(new BigDecimal("0"));
                agentDayReport.setCommission(new BigDecimal("0"));
                agentDayReport.setIncome(agentCommission);
                agentDayReport.setUserAgent(userAgent.getUserAgent());
                agentDayReport.setUserAgentNode(userAgent.getUserAgentNode());
                agentDayReport.setUserAgentLevel(userAgent.getUserAgentLevel());
                agentDayReport.setCreateTime(now);
                agentDayReport.setUpdateTime(now);
                dayReports.add(agentDayReport);
            }
        }
        userService.batchUpdateReport(users);
        accountService.saveBatch(accounts);
        dayReportService.batchInsertOrUpdate(dayReports);
        return R.ok();
    }

    /**
     * 获取用户前三级代理
     * @param userAgentNode
     * @return
     */
    private List<User> getUserAgents(String userAgentNode) {
        String[] userAgentArr = userAgentNode.split("\\|");
        List<String> userAgentNames = new ArrayList<>();
        for (int i = userAgentArr.length - 2; i >= 1; i--) {
            userAgentNames.add(userAgentArr[i]);
        }
        // 查询到用户前三级代理
        List<User> list = userService.list(
                new LambdaQueryWrapper<User>()
                        .in(User::getUserName, userAgentNames)
                        .orderByDesc(User::getUserAgentLevel)
        );
        return list;
    }
}
