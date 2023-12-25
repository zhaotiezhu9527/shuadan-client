package com.juhai.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "收集信息请求类", description = "收集信息请求参数")
public class InfomationRequest {

    /**
     * 手机号
     */
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "手机号", example = "dunaifen123", required = true)
    private String userPhone;

}
