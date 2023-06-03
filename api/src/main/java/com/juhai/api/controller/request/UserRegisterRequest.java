package com.juhai.api.controller.request;

import com.juhai.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "注册请求类", description = "注册请求参数")
public class UserRegisterRequest {

    @NotNull(message = "validation.user.register.username")
    @Pattern(regexp = RegConstant.USER_NAME_REG, message = "validation.user.register.username")
    @ApiModelProperty(value = "用户名", example = "dunaifen123", required = true)
    private String userName;

    @NotNull(message = "validation.user.register.invitecode")
    @Pattern(regexp = RegConstant.INVITE_CODE_REG, message = "validation.user.register.invitecode")
    @ApiModelProperty(value = "邀请码", example = "123456", required = true)
    private String inviteCode;

    @NotNull(message = "validation.user.register.loginpwd")
    @Pattern(regexp = RegConstant.USER_PWD_REG, message = "validation.user.register.loginpwd")
    @ApiModelProperty(value = "登录密码", example = "123qwe", required = true)
    private String loginPwd;

    @NotNull(message = "validation.user.register.confirmloginpwd")
    @Pattern(regexp = RegConstant.USER_PWD_REG, message = "validation.user.register.confirmloginpwd")
    @ApiModelProperty(value = "确认登录密码", example = "123qwe", required = true)
    private String confirmLoginPwd;

    @NotNull(message = "validation.user.register.paypwd")
    @Pattern(regexp = RegConstant.WITHDRAW_PWD_REG, message = "validation.user.register.paypwd")
    @ApiModelProperty(value = "支付密码", example = "1234", required = true)
    private String payPwd;
}
