package com.juhai.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "绑定银行卡请求类", description = "绑定银行卡请求参数")
public class BindBankRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "银行卡号", example = "465465465123", required = true)
    private String cardNo;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "地址", example = "北京支行", required = true)
    private String addr;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "银行名称", example = "人民银行", required = true)
    private String bankName;
}
