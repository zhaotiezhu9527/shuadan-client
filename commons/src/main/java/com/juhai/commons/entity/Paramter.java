package com.juhai.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_paramter
 */
@TableName(value ="t_paramter")
@Data
public class Paramter implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 参数名
     */
    @TableField(value = "param_name")
    private String paramName;

    /**
     * 参数key
     */
    @TableField(value = "param_key")
    private String paramKey;

    /**
     * 参数值
     */
    @TableField(value = "param_value")
    private String paramValue;

    /**
     * 是否展示前端(0:是 1:否)
     */
    @TableField(value = "is_show")
    private Integer isShow;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}