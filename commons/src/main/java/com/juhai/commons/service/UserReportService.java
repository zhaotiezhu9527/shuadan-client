package com.juhai.commons.service;

import com.juhai.commons.entity.UserReport;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【t_user_report】的数据库操作Service
* @createDate 2023-05-24 11:32:10
*/
public interface UserReportService extends IService<UserReport> {

    void insertOrUpdate(UserReport report) throws Exception;
}
