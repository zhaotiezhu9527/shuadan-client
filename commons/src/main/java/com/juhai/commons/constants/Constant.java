/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.juhai.commons.constants;

/**
 * 常量
 *
 * @author Mark sunlightcs@gmail.com
 */
public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;
    /** 数据权限过滤 */
	public static final String SQL_FILTER = "sql_filter";
    /**
     * 当前页码
     */
    public static final String PAGE = "page";
    /**
     * 每页显示记录数
     */
    public static final String LIMIT = "limit";
    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "sidx";
    /**
     * 排序方式
     */
    public static final String ORDER = "order";
    /**
     *  升序
     */
    public static final String ASC = "asc";

	/**
	 * 菜单类型
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    /**
     * 定时任务状态
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 资金流动类型
     */
    public enum FundType {
        /**
         * 充值
         */
        TOPUP(1),
        /**
         * 提现
         */
        WITHDRAW(2),
        /**
         * 购买
         */
        BET(3),
        /**
         * 平台赠送
         */
        PLAT(4),
        /**
         * 代币挖矿收益
         */
        MINING(5),
        /**
         * 下级返点
         */
        COMMISSION(6),
        /**
         * 下单收益
         */
        INCOME(7),
        /**
         * 邀请奖励
         */
        INVITATION_REWARD(8),
        /**
         * 周薪
         */
        WEEK_SALARY(9),
        /**
         * 后台扣款
         */
        ADMIN_SUB(10),
        /**
         * 后台充值
         */
        ADMIN_ADD(11),
        /**
         * 提币拒绝返还
         */
        RETURN(12);
        private int value;

        FundType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 代币金额流动类型
     */
    public enum VirtualType {
        /**
         * 解锁
         */
        UNLOCK(1),
        /**
         * 代币挖矿收益
         */
        MINING(2),
        /**
         * 每周奖励
         */
        WEEKRAWARD(3),
        /**
         * 下级充值解锁
         */
        TOPUP_LOCK(4),
        /**
         * 下级返点
         */
        COMMISSION(5)
        ;
        private int value;

        VirtualType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
