package com.juhai.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName t_information
 */
@TableName(value ="t_information")
@Data
public class Information implements Serializable {
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
     * 手机号
     */
    @TableField(value = "user_phone")
    private String userPhone;

    /**
     * 年龄
     */
    @TableField(value = "user_age")
    private Integer userAge;

    /**
     * 月薪
     */
    @TableField(value = "user_salary")
    private String userSalary;

    /**
     * 工作
     */
    @TableField(value = "user_job")
    private String userJob;

    /**
     * 车房
     */
    @TableField(value = "user_car")
    private String userCar;

    /**
     * 婚姻情况
     */
    @TableField(value = "user_marriage")
    private String userMarriage;

    /**
     * FB帐号
     */
    @TableField(value = "user_fb")
    private String userFb;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "modify")
    private Date modify;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}