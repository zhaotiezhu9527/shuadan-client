package com.juhai.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "分页请求类")
public class PageBaseRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "当前页数", example = "1", required = false)
    private String page;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "分页条数", example = "10", required = false)
    private String limit;
}
