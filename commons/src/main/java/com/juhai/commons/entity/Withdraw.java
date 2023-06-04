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
     * 昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 提现金额
     */
    @TableField(value = "opt_amount")
    private BigDecimal optAmount;

    /**
     * 手续费
     */
    @TableField(value = "fee_rate")
    private BigDecimal feeRate;

    /**
     * 到账金额
     */
    @TableField(value = "real_amount")
    private BigDecimal realAmount;

    /**
     * 开户行
     */
    @TableField(value = "bank_name")
    private String bankName;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 银行卡号
     */
    @TableField(value = "bank_no")
    private String bankNo;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 订单时间
     */
    @TableField(value = "order_time")
    private Date orderTime;

    /**
     * 审核时间
     */
    @TableField(value = "check_time")
    private Date checkTime;

    /**
     * 状态(0:未审核 1:审核通过 2:拒绝)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 操作人
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 上级代理
     */
    @TableField(value = "user_agent")
    private String userAgent;

    /**
     * 代理数节点
     */
    @TableField(value = "user_agent_node")
    private String userAgentNode;

    /**
     * 代理层级
     */
    @TableField(value = "user_agent_level")
    private Integer userAgentLevel;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}