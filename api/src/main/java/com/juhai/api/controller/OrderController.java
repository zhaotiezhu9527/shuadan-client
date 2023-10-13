package com.juhai.api.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.juhai.api.utils.JwtUtils;
import com.juhai.commons.entity.*;
import com.juhai.commons.service.*;
import com.juhai.commons.utils.MsgUtil;
import com.juhai.commons.utils.R;
import icu.mhb.mybatisplus.plugln.core.JoinLambdaWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

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
    private DayReportService dayReportService;

    @Autowired
    private OrderCountService orderCountService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Transactional
    @ApiOperation(value = "匹配订单")
    @GetMapping("/match/{areaId}")
    public R match(HttpServletRequest httpServletRequest, @PathVariable(value = "areaId") String areaId) throws Exception {
        Date now = new Date();
        String userName = JwtUtils.getUserName(httpServletRequest);
        // 防止频繁提交
        Map<String, String> paramsMap = paramterService.getAllParamByMap();
        // 验证时间段
        String orderTimeStr = paramsMap.get("order_time");
        if (StringUtils.isNotBlank(orderTimeStr)) {
            String today = DateUtil.formatDate(now);
            String[] timeArr = orderTimeStr.split("-");
            Date beginTime = DateUtil.parse(today + " " + timeArr[0]);
            Date endTime = DateUtil.parse(today + " " + timeArr[1]);
            if (!DateUtil.isIn(now, beginTime, endTime)) {
                return R.error(StrUtil.format(MsgUtil.get("system.order.opentime"), orderTimeStr));
            }
        }
        // 获取用户信息
        JoinLambdaWrapper<User> wrapper = new JoinLambdaWrapper<>(User.class);
        wrapper.eq(User::getUserName, userName);
        wrapper.leftJoin(Level.class,Level::getId,User::getLevelId).oneToOneSelect(User::getLevel, Level.class).end();
        User user = userService.joinGetOne(wrapper, User.class);
        Level userLevel = user.getLevel();

        if (StringUtils.isEmpty(user.getBankNo())) {
            return R.error(MsgUtil.get("system.withdraw.nobank"));
        }

        // 获取专区信息
        JoinLambdaWrapper<Area> areaWrapper = new JoinLambdaWrapper<>(Area.class);
        areaWrapper.eq(Area::getStatus, 0);
        areaWrapper.eq(Area::getId, areaId);
        areaWrapper.leftJoin(Level.class,Level::getId,Area::getLevelId).oneToOneSelect(Area::getLevel, Level.class).end();
        areaWrapper.orderByDesc(Area::getPxh);
        Area area = areaService.joinGetOne(areaWrapper, Area.class);
        Level areaLevel = area.getLevel();
        // 校验当前用户是否解锁该区域
        if (areaLevel.getLevelValue().intValue() > userLevel.getLevelValue().intValue()) {
            return R.error(MsgUtil.get("system.order.unlockarea"));
        }

        // 校验用户余额是否大于专区余额
        if (area.getAreaBalance().doubleValue() > user.getBalance().doubleValue()) {
            return R.error(StrUtil.format(MsgUtil.get("system.order.minbalance"), area.getAreaBalance()));
        }

        // 验证是否还有订单未完成
        long noFinishCount = orderService.count(
                new LambdaQueryWrapper<Order>()
                        .in(Order::getStatus, Arrays.asList(0, 4))
                        .eq(Order::getUserName, userName)
        );
        if (noFinishCount >= 1) {
            return R.error(MsgUtil.get("system.order.nofinish"));
        }

        // 获取今日订单数
        List<OrderCount> orderCounts = orderCountService.list(
                new LambdaQueryWrapper<OrderCount>()
                        .eq(OrderCount::getUserName, userName)
                        .eq(user.getUpdateOrder().intValue() == 1, OrderCount::getToday, DateUtil.formatDate(now))
                        .orderByDesc(OrderCount::getCreateTime)
        );
        int countNum = CollUtil.isEmpty(orderCounts) ? 0 : orderCounts.get(0).getOrderCount();

        if (countNum >= userLevel.getDayOrderCount()) {
            return R.error(MsgUtil.get("system.order.dayfinish"));
        }

        // 预派送业务
        Prepare prePare = prepareService.getOne(
                new LambdaQueryWrapper<Prepare>()
                        .eq(Prepare::getStatus, 0)
                        .eq(Prepare::getUserName, userName)
                        .eq(Prepare::getTriggerNum, countNum + 1)
        );
        String orderNo = IdUtil.getSnowflakeNextIdStr();
        Order order = new Order();
        JSONObject object = new JSONObject();
        Goods goods = null;
        if (prePare != null) {
            goods = goodsService.getById(prePare.getGoodsId());
            // 预派送订单
            order.setOrderNo(orderNo);
            order.setUserName(userName);
            order.setNickName(user.getNickName());
            order.setGoodsId(prePare.getGoodsId());
            order.setGoodsPrice(prePare.getOrderAmount());
            order.setGoodsCount(prePare.getGoodsCount());
            order.setOrderAmount(NumberUtil.mul(goods.getGoodsPrice(), order.getGoodsCount()));
            order.setCommissionRate(areaLevel.getCommissionRate());
            order.setCommissionMul(prePare.getCommissionMul());
            order.setCommission(NumberUtil.mul(order.getCommissionMul(), order.getOrderAmount(), NumberUtil.div(order.getCommissionRate(), 100)));
            order.setStatus(0);
            order.setOrderTime(now);
            order.setPayTime(null);
            order.setPreId(prePare.getId());
            order.setPreDetailId(0);
            order.setCountNum(countNum + 1);
            order.setOrderType(0);
            order.setPreBatch(prePare.getPreBatch());
            order.setAreaId(area.getId());
            order.setPromptText(prePare.getPromptText());
            if (user.getBalance().doubleValue() < order.getOrderAmount().doubleValue()) {
                order.setStatus(4);
                // 扣钱
                String sql = StrUtil.format("balance = balance - {},freeze_balance = freeze_balance + {}", order.getOrderAmount(), NumberUtil.add(order.getOrderAmount(), order.getCommission()));
                userService.update(new UpdateWrapper<User>()
                        .setSql(sql)
                        .eq("user_name", order.getUserName())
                );
            }
        } else {
            // 取得商品(金额范围) 预派送业务 预派送订单用户金额不足 直接扣负数
            List<Goods> goodsList = goodsService.list(
                    new LambdaQueryWrapper<Goods>()
                            .eq(Goods::getStatus, 0)
                            .eq(Goods::getAreaId, area.getId())
            );
            // 随机取得一个商品
            Collections.shuffle(goodsList);
            goods = goodsList.get(0);
            // 计算用户余额可以买多少个商品
            int goodsCount = 1;
            // 取得订单价格区间
            String[] matchRangeArr = paramsMap.get("match_range").split("-");
            BigDecimal minBalance = NumberUtil.mul(user.getBalance(), NumberUtil.div(NumberUtils.toDouble(matchRangeArr[0]), 100));
            BigDecimal maxBalance = NumberUtil.mul(user.getBalance(), NumberUtil.div(NumberUtils.toDouble(matchRangeArr[1]), 100));
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

            order.setOrderNo(orderNo);
            order.setUserName(userName);
            order.setNickName(user.getNickName());
            order.setGoodsId(goods.getId());
            order.setGoodsPrice(goods.getGoodsPrice());
            order.setGoodsCount(goodsCount);
            order.setOrderAmount(NumberUtil.mul(order.getGoodsPrice(), order.getGoodsCount()));
            order.setCommissionRate(areaLevel.getCommissionRate());
            order.setCommissionMul(1.0);
            order.setCommission(NumberUtil.mul(order.getCommissionMul(), order.getOrderAmount(), NumberUtil.div(order.getCommissionRate(), 100)));
            order.setStatus(0);
            order.setOrderTime(now);
            order.setPayTime(null);
            order.setPreId(0);
            order.setPreDetailId(0);
            order.setCountNum(countNum + 1);
            order.setOrderType(1);
            order.setAreaId(area.getId());
        }

        orderService.save(order);
        // 累计今日订单
        incOrderCount(user.getUserName(), now);

        object.put("orderNo", order.getOrderNo());
        return R.ok(MsgUtil.get("system.order.success")).put("data", object);
    }

    /**
     * 累计订单
     * @param userName
     * @param now
     * @throws Exception
     */
    private void incOrderCount(String userName, Date now) throws Exception {
        OrderCount orderCount1 = new OrderCount();
        orderCount1.setUserName(userName);
        orderCount1.setToday(DateUtil.formatDate(now));
        orderCount1.setOrderCount(1);
        orderCount1.setCreateTime(now);
        orderCount1.setUpdateTime(now);
        orderCountService.insertOrUpdate(orderCount1);
    }

    @Transactional
    @ApiOperation(value = "支付订单")
    @GetMapping("/pay/{orderNo}")
    public R pay(HttpServletRequest httpServletRequest, @PathVariable(value = "orderNo") String orderNo) throws Exception {
        Date now = new Date();

        Map<String, String> paramsMap = paramterService.getAllParamByMap();
        // 验证时间段
        String orderTimeStr = paramsMap.get("order_time");
        if (StringUtils.isNotBlank(orderTimeStr)) {
            String today = DateUtil.formatDate(now);
            String[] timeArr = orderTimeStr.split("-");
            Date beginTime = DateUtil.parse(today + " " + timeArr[0]);
            Date endTime = DateUtil.parse(today + " " + timeArr[1]);
            if (!DateUtil.isIn(now, beginTime, endTime)) {
                return R.error(StrUtil.format(MsgUtil.get("system.order.opentime"), orderTimeStr));
            }
        }

        String userName = JwtUtils.getUserName(httpServletRequest);
        JoinLambdaWrapper<User> wrapper = new JoinLambdaWrapper<>(User.class);
        wrapper.eq(User::getUserName, userName);
        wrapper.leftJoin(Level.class,Level::getId,User::getLevelId).oneToOneSelect(User::getLevel, Level.class).end();
        User user = userService.joinGetOne(wrapper, User.class);

        // 查询订单
        Order order = orderService.getOne(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNo, orderNo)
                        .eq(Order::getUserName, userName)
        );
        if (order == null) {
            return R.error(MsgUtil.get("system.payorder.noexist"));
        }
        if (order.getStatus().intValue() == 1 || order.getStatus().intValue() == 2 || order.getStatus().intValue() == 3) {
            return R.error(MsgUtil.get("system.payorder.orderfinish"));
        }

        if (order.getOrderType().intValue() == 1 ) {
            // 普通订单 增加用户金额
            return payOrder1(user, order, now, paramsMap);
        } else {
            // 加急订单  冻结金额
            return payOrder2(user, order, now, paramsMap);
        }
    }

    /**
     * 支付加急订单
     * @param user
     * @param order
     * @param now
     * @return
     * @throws Exception
     */
    private R payOrder2(User user, Order order, Date now, Map<String, String> paramsMap) throws Exception {
        /**
         * 如果订单是待处理0 校验金额是否充足 不充足就扣成负数 累计冻结金额 把订单改成未完全支付状态
         * 如果订单是未完全支付状态4 判断余额是否大于0
         */
        if (order.getStatus().intValue() == 4) {
            if (user.getBalance().doubleValue() < 0) {
                // 未完成支付,余额小于等于0
                return R.error(MsgUtil.get("system.payorder.jebg"));
            } else {
                // 余额充足,可以完成支付
                // 查询当前批次是否还有预派送订单  有则冻结这一单的钱
                Prepare prePare = prepareService.getOne(
                        new LambdaQueryWrapper<Prepare>()
                                .eq(Prepare::getStatus, 0)
                                .eq(Prepare::getUserName, order.getUserName())
                                .eq(Prepare::getTriggerNum, order.getCountNum() + 1)
                                .eq(Prepare::getPreBatch, order.getPreBatch())
                );
                if (prePare == null) {
                    // 没有预派送订单  返还金额  解冻金额  修改所有冻结订单状态
                    // 修改订单状态为已完成
                    orderService.update(
                            new UpdateWrapper<Order>().lambda()
                                    .eq(Order::getId, order.getId())
                                    .set(Order::getStatus, 1)
                                    .set(Order::getPayTime, now)
                    );
                    // 修改当前批次所有已冻结的订单为已完成
                    orderService.update(
                            new UpdateWrapper<Order>().lambda()
                                    .eq(Order::getPreBatch, order.getPreBatch())
                                    .eq(Order::getStatus, 2)
                                    .set(Order::getStatus, 1)
                    );

                    // 计算佣金
                    BigDecimal commission = NumberUtil.mul(order.getCommissionMul(), order.getOrderAmount(), NumberUtil.div(order.getCommissionRate(), 100));
                    // 修改解冻金额为0,将冻结金额累积到用户金额,累计佣金
                    String sql = StrUtil.format("balance = balance + freeze_balance,freeze_balance = 0,bet = bet + {},income = income + {}", order.getOrderAmount(), commission);
                    userService.update(new UpdateWrapper<User>()
                            .setSql(sql)
                            .eq("user_name", order.getUserName())
                    );

                    // 修改预派送订单状态
                    prepareService.update(
                            new UpdateWrapper<Prepare>().lambda()
                                    .eq(Prepare::getId, order.getPreId())
                                    .set(Prepare::getStatus, 1)
                                    .set(Prepare::getUpdateTime, now)
                    );

                    List<Account> accounts = new ArrayList<>();
                    // 佣金流水
                    String accountNo1 = IdUtil.getSnowflakeNextIdStr();
                    Account account1 = new Account();
                    account1.setAccountNo(accountNo1);
                    account1.setUserName(user.getUserName());
                    account1.setOptAmount(commission);
                    account1.setType(1);
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
                    dayReport.setToday(DateUtil.formatDate(order.getOrderTime()));
                    dayReport.setBet(order.getOrderAmount());
                    dayReport.setCommission(commission);
                    dayReport.setIncome(commission);
                    dayReport.setUserAgent(user.getUserAgent());
                    dayReport.setUserAgentNode(user.getUserAgentNode());
                    dayReport.setUserAgentLevel(user.getUserAgentLevel());
                    dayReport.setCreateTime(now);
                    dayReport.setUpdateTime(now);
                    dayReports.add(dayReport);

                    List<User> users = new ArrayList<>();
                    // 计算上级返佣以及报表
                    if (user.getUserAgentLevel().intValue() > 0) {
                        // 层级返点比例
                        Map<Integer, BigDecimal> rateMap = new HashMap<>();
                        rateMap.put(1, NumberUtil.div(new BigDecimal(paramsMap.get("agent_1_rate")), 100));
                        rateMap.put(2, NumberUtil.div(new BigDecimal(paramsMap.get("agent_2_rate")), 100));
                        rateMap.put(3, NumberUtil.div(new BigDecimal(paramsMap.get("agent_3_rate")), 100));
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
                            userReport1.setIncome(agentCommission);
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
                            agentDayReport.setToday(DateUtil.formatDate(order.getOrderTime()));
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

                    JSONObject object = new JSONObject();
                    object.put("orderNo", "");
                    return R.ok(MsgUtil.get("system.payorder.jjdwc")).put("data", object);
                } else {
                    // 还有预派送订单 扣除用户余额 累计冻结金额 修改订单状态为冻结

                    // 计算佣金
                    BigDecimal commission = NumberUtil.mul(order.getCommissionMul(), order.getOrderAmount(), NumberUtil.div(order.getCommissionRate(), 100));
                    // 累计冻结余额
                    BigDecimal freezeBalance = NumberUtil.add(order.getOrderAmount(), commission);
                    List<User> users = new ArrayList<>();
                    User userReport = new User();
                    userReport.setUserName(user.getUserName());
//                    userReport.setBalance(order.getOrderAmount().negate());
//                    userReport.setFreezeBalance(freezeBalance);
                    userReport.setBet(order.getOrderAmount());
                    userReport.setIncome(commission);
                    users.add(userReport);

                    // 修改订单状态为冻结状态
                    orderService.update(
                            new UpdateWrapper<Order>().lambda()
                                    .eq(Order::getId, order.getId())
                                    .set(Order::getStatus, 2)
                                    .set(Order::getPayTime, now)
                    );
                    // 修改预派送订单状态
                    prepareService.update(
                            new UpdateWrapper<Prepare>().lambda()
                                    .eq(Prepare::getId, order.getPreId())
                                    .set(Prepare::getStatus, 1)
                                    .set(Prepare::getUpdateTime, now)
                    );

                    // 计算报表
                    List<DayReport> dayReports = new ArrayList<>();
                    DayReport dayReport = new DayReport();
                    dayReport.setUserName(user.getUserName());
                    dayReport.setToday(DateUtil.formatDate(order.getOrderTime()));
                    dayReport.setBet(order.getOrderAmount());
                    dayReport.setCommission(commission);
                    dayReport.setIncome(commission);
                    dayReport.setUserAgent(user.getUserAgent());
                    dayReport.setUserAgentNode(user.getUserAgentNode());
                    dayReport.setUserAgentLevel(user.getUserAgentLevel());
                    dayReport.setCreateTime(now);
                    dayReport.setUpdateTime(now);
                    dayReports.add(dayReport);

                    List<Account> accounts = new ArrayList<>();
                    // 佣金流水
                    String accountNo1 = IdUtil.getSnowflakeNextIdStr();
                    Account account1 = new Account();
                    account1.setAccountNo(accountNo1);
                    account1.setUserName(user.getUserName());
                    account1.setOptAmount(commission);
                    account1.setType(1);
                    account1.setOptType(4);
                    account1.setUserAgent(user.getUserAgent());
                    account1.setUserAgentNode(user.getUserAgentNode());
                    account1.setUserAgentLevel(user.getUserAgentLevel());
                    account1.setRefNo(order.getOrderNo());
                    account1.setOptTime(now);
                    account1.setRemark("订单佣金:[" + order.getOrderNo() +"]");
                    accounts.add(account1);
                    // 计算上级返佣以及报表
                    if (user.getUserAgentLevel().intValue() > 0) {
                        // 层级返点比例
                        Map<Integer, BigDecimal> rateMap = new HashMap<>();
                        rateMap.put(1, NumberUtil.div(new BigDecimal(paramsMap.get("agent_1_rate")), 100));
                        rateMap.put(2, NumberUtil.div(new BigDecimal(paramsMap.get("agent_2_rate")), 100));
                        rateMap.put(3, NumberUtil.div(new BigDecimal(paramsMap.get("agent_3_rate")), 100));
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
                            userReport1.setIncome(agentCommission);
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
                            agentDayReport.setToday(DateUtil.formatDate(order.getOrderTime()));
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
                    // 累计今日订单
                    // incOrderCount(user.getUserName(), now);

                    // 添加一个新的预派送订单
                    // Goods goods = goodsService.getById(prePare.getGoodsId());
                    String orderNo = IdUtil.getSnowflakeNextIdStr();
                    Order newOrder = new Order();
                    // 预派送订单
                    Goods goods = goodsService.getById(prePare.getGoodsId());
                    newOrder.setOrderNo(orderNo);
                    newOrder.setUserName(user.getUserName());
                    newOrder.setNickName(user.getNickName());
                    newOrder.setGoodsId(prePare.getGoodsId());
                    newOrder.setGoodsPrice(goods.getGoodsPrice());
                    newOrder.setGoodsCount(prePare.getGoodsCount());
                    newOrder.setOrderAmount(NumberUtil.mul(newOrder.getGoodsPrice(), newOrder.getGoodsCount()));
                    newOrder.setCommissionRate(user.getLevel().getCommissionRate());
                    newOrder.setCommissionMul(prePare.getCommissionMul());
                    newOrder.setCommission(NumberUtil.mul(newOrder.getCommissionMul(), newOrder.getOrderAmount(), NumberUtil.div(newOrder.getCommissionRate(), 100)));
                    newOrder.setStatus(0);
                    newOrder.setOrderTime(now);
                    newOrder.setPayTime(null);
                    newOrder.setPreId(prePare.getId());
                    newOrder.setPreDetailId(0);
                    newOrder.setCountNum(prePare.getTriggerNum());
                    newOrder.setOrderType(0);
                    newOrder.setPreBatch(prePare.getPreBatch());
                    newOrder.setAreaId(order.getAreaId());
                    newOrder.setPromptText(prePare.getPromptText());
                    orderService.save(newOrder);

                    // 累计今日订单
                    incOrderCount(user.getUserName(), now);
                    JSONObject object = new JSONObject();
                    object.put("orderNo", newOrder.getOrderNo());
                    return R.ok(MsgUtil.get("system.payorder.ddtjcg")).put("data", object);
                }
            }
        } else if (order.getStatus().intValue() == 0) {
            if (user.getBalance().doubleValue() >= order.getOrderAmount().doubleValue()) {
                // 余额充足,可以完成支付
                // 查询当前批次是否还有预派送订单  有则冻结这一单的钱
                Prepare prePare = prepareService.getOne(
                        new LambdaQueryWrapper<Prepare>()
                                .eq(Prepare::getStatus, 0)
                                .eq(Prepare::getUserName, order.getUserName())
                                .eq(Prepare::getTriggerNum, order.getCountNum() + 1)
                                .eq(Prepare::getPreBatch, order.getPreBatch())
                );
                if (prePare == null) {
                    // 没有预派送订单  返还金额  解冻金额  修改所有冻结订单状态
                    // 修改订单状态为已完成
                    orderService.update(
                            new UpdateWrapper<Order>().lambda()
                                    .eq(Order::getId, order.getId())
                                    .set(Order::getStatus, 1)
                                    .set(Order::getPayTime, now)
                    );
                    // 修改当前批次所有已冻结的订单为已完成
                    orderService.update(
                            new UpdateWrapper<Order>().lambda()
                                    .eq(Order::getPreBatch, order.getPreBatch())
                                    .eq(Order::getStatus, 2)
                                    .set(Order::getStatus, 1)
                    );

                    // 计算佣金
                    BigDecimal commission = NumberUtil.mul(order.getCommissionMul(), order.getOrderAmount(), NumberUtil.div(order.getCommissionRate(), 100));
                    // 修改解冻金额为0,将冻结金额累积到用户金额,累计佣金
                    String sql = StrUtil.format("balance = balance + freeze_balance + {},freeze_balance = 0,bet = bet + {},income = income + {}", commission, order.getOrderAmount(), commission);
                    userService.update(new UpdateWrapper<User>()
                            .setSql(sql)
                            .eq("user_name", order.getUserName())
                    );

                    // 修改预派送订单状态
                    prepareService.update(
                            new UpdateWrapper<Prepare>().lambda()
                                    .eq(Prepare::getId, order.getPreId())
                                    .set(Prepare::getStatus, 1)
                                    .set(Prepare::getUpdateTime, now)
                    );

                    List<Account> accounts = new ArrayList<>();
                    // 佣金流水
                    String accountNo1 = IdUtil.getSnowflakeNextIdStr();
                    Account account1 = new Account();
                    account1.setAccountNo(accountNo1);
                    account1.setUserName(user.getUserName());
                    account1.setOptAmount(commission);
                    account1.setType(1);
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
                    dayReport.setToday(DateUtil.formatDate(order.getOrderTime()));
                    dayReport.setBet(order.getOrderAmount());
                    dayReport.setCommission(commission);
                    dayReport.setIncome(commission);
                    dayReport.setUserAgent(user.getUserAgent());
                    dayReport.setUserAgentNode(user.getUserAgentNode());
                    dayReport.setUserAgentLevel(user.getUserAgentLevel());
                    dayReport.setCreateTime(now);
                    dayReport.setUpdateTime(now);
                    dayReports.add(dayReport);

                    List<User> users = new ArrayList<>();
                    // 计算上级返佣以及报表
                    if (user.getUserAgentLevel().intValue() > 0) {
                        // 层级返点比例
                        Map<Integer, BigDecimal> rateMap = new HashMap<>();
                        rateMap.put(1, NumberUtil.div(new BigDecimal(paramsMap.get("agent_1_rate")), 100));
                        rateMap.put(2, NumberUtil.div(new BigDecimal(paramsMap.get("agent_2_rate")), 100));
                        rateMap.put(3, NumberUtil.div(new BigDecimal(paramsMap.get("agent_3_rate")), 100));
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
                            userReport1.setIncome(agentCommission);
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
                            agentDayReport.setToday(DateUtil.formatDate(order.getOrderTime()));
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

                    JSONObject object = new JSONObject();
                    object.put("orderNo", "");
                    return R.ok(MsgUtil.get("system.payorder.jjdwc")).put("data", object);
                } else {
                    // 还有预派送订单 扣除用户余额 累计冻结金额 修改订单状态为冻结

                    // 计算佣金
                    BigDecimal commission = NumberUtil.mul(order.getCommissionMul(), order.getOrderAmount(), NumberUtil.div(order.getCommissionRate(), 100));
                    // 累计冻结余额
                    BigDecimal freezeBalance = NumberUtil.add(order.getOrderAmount(), commission);
                    List<User> users = new ArrayList<>();
                    User userReport = new User();
                    userReport.setUserName(user.getUserName());
                    userReport.setBalance(order.getOrderAmount().negate());
                    userReport.setFreezeBalance(freezeBalance);
                    userReport.setBet(order.getOrderAmount());
                    userReport.setIncome(commission);
                    users.add(userReport);

                    // 修改订单状态为冻结状态
                    orderService.update(
                            new UpdateWrapper<Order>().lambda()
                                    .eq(Order::getId, order.getId())
                                    .set(Order::getStatus, 2)
                                    .set(Order::getPayTime, now)
                    );
                    // 修改预派送订单状态
                    prepareService.update(
                            new UpdateWrapper<Prepare>().lambda()
                                    .eq(Prepare::getId, order.getPreId())
                                    .set(Prepare::getStatus, 1)
                                    .set(Prepare::getUpdateTime, now)
                    );

                    // 计算报表
                    List<DayReport> dayReports = new ArrayList<>();
                    DayReport dayReport = new DayReport();
                    dayReport.setUserName(user.getUserName());
                    dayReport.setToday(DateUtil.formatDate(order.getOrderTime()));
                    dayReport.setBet(order.getOrderAmount());
                    dayReport.setCommission(commission);
                    dayReport.setIncome(commission);
                    dayReport.setUserAgent(user.getUserAgent());
                    dayReport.setUserAgentNode(user.getUserAgentNode());
                    dayReport.setUserAgentLevel(user.getUserAgentLevel());
                    dayReport.setCreateTime(now);
                    dayReport.setUpdateTime(now);
                    dayReports.add(dayReport);

                    List<Account> accounts = new ArrayList<>();
                    // 佣金流水
                    String accountNo1 = IdUtil.getSnowflakeNextIdStr();
                    Account account1 = new Account();
                    account1.setAccountNo(accountNo1);
                    account1.setUserName(user.getUserName());
                    account1.setOptAmount(commission);
                    account1.setType(1);
                    account1.setOptType(4);
                    account1.setUserAgent(user.getUserAgent());
                    account1.setUserAgentNode(user.getUserAgentNode());
                    account1.setUserAgentLevel(user.getUserAgentLevel());
                    account1.setRefNo(order.getOrderNo());
                    account1.setOptTime(now);
                    account1.setRemark("订单佣金:[" + order.getOrderNo() +"]");
                    accounts.add(account1);
                    // 计算上级返佣以及报表
                    if (user.getUserAgentLevel().intValue() > 0) {
                        // 层级返点比例
                        Map<Integer, BigDecimal> rateMap = new HashMap<>();
                        rateMap.put(1, NumberUtil.div(new BigDecimal(paramsMap.get("agent_1_rate")), 100));
                        rateMap.put(2, NumberUtil.div(new BigDecimal(paramsMap.get("agent_2_rate")), 100));
                        rateMap.put(3, NumberUtil.div(new BigDecimal(paramsMap.get("agent_3_rate")), 100));
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
                            userReport1.setIncome(agentCommission);
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
                            agentDayReport.setToday(DateUtil.formatDate(order.getOrderTime()));
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
                    // 累计今日订单
                    // incOrderCount(user.getUserName(), now);

                    // 添加一个新的预派送订单
                    // Goods goods = goodsService.getById(prePare.getGoodsId());
                    String orderNo = IdUtil.getSnowflakeNextIdStr();
                    Order newOrder = new Order();
                    // 预派送订单
                    Goods goods = goodsService.getById(prePare.getGoodsId());
                    newOrder.setOrderNo(orderNo);
                    newOrder.setUserName(user.getUserName());
                    newOrder.setNickName(user.getNickName());
                    newOrder.setGoodsId(prePare.getGoodsId());
                    newOrder.setGoodsPrice(goods.getGoodsPrice());
                    newOrder.setGoodsCount(prePare.getGoodsCount());
                    newOrder.setOrderAmount(NumberUtil.mul(newOrder.getGoodsPrice(), newOrder.getGoodsCount()));
                    newOrder.setCommissionRate(user.getLevel().getCommissionRate());
                    newOrder.setCommissionMul(prePare.getCommissionMul());
                    newOrder.setCommission(NumberUtil.mul(newOrder.getCommissionMul(), newOrder.getOrderAmount(), NumberUtil.div(newOrder.getCommissionRate(), 100)));
                    newOrder.setStatus(0);
                    newOrder.setOrderTime(now);
                    newOrder.setPayTime(null);
                    newOrder.setPreId(prePare.getId());
                    newOrder.setPreDetailId(0);
                    newOrder.setCountNum(prePare.getTriggerNum());
                    newOrder.setOrderType(0);
                    newOrder.setPreBatch(prePare.getPreBatch());
                    newOrder.setAreaId(order.getAreaId());
                    newOrder.setPromptText(prePare.getPromptText());
                    orderService.save(newOrder);

                    // 累计今日订单
                    incOrderCount(user.getUserName(), now);
                    JSONObject object = new JSONObject();
                    object.put("orderNo", newOrder.getOrderNo());
                    return R.ok(MsgUtil.get("system.payorder.ddtjcg")).put("data", object);
                }
            } else {
                // 余额不足
                BigDecimal commission = NumberUtil.mul(order.getCommissionMul(), order.getOrderAmount(), NumberUtil.div(order.getCommissionRate(), 100));
                // 修改解冻金额为0,将冻结金额累积到用户金额,累计佣金
                String sql = StrUtil.format("balance = balance - {},freeze_balance = freeze_balance + {}", order.getOrderAmount(), NumberUtil.add(order.getOrderAmount(), commission));
                userService.update(new UpdateWrapper<User>()
                        .setSql(sql)
                        .eq("user_name", order.getUserName())
                );
                // 修改订单为未完成支付状态
                orderService.update(
                        new UpdateWrapper<Order>().lambda()
                                .eq(Order::getPreBatch, order.getPreBatch())
                                .eq(Order::getStatus, 0)
                                .set(Order::getStatus, 4)
                );
                return R.error(MsgUtil.get("system.payorder.jebg"));
            }
        }
        return R.error("订单已完成");
    }


    /**
     * 支付普通订单
     * @param user
     * @param order
     * @return
     */
    private R payOrder1(User user, Order order, Date now, Map<String, String> paramsMap) throws Exception {
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
        // 增加金额、累计投注额、总收益
        List<User> users = new ArrayList<>();
        User userReport = new User();
        userReport.setUserName(user.getUserName());
        userReport.setBalance(commission);
        userReport.setBet(order.getOrderAmount());
        userReport.setIncome(commission);
        users.add(userReport);

        List<Account> accounts = new ArrayList<>();
        // 佣金流水
        String accountNo1 = IdUtil.getSnowflakeNextIdStr();
        Account account1 = new Account();
        account1.setAccountNo(accountNo1);
        account1.setUserName(user.getUserName());
        account1.setOptAmount(commission);
        account1.setType(1);
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
        dayReport.setToday(DateUtil.formatDate(order.getOrderTime()));
        dayReport.setBet(order.getOrderAmount());
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
            rateMap.put(1, NumberUtil.div(new BigDecimal(paramsMap.get("agent_1_rate")), 100));
            rateMap.put(2, NumberUtil.div(new BigDecimal(paramsMap.get("agent_2_rate")), 100));
            rateMap.put(3, NumberUtil.div(new BigDecimal(paramsMap.get("agent_3_rate")), 100));
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
                userReport1.setIncome(agentCommission);
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
                agentDayReport.setToday(DateUtil.formatDate(order.getOrderTime()));
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
        // 累计今日订单
//        incOrderCount(user.getUserName(), now);

        JSONObject object = new JSONObject();
        object.put("orderNo", "");
        return R.ok(MsgUtil.get("system.payorder.ddtiwc")).put("data", object);
    }

    @ApiOperation(value = "订单详情")
    @GetMapping("/detail/{orderNo}")
    public R detail(HttpServletRequest httpServletRequest,@PathVariable(value = "orderNo") String orderNo) {
        String lang = httpServletRequest.getHeader("lang");
        String userName = JwtUtils.getUserName(httpServletRequest);

        Order order = orderService.getOne(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNo, orderNo)
                        .eq(Order::getUserName, userName)
        );
        if (order == null) {
            return R.error(MsgUtil.get("system.payorder.noexist"));
        }

        User user = userService.getUserByName(userName);

        Map<String, String> paramsMap = paramterService.getAllParamByMap();

        JSONObject object = new JSONObject();
        object.put("orderNo", order.getOrderNo());
//        object.put("orderTime", order.getOrderTime());
        object.put("orderTime", StringUtils.equals(lang, "vi_VN") ? DateUtil.format(order.getOrderTime(), "dd-MM-yyyy HH:mm:ss") : order.getOrderTime());
        object.put("orderType", order.getOrderType());
        object.put("orderAmount", order.getOrderAmount().stripTrailingZeros());
        object.put("countNum", order.getCountNum());
        object.put("promptText", order.getPromptText());
        object.put("commission", order.getCommission().stripTrailingZeros());
        object.put("commissionMul", order.getCommissionMul());

        Goods goods = goodsService.getById(order.getGoodsId());

        String resourceDomain = paramsMap.get("resource_domain");
        object.put("goodsName", goods == null ? "" : goods.getGoodsName());
        object.put("goodsImg", goods == null ? "" : resourceDomain + goods.getGoodsImg());
        object.put("goodsPrice", order.getGoodsPrice());
        object.put("goodsCount", order.getGoodsCount());
        object.put("forecastReturn", NumberUtil.add(order.getOrderAmount(), order.getCommission()).stripTrailingZeros());
        object.put("orderStatus", order.getStatus());
        if (order.getStatus().intValue() == 4) {
            object.put("orderStatus", 0);
        }
        // 余额
        BigDecimal balanceSub = new BigDecimal(0);
        if (order.getOrderType().intValue() == 1) {
            // 普通订单
            if (user.getBalance().doubleValue() < order.getOrderAmount().doubleValue()) {
                balanceSub = NumberUtil.sub(user.getBalance(), order.getOrderAmount()).stripTrailingZeros();
            }
        } else {
            if (order.getStatus().intValue() == 4) {
                if (user.getBalance().doubleValue() < 0) {
                    balanceSub = user.getBalance();
                }
            }
        }
        object.put("balanceSub", balanceSub);
        return R.ok().put("data", object);
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
            if (userAgentNames.size() >= 3) {
                break;
            }
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
