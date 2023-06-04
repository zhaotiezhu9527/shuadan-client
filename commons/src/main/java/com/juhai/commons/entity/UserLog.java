package com.juhai.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_user_log
 */
@TableName(value ="t_user_log")
@Data
public class UserLog implements Serializable {
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
     * 登录ip
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * ip详情
     */
    @TableField(value = "ip_detail")
    private String ipDetail;

    /**
     * 登录时间
     */
    @TableField(value = "login_time")
    private Date loginTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}