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
 * @TableName t_withdraw
 */
@TableName(value ="t_withdraw")
@Data
public class Withdraw implements Serializable {
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
     * 操作金额
     */
    @TableField(value = "opt_amount")
    private BigDecimal optAmount;

    /**
     * USDT金额
     */
    @TableField(value = "usdt_amount")
    private BigDecimal usdtAmount;

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
     * 钱包地址
     */
    @TableField(value = "wallet_addr")
    private String walletAddr;

    /**
     * 银行卡号
     */
    @TableField(value = "bank_card_num")
    private String bankCardNum;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 银行名称
     */
    @TableField(value = "bank_name")
    private String bankName;

    /**
     * 开户行地址
     */
    @TableField(value = "bank_addr")
    private String bankAddr;

    /**
     * 1:提现银行 2:提现钱包
     */
    @TableField(value = "opt_type")
    private Integer optType;

    /**
     * 0:待审核 1:审核通过 2:审核不通过
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 申请时间
     */
    @TableField(value = "opt_time")
    private Date optTime;

    /**
     * 审核时间
     */
    @TableField(value = "check_time")
    private Date checkTime;

    /**
     * 操作人
     */
    @TableField(value = "operator")
    private String operator;

    /**
     * 上级代理
     */
    @TableField(value = "user_agent")
    private String userAgent;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}