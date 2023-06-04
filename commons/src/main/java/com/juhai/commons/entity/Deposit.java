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
     * 充值方式 1:手动充值
     */
    @TableField(value = "opt_type")
    private Integer optType;

    /**
     * 0:未处理 1:处理完成
     */
    @TableField(value = "status")
    private Integer status;

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
     * 创建时间
     */
    @TableField(value = "order_time")
    private Date orderTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}