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
 * @TableName t_deposit
 */
@TableName(value ="t_deposit")
@Data
public class Deposit implements Serializable {
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
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 操作金额
     */
    @TableField(value = "opt_amount")
    private BigDecimal optAmount;

    /**
     * 操作前金额
     */
    @TableField(value = "before_amount")
    private BigDecimal beforeAmount;

    /**
     * 操作后金额
     */
    @TableField(value = "after_amount")
    private BigDecimal afterAmount;

    /**
     * 1:系统充值
     */
    @TableField(value = "opt_type")
    private Integer optType;

    /**
     * 操作时间
     */
    @TableField(value = "opt_time")
    private Date optTime;

    /**
     * 状态(0:待审核 1:审核通过)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 操作人
     */
    @TableField(value = "opt_user")
    private String optUser;

    /**
     * 上级代理
     */
    @TableField(value = "user_agent")
    private String userAgent;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}