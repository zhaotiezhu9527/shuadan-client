package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.Goods;
import com.juhai.commons.service.GoodsService;
import com.juhai.commons.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_goods】的数据库操作Service实现
* @createDate 2023-06-04 19:23:26
*/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{

}




