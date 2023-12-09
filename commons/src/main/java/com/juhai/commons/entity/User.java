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
 * @TableName t_user
 */
@TableName(value ="t_user")
@Data
public class User implements Serializable {
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
     * 昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 用户余额
     */
    @TableField(value = "balance")
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    @TableField(value = "freeze_balance")
    private BigDecimal freezeBalance;

    /**
     * 登录密码
     */
    @TableField(value = "login_pwd")
    private String loginPwd;

    /**
     * 支付密码
     */
    @TableField(value = "pay_pwd")
    private String payPwd;

    /**
     * 状态(0:正常 1:冻结)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 资金状态(0:正常 1:冻结)
     */
    @TableField(value = "funds_status")
    private Integer fundsStatus;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 用户手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 开户行
     */
    @TableField(value = "bank_name")
    private String bankName;

    /**
     * 银行卡号
     */
    @TableField(value = "bank_no")
    private String bankNo;

    /**
     * 开户行地址
     */
    @TableField(value = "bank_addr")
    private String bankAddr;

    /**
     * 信用分
     */
    @TableField(value = "credit_value")
    private Integer creditValue;

    /**
     * 用户等级
     */
    @TableField(value = "level_id")
    private Integer levelId;

    /**
     * 头像ID
     */
    @TableField(value = "avatar_id")
    private Integer avatarId;

    /**
     * 邀请码
     */
    @TableField(value = "invite_code")
    private String inviteCode;

    /**
     * 上级代理
     */
    @TableField(value = "user_agent")
    private String userAgent;

    /**
     * 代理节点数
     */
    @TableField(value = "user_agent_node")
    private String userAgentNode;

    /**
     * 层级数
     */
    @TableField(value = "user_agent_level")
    private Integer userAgentLevel;

    /**
     * 注册时间
     */
    @TableField(value = "register_time")
    private Date registerTime;

    /**
     * 注册IP
     */
    @TableField(value = "register_ip")
    private String registerIp;

    /**
     * 最后登录时间
     */
    @TableField(value = "last_time")
    private Date lastTime;

    /**
     * 最后登录IP
     */
    @TableField(value = "last_ip")
    private String lastIp;

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
     * 最后更新时间
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
    @TableField(value = "remake")
    private String remake;

    /**
     * 钱包地址
     */
    @TableField(value = "wallet_addr")
    private String walletAddr;

    /**
     * 是否累加订单(0:是 1:否)
     */
    @TableField(value = "update_order")
    private Integer updateOrder;

    /**
     * 总充值
     */
    @TableField(value = "deposit")
    private BigDecimal deposit;

    /**
     * 总提现
     */
    @TableField(value = "withdraw")
    private BigDecimal withdraw;

    /**
     * 总收益
     */
    @TableField(value = "income")
    private BigDecimal income;

    /**
     * 总投注
     */
    @TableField(value = "bet")
    private BigDecimal bet;

    /**
     * 推荐人数
     */
    @TableField(value = "invite_count")
    private Integer inviteCount;

    /**
     * 身份证号码
     */
    @TableField(value = "id_card")
    private String idCard;

    /**
     * 身份证号码
     */
    @TableField(value = "id_card_img")
    private String idCardImg;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private Avatar avatar;

    @TableField(exist = false)
    private Level level;
}