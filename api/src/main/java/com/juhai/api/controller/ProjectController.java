package com.juhai.api.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juhai.api.utils.JwtUtils;
import com.juhai.commons.entity.Project;
import com.juhai.commons.entity.User;
import com.juhai.commons.service.ParamterService;
import com.juhai.commons.service.ProjectService;
import com.juhai.commons.service.UserService;
import com.juhai.commons.utils.MsgUtil;
import com.juhai.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "项目相关", tags = "项目相关")
@RequestMapping("/project")
@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ParamterService paramterService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取项目列表")
    @GetMapping("/list")
    public R list(HttpServletRequest httpServletRequest) {
        List<Project> list = projectService.list(
                new LambdaQueryWrapper<Project>()
                        .eq(Project::getStatus, 0)
                        .orderByDesc(Project::getSort)
        );
        JSONArray array = new JSONArray();
        if (CollUtil.isNotEmpty(list)) {
            Map<String, String> map = paramterService.getAllParamByMap();
            for (Project project : list) {
                JSONObject temp = new JSONObject();
                temp.put("projectId", project.getId());
                temp.put("projectName", project.getProjectName());
                temp.put("projectAmount", project.getProjectAmount());
                temp.put("incomeRate", project.getIncomeRate());
                temp.put("limitTime", project.getLimitTime());
                temp.put("minAmount", project.getMinAmount());
                temp.put("startTime", project.getStartTime());
                temp.put("schedule", project.getSchedule());
                temp.put("guaranteeCompany", map.get("guarantee_company"));
                temp.put("img", map.get("resource_domain") + project.getImg());
                array.add(temp);
            }
        }
        return R.ok().put("data", array);
    }

    @ApiOperation(value = "获取项目详情")
    @GetMapping("/detail/{id}")
    public R list(HttpServletRequest httpServletRequest, @PathVariable("id") String id) {
        Project project = projectService.getById(id);
        if (project == null) {
            return R.error(MsgUtil.get("system.project.invalid"));
        }

        if(project.getStatus().intValue() == 1 || !DateUtil.isIn(new Date(), project.getStartTime(), project.getEndTime())) {
            return R.error(MsgUtil.get("system.project.finshed"));
        }

        Map<String, String> map = paramterService.getAllParamByMap();
        JSONObject temp = new JSONObject();
        temp.put("projectId", project.getId());
        temp.put("projectName", project.getProjectName());
        temp.put("projectAmount", project.getProjectAmount());
        temp.put("incomeRate", project.getIncomeRate());
        temp.put("limitTime", project.getLimitTime());
        temp.put("minAmount", project.getMinAmount());
        temp.put("startTime", project.getStartTime());
        temp.put("schedule", project.getSchedule());
        temp.put("guaranteeCompany", map.get("guarantee_company"));
        temp.put("img", map.get("resource_domain") + project.getImg());

        String userName = JwtUtils.getUserName(httpServletRequest);
        User user = userService.getUserByName(userName);
        temp.put("userBalance", user.getBalance());

        return R.ok().put("data", temp);
    }
}
