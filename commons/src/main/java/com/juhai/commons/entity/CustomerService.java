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
 * @TableName t_customer_service
 */
@TableName(value ="t_customer_service")
@Data
public class CustomerService implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客服名称
     */
    @TableField(value = "service_name")
    private String serviceName;

    /**
     * 客服链接
     */
    @TableField(value = "service_link")
    private String serviceLink;

    /**
     * 工作时间
     */
    @TableField(value = "work_time")
    private String workTime;

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
    @TableField(value = "remake")
    private String remake;

    /**
     * 状态(0:启用 1:禁用)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 排序号
     */
    @TableField(value = "pxh")
    private Integer pxh;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}