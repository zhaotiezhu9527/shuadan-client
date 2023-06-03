package com.juhai.commons.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;

/**
 * 生成各类业务订单ID
 */
public class IdUtils {

    public static String getTopUp() {
        return "t" + Long.toString(IdUtil.getSnowflake(1, 1).nextId());
    }

    public static String getWithdraw() {
        return "w" + Long.toString(IdUtil.getSnowflake(1, 1).nextId());
    }

    public static String getProductOrderNo() {
        return "p" + Long.toString(IdUtil.getSnowflake(1, 1).nextId());
    }

    public static String getBill() {
        return "b" + Long.toString(IdUtil.getSnowflake(1, 1).nextId());
    }

    public static String getGrabOrderDetailNo() {
        return "gd" + Long.toString(IdUtil.getSnowflake(1, 1).nextId());
    }

    public static String randomId(String bill) {
        return bill + Long.toString(IdUtil.getSnowflake(1, 1).nextId());
    }

    public static String randomId() {
        return IdUtil.getSnowflake(1, 1).nextIdStr() + RandomUtil.randomNumbers(2);
    }

    public static void main(String[] args) {
        System.out.println(RandomUtil.randomString(6));
    }
}
