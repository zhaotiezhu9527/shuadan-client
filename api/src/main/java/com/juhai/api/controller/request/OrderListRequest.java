package com.juhai.api.controller.request;

import com.juhai.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "订单列表请求类", description = "订单列表请求参数")
public class OrderListRequest extends PageBaseRequest{

    @ApiModelProperty(value = "状态(空值:全部 0:待处理 1:已完成 2:冻结中)", example = "0", required = false)
    private String status;
}
