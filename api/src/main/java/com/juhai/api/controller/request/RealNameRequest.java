package com.juhai.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "实名认证请求类", description = "实名认证请求参数")
public class RealNameRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "身份证号码", example = "465465465123", required = true)
    private String idCardNo;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "真实姓名", example = "张三", required = true)
    private String realName;

}
