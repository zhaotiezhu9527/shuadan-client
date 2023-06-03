package com.juhai.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 
 * @TableName t_day_report
 */
@TableName(value ="t_day_report")
@Data
public class DayReport implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日期
     */
    @TableField(value = "today")
    private String today;

    /**
     * 充值金额
     */
    @TableField(value = "deposit_amount")
    private BigDecimal depositAmount;

    /**
     * 提现金额
     */
    @TableField(value = "withdraw_amount")
    private BigDecimal withdrawAmount;

    /**
     * 投资金额
     */
    @TableField(value = "investment_amount")
    private BigDecimal investmentAmount;

    /**
     * 收益金额
     */
    @TableField(value = "income_amount")
    private BigDecimal incomeAmount;

    /**
     * 注册人数
     */
    @TableField(value = "register_count")
    private Integer registerCount;

    /**
     * 充值人数
     */
    @TableField(value = "deposit_count")
    private Integer depositCount;

    /**
     * 提现人数
     */
    @TableField(value = "withdraw_count")
    private Integer withdrawCount;

    /**
     * 投资人数
     */
    @TableField(value = "investment_count")
    private Integer investmentCount;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}