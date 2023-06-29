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
 * @TableName t_order
 */
@TableName(value ="t_order")
@Data
public class Order implements Serializable {
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
     * 预派送批次
     */
    @TableField(value = "pre_batch")
    private String preBatch;

    /**
     * 商品ID
     */
    @TableField(value = "goods_id")
    private Integer goodsId;

    /**
     * 商品价格
     */
    @TableField(value = "goods_price")
    private BigDecimal goodsPrice;

    /**
     * 商品数量
     */
    @TableField(value = "goods_count")
    private Integer goodsCount;

    /**
     * 订单金额
     */
    @TableField(value = "order_amount")
    private BigDecimal orderAmount;

    /**
     * 佣金
     */
    @TableField(value = "commission")
    private BigDecimal commission;

    /**
     * 佣金比例
     */
    @TableField(value = "commission_rate")
    private BigDecimal commissionRate;

    /**
     * 状态(0:待处理 1:已完成 2:冻结中)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 订单时间
     */
    @TableField(value = "order_time")
    private Date orderTime;

    /**
     * 支付时间
     */
    @TableField(value = "pay_time")
    private Date payTime;

    /**
     * 预派送ID
     */
    @TableField(value = "pre_id")
    private Integer preId;

    /**
     * 预派送详情ID
     */
    @TableField(value = "pre_detail_id")
    private Integer preDetailId;

    /**
     * 今日第几单
     */
    @TableField(value = "count_num")
    private Integer countNum;

    /**
     * 订单类型(0:加急单 1:普通单)
     */
    @TableField(value = "order_type")
    private Integer orderType;

    /**
     * 佣金倍数
     */
    @TableField(value = "commission_mul")
    private Double commissionMul;

    /**
     * 专区ID
     */
    @TableField(value = "area_id")
    private Integer areaId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private Goods goods;

    /**
     * 加急单
     */
    @TableField(value = "prompt_text")
    private String promptText;
}