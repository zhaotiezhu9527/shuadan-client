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
 * @TableName t_level
 */
@TableName(value ="t_level")
@Data
public class Level implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 等级值
     */
    @TableField(value = "level_value")
    private Integer levelValue;

    /**
     * 等级名称
     */
    @TableField(value = "level_name")
    private String levelName;

    /**
     * 等级价格
     */
    @TableField(value = "level_price")
    private BigDecimal levelPrice;

    /**
     * 等级图标
     */
    @TableField(value = "level_icon")
    private String levelIcon;

    /**
     * 每日订单量
     */
    @TableField(value = "day_order_count")
    private Integer dayOrderCount;

    /**
     * 佣金比例
     */
    @TableField(value = "commission_rate")
    private BigDecimal commissionRate;

    /**
     * 每日提现次数
     */
    @TableField(value = "day_withdraw_count")
    private Integer dayWithdrawCount;

    /**
     * 最小提现金额
     */
    @TableField(value = "min_withdraw_amount")
    private BigDecimal minWithdrawAmount;

    /**
     * 最大提现金额
     */
    @TableField(value = "max_withdraw_amount")
    private BigDecimal maxWithdrawAmount;

    /**
     * 提现手续费
     */
    @TableField(value = "withdraw_fee")
    private BigDecimal withdrawFee;

    /**
     * 提现需要完成 几笔订单才开提现 / 天
     */
    @TableField(value = "withdraw_order_count")
    private Integer withdrawOrderCount;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}