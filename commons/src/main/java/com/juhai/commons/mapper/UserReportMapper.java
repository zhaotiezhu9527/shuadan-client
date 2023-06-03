package com.juhai.commons.mapper;

import com.juhai.commons.entity.UserReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【t_user_report】的数据库操作Mapper
* @createDate 2023-05-24 11:32:10
* @Entity com.juhai.commons.entity.UserReport
*/
public interface UserReportMapper extends BaseMapper<UserReport> {

    int insertOrUpdate(@Param("report") UserReport report);
}




