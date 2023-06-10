package com.juhai.commons.service;

import com.juhai.commons.entity.DayReport;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_day_report】的数据库操作Service
* @createDate 2023-06-10 10:38:15
*/
public interface DayReportService extends IService<DayReport> {

    void batchInsertOrUpdate(List<DayReport> list) throws Exception;

    void insertOrUpdate(DayReport report) throws Exception;

}
