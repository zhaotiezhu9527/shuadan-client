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
 * @TableName t_account
 */
@TableName(value ="t_account")
@Data
public class Account implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 流水号
     */
    @TableField(value = "account_no")
    private String accountNo;

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
     * 1:收入 2:支出
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 1:系统充值 2:系统扣款 3:提现 4:投资扣款 5:投资进账 6:签到
     */
    @TableField(value = "opt_type")
    private Integer optType;

    /**
     * 操作时间
     */
    @TableField(value = "opt_time")
    private Date optTime;

    /**
     * 上级代理
     */
    @TableField(value = "user_agent")
    private String userAgent;

    /**
     * 关联订单号
     */
    @TableField(value = "ref_no")
    private String refNo;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}