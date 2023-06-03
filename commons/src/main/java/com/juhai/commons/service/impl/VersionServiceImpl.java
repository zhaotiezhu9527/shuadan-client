package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.Version;
import com.juhai.commons.service.VersionService;
import com.juhai.commons.mapper.VersionMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_version】的数据库操作Service实现
* @createDate 2023-05-25 10:42:26
*/
@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version>
    implements VersionService{

}




