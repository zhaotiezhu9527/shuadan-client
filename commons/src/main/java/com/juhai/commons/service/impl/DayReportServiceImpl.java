package com.juhai.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juhai.commons.entity.DayReport;
import com.juhai.commons.service.DayReportService;
import com.juhai.commons.mapper.DayReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_day_report】的数据库操作Service实现
* @createDate 2023-06-10 10:38:15
*/
@Service
public class DayReportServiceImpl extends ServiceImpl<DayReportMapper, DayReport>
    implements DayReportService{

    @Autowired
    private DayReportMapper dayReportMapper;

    @Override
    public void batchInsertOrUpdate(List<DayReport> list) throws Exception {
        int i = dayReportMapper.batchInsertOrUpdate(list);
        if (i <= 0) {
            throw new Exception("修改日报表失败");
        }
    }

    @Override
    public void insertOrUpdate(DayReport report) throws Exception {
        int i = dayReportMapper.insertOrUpdate(report);
        if (i <= 0) {
            throw new Exception("修改日报表失败");
        }
    }
}




