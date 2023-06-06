package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.Area;
import com.juhai.commons.service.AreaService;
import com.juhai.commons.mapper.AreaMapper;
import icu.mhb.mybatisplus.plugln.base.service.impl.JoinServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_area】的数据库操作Service实现
* @createDate 2023-06-04 19:23:17
*/
@Service
public class AreaServiceImpl extends JoinServiceImpl<AreaMapper, Area>
    implements AreaService{

}




