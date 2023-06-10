package com.juhai.commons.mapper;

import com.juhai.commons.entity.DayReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_day_report】的数据库操作Mapper
* @createDate 2023-06-10 10:38:15
* @Entity com.juhai.commons.entity.DayReport
*/
public interface DayReportMapper extends BaseMapper<DayReport> {

    int batchInsertOrUpdate(List<DayReport> list);

    int insertOrUpdate(@Param("report") DayReport report);

}




