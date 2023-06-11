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
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 日期
     */
    @TableField(value = "today")
    private Date today;

    /**
     * 充值量
     */
    @TableField(value = "deposit")
    private BigDecimal deposit;

    /**
     * 提现量
     */
    @TableField(value = "withdraw")
    private BigDecimal withdraw;

    /**
     * 投注量
     */
    @TableField(value = "bet")
    private BigDecimal bet;

    /**
     * 返佣量
     */
    @TableField(value = "commission")
    private BigDecimal commission;

    /**
     * 收入量
     */
    @TableField(value = "income")
    private BigDecimal income;

    /**
     * 上级代理
     */
    @TableField(value = "user_agent")
    private String userAgent;

    /**
     * 上级代理节点
     */
    @TableField(value = "user_agent_node")
    private String userAgentNode;

    /**
     * 上级代理层级
     */
    @TableField(value = "user_agent_level")
    private Integer userAgentLevel;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}