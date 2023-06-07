package com.juhai.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "修改昵称", description = "修改昵称参数")
public class UpdNickNameRequest {

    @NotNull(message = "validation.user.register.nickname")
    @ApiModelProperty(value = "昵称", example = "赵铁柱", required = true)
    private String nickName;
}
