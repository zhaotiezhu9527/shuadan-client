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
     * 1:收入 2:支出
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 
     */
    @TableField(value = "opt_type")
    private Integer optType;

    /**
     * 上级代理
     */
    @TableField(value = "user_agent")
    private String userAgent;

    /**
     * 代理树节点
     */
    @TableField(value = "user_agent_node")
    private String userAgentNode;

    /**
     * 代理层级
     */
    @TableField(value = "user_agent_level")
    private Integer userAgentLevel;

    /**
     * 关联订单号
     */
    @TableField(value = "ref_no")
    private String refNo;

    /**
     * 操作时间
     */
    @TableField(value = "opt_time")
    private Date optTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}