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
 * @TableName t_project
 */
@TableName(value ="t_project")
@Data
public class Project implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 产品名称
     */
    @TableField(value = "project_name")
    private String projectName;

    /**
     * 项目金额(万元)
     */
    @TableField(value = "project_amount")
    private BigDecimal projectAmount;

    /**
     * 收益率
     */
    @TableField(value = "income_rate")
    private BigDecimal incomeRate;

    /**
     * 期限(分钟)
     */
    @TableField(value = "limit_time")
    private Integer limitTime;

    /**
     * 起投金额
     */
    @TableField(value = "min_amount")
    private BigDecimal minAmount;

    /**
     * 0:开启 1:关闭
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 项目进度
     */
    @TableField(value = "schedule")
    private String schedule;

    /**
     * 担保公司
     */
    @TableField(value = "guarantee_company")
    private String guaranteeCompany;

    /**
     * 排序号(值越大越靠前)
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 最后修改时间
     */
    @TableField(value = "modify_time")
    private Date modifyTime;

    /**
     * 图片地址
     */
    @TableField(value = "img")
    private String img;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}