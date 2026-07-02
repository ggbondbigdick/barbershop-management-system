package com.barber.mapper;

import com.barber.entity.Attendance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.barber.mapping.AttendanceDTO;
import com.barber.mapping.AttendanceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 考勤 Mapper 接口
 * </p>
 *
 * @author barber
 * @since 2025-04-08
 */
public interface AttendanceMapper extends BaseMapper<Attendance> {

    /**
     * 查询考勤列表
     * @param attendanceDTO
     * @return
     */
    List<AttendanceVO> queryAttendanceList(@Param("attendanceDTO") AttendanceDTO attendanceDTO);


    /**
     * 查询考勤详情
     * @param id
     * @return
     */
    AttendanceVO queryAttendanceInfo(@Param("id") String id);
}
