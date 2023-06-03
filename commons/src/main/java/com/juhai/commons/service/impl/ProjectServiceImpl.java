package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.Project;
import com.juhai.commons.service.ProjectService;
import com.juhai.commons.mapper.ProjectMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_project】的数据库操作Service实现
* @createDate 2023-05-18 15:38:06
*/
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project>
    implements ProjectService{

}




