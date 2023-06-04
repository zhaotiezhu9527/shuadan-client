package com.juhai.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_avatar
 */
@TableName(value ="t_avatar")
@Data
public class Avatar implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 头像地址
     */
    @TableField(value = "img_url")
    private String imgUrl;

    /**
     * 状态(0:正常 1:禁用)
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