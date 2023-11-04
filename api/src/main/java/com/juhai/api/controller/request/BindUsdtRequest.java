package com.juhai.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "绑定USDT请求类", description = "绑定USDT请求参数")
public class BindUsdtRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "地址", example = "465465421354645", required = true)
    private String addr;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "真实姓名", example = "赵铁柱", required = true)
    private String realName;
}
