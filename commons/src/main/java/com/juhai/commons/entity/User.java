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
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 用户余额
     */
    @TableField(value = "balance")
    private BigDecimal balance;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 身份证号码
     */
    @TableField(value = "id_card")
    private String idCard;

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
     * 邀请码
     */
    @TableField(value = "invite_code")
    private String inviteCode;

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
     * 用户状态(0:正常 1:冻结)
     */
    @TableField(value = "user_status")
    private Integer userStatus;

    /**
     * 用户等级id
     */
    @TableField(value = "user_level_id")
    private Integer userLevelId;

    /**
     * 上级代理用户名
     */
    @TableField(value = "user_agent")
    private String userAgent;

    /**
     * 是否实名(0:已实名 1:未实名)
     */
//    @TableField(value = "is_real_name")
//    private Integer isRealName;

    /**
     * 注册时间
     */
    @TableField(value = "register_time")
    private Date registerTime;

    /**
     * 注册Ip
     */
    @TableField(value = "register_ip")
    private String registerIp;

    /**
     * 上次登录时间
     */
    @TableField(value = "last_time")
    private Date lastTime;

    /**
     * 上次登录IP
     */
    @TableField(value = "last_ip")
    private String lastIp;

    /**
     * 最后修改时间
     */
    @TableField(value = "modify_time")
    private Date modifyTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}