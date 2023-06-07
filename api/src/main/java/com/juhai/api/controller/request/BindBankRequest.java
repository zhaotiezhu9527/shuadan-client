package com.juhai.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "绑定银行卡请求类", description = "绑定银行卡请求参数")
public class BindBankRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "银行卡号", example = "6226905531172489", required = true)
    private String cardNo;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "地址", example = "北京支行", required = true)
    private String addr;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "银行名称", example = "中信实业银行(借记卡)", required = true)
    private String bankName;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "真实姓名", example = "赵铁柱", required = true)
    private String realName;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "手机号", example = "15996874567", required = true)
    private String phone;
}
