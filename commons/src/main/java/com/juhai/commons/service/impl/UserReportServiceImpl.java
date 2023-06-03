package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.UserReport;
import com.juhai.commons.service.UserReportService;
import com.juhai.commons.mapper.UserReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【t_user_report】的数据库操作Service实现
* @createDate 2023-05-24 11:32:10
*/
@Service
public class UserReportServiceImpl extends ServiceImpl<UserReportMapper, UserReport>
    implements UserReportService{

    @Autowired
    private UserReportMapper userReportMapper;

    @Override
    public void insertOrUpdate(UserReport report) throws Exception {
        int i = userReportMapper.insertOrUpdate(report);
        if (i <= 0) {
            throw new Exception("修改日报表失败");
        }
    }
}




