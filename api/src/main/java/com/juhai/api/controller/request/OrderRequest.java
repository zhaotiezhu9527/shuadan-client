package com.juhai.api.controller.request;

import com.juhai.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "注册请求类", description = "注册请求参数")
public class OrderRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "项目ID", example = "100", required = true)
    private String projectId;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "金额", example = "30000", required = true)
    private String amount;

    @NotNull(message = "validation.user.register.paypwd")
    @Pattern(regexp = RegConstant.WITHDRAW_PWD_REG, message = "validation.user.register.paypwd")
    @ApiModelProperty(value = "支付密码", example = "123qwe", required = true)
    private String pwd;
}
