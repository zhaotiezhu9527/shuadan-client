package com.juhai.commons.service.impl;

import cn.hutool.core.collection.CollStreamUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.Paramter;
import com.juhai.commons.service.ParamterService;
import com.juhai.commons.mapper.ParamterMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【t_paramter】的数据库操作Service实现
* @createDate 2023-05-18 15:38:02
*/
@Service
public class ParamterServiceImpl extends ServiceImpl<ParamterMapper, Paramter>
    implements ParamterService{

    @Override
    public Map<String, String> getAllParamByMap() {
        List<Paramter> list = list();
        return CollStreamUtil.toMap(list, Paramter::getParamKey, Paramter::getParamValue);
    }
}




