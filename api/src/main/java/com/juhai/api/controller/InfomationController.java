package com.juhai.api.controller;

import com.juhai.api.controller.request.InfomationRequest;
import com.juhai.commons.entity.Information;
import com.juhai.commons.service.InformationService;
import com.juhai.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@Api(value = "收集信息相关", tags = "收集信息")
@RequestMapping("/infomation")
@RestController
public class InfomationController {

    @Autowired
    private InformationService informationService;

    @ApiOperation(value = "收集信息")
    @PostMapping("/coll")
    public R register(@Validated InfomationRequest request) {
        Information information = new Information();
        information.setUserPhone(request.getUserPhone());
        information.setCreateTime(new Date());
        information.setModify(new Date());
        informationService.save(information);
        return R.ok();
    }
}
