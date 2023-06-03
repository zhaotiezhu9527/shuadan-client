package com.juhai.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_order
 */
@TableName(value ="t_order")
@Data
public class Order implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单号
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 项目ID
     */
    @TableField(value = "project_id")
    private Integer projectId;

    /**
     * 项目名
     */
    @TableField(value = "project_name")
    private String projectName;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 投资金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 收益率
     */
    @TableField(value = "income_rate")
    private BigDecimal incomeRate;

    /**
     * 期限(分钟)
     */
    @TableField(value = "limit_time")
    private Integer limitTime;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 预计返款时间
     */
    @TableField(value = "forecast_return_time")
    private Date forecastReturnTime;

    /**
     * 实际返款时间
     */
    @TableField(value = "actual_return_time")
    private Date actualReturnTime;

    /**
     * 预计返款金额
     */
    @TableField(value = "forecast_return_amount")
    private BigDecimal forecastReturnAmount;

    /**
     * 实际返款金额
     */
    @TableField(value = "actual_return_amount")
    private BigDecimal actualReturnAmount;

    /**
     * 0:未结算 1:已结算 2:结算异常
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 上级代理
     */
    @TableField(value = "user_agent")
    private String userAgent;

    /**
     * 订单时间
     */
    @TableField(value = "order_time")
    private Date orderTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}