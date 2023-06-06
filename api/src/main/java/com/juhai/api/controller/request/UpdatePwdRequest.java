package com.juhai.api.controller.request;

import com.juhai.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "修改密码请求类", description = "修改密码请求参数")
public class UpdatePwdRequest {

    @NotNull(message = "validation.user.register.loginpwd")
    @Pattern(regexp = RegConstant.USER_PWD_REG, message = "validation.user.register.loginpwd")
    @ApiModelProperty(value = "旧密码", example = "123qwe", required = true)
    private String oldPwd;

    @NotNull(message = "validation.user.register.loginpwd")
    @Pattern(regexp = RegConstant.USER_PWD_REG, message = "validation.user.register.loginpwd")
    @ApiModelProperty(value = "新密码", example = "123qwe", required = true)
    private String newPwd;
}
