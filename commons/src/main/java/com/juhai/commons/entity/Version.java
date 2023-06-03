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
 * @TableName t_version
 */
@TableName(value ="t_version")
@Data
public class Version implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 平台标识
     */
    @TableField(value = "plat_form")
    private String platForm;

    /**
     * 版本号
     */
    @TableField(value = "version")
    private String version;

    /**
     * 下载地址
     */
    @TableField(value = "download_url")
    private String downloadUrl;

    /**
     * 更新时间
     */
    @TableField(value = "modify_time")
    private Date modifyTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}