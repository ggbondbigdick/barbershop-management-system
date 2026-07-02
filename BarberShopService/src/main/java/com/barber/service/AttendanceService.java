package com.barber.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.barber.entity.Attendance;
import com.barber.entity.User;
import com.barber.mapper.AttendanceMapper;
import com.barber.mapping.AttendanceDTO;
import com.barber.mapping.AttendanceVO;
import com.barber.tool.DateTool;
import com.barber.tool.LoginUtil;
import com.barber.tool.PageTool;
import com.barber.tool.PreviewTool;
import com.barber.tool.data.ResultVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 考勤 服务实现类
 * </p>
 *
 * @author barber
 * @since 2025-04-08
 */
@Service
public class AttendanceService extends ServiceImpl<AttendanceMapper, Attendance> {

    @Resource
    private PreviewTool previewTool;


    /**
     * 获取考勤分页列表
     * @param attendanceDTO
     * @return
     */
    public ResultVO getAttendancePaginationList(AttendanceDTO attendanceDTO) {

        // 判断角色
        if (StrUtil.equals("3", LoginUtil.getCurrentUserRole())) {
            attendanceDTO.setStaff(LoginUtil.getCurrentUserId());
        }

        PageHelper.startPage(attendanceDTO.getPage(), attendanceDTO.getSize());

        // 查询考勤列表
        List<AttendanceVO> attendanceList = baseMapper.queryAttendanceList(attendanceDTO);

        PageInfo<AttendanceVO> pageInfo = new PageInfo<>(attendanceList);

        return PageTool.convertResult(pageInfo);
    }


    /**
     * 获取考勤详情
     * @param id
     * @return
     */
    public ResultVO getAttendanceInfo(String id) {

        Optional.ofNullable(id).orElseThrow(() -> new IllegalArgumentException("ID 参数错误"));

        AttendanceVO attendanceVO = baseMapper.queryAttendanceInfo(id);

        if (ObjectUtil.isNotEmpty(attendanceVO)) {
            attendanceVO.setStaffAvatar(StrUtil.isBlank(attendanceVO.getStaffAvatar()) ? "" : previewTool.getFilePreviewUrl(attendanceVO.getStaffAvatar()));
            // 工资计算
            attendanceVO.setSalary(calculateSalary(attendanceVO));
            // 计算考勤天数
            JSONObject resJson = calculateAttendanceDays(attendanceVO);
            attendanceVO.setAttendanceDays(resJson.getInteger("attendanceDays"));
            attendanceVO.setOverTimeDays(resJson.getInteger("overTimeDays"));
            attendanceVO.setLeaveDays(resJson.getInteger("leaveDays"));
        }

        return PageTool.convertResult(attendanceVO);
    }


    /**
     * 工资计算
     * @param attendanceVO
     * @return
     */
    private double calculateSalary(AttendanceVO attendanceVO) {

        // 获取当前月份
        String currentMonth = StrUtil.sub(attendanceVO.getCreateTime(), 0, 7);

        // 获取当月最后一日为几号
        int lastDay = DateTool.getLastDayOfMonth(DateTool.getCurrentDate());

        // 获取当月的考勤记录
        String currentMonthStartTime = StrUtil.format("{}-0{} 00:00:00", currentMonth, 1);
        String currentMonthEndTime = StrUtil.format("{}-{} 23:59:59", currentMonth, lastDay);

        // 查询当月出勤天数
        long count = count(Wrappers.<Attendance>lambdaQuery().eq(Attendance::getType, "1")
                .eq(Attendance::getStaff, attendanceVO.getStaff())
                .between(Attendance::getCreateTime, currentMonthStartTime, currentMonthEndTime));
        
        // 查询当月加班天数
        long overTimeCount = count(Wrappers.<Attendance>lambdaQuery().eq(Attendance::getOvertime, "1")
                .eq(Attendance::getOvertime, "1")
                .eq(Attendance::getStaff, attendanceVO.getStaff())
                .between(Attendance::getCreateTime, currentMonthStartTime, currentMonthEndTime));

        double salary = ((count * 100) + (overTimeCount * 30));

        return salary;
    }


    /**
     * 考勤天数计算
     * attendanceVO
     * @return
     */
    private JSONObject calculateAttendanceDays(AttendanceVO attendanceVO) {

        // 获取当前月份
        String currentMonth = StrUtil.sub(DateTool.getCurrentDate(), 0, 7);

        // 获取当月最后一日为几号
        int lastDay = DateTool.getLastDayOfMonth(DateTool.getCurrentDate());

        // 获取当月的考勤记录
        String currentMonthStartTime = StrUtil.format("{}-0{} 00:00:00", currentMonth, 1);
        String currentMonthEndTime = StrUtil.format("{}-{} 23:59:59", currentMonth, lastDay);

        long attendanceDays = count(Wrappers.<Attendance>lambdaQuery().eq(Attendance::getType, "1")
                .eq(Attendance::getStaff, attendanceVO.getStaff())
                .between(Attendance::getCreateTime, currentMonthStartTime, currentMonthEndTime));

        long overTimeDays = count(Wrappers.<Attendance>lambdaQuery().eq(Attendance::getType, "1")
                .eq(Attendance::getOvertime, "1")
                .eq(Attendance::getStaff, attendanceVO.getStaff())
                .between(Attendance::getCreateTime, currentMonthStartTime, currentMonthEndTime));

        long  leaveDays = count(Wrappers.<Attendance>lambdaQuery().eq(Attendance::getType, "2")
                .eq(Attendance::getStaff, attendanceVO.getStaff())
                .between(Attendance::getCreateTime, currentMonthStartTime, currentMonthEndTime));

        JSONObject returnJson = new JSONObject();
        returnJson.put("attendanceDays", (int) attendanceDays);
        returnJson.put("overTimeDays", (int) overTimeDays);
        returnJson.put("leaveDays", (int) leaveDays);

        return returnJson;
    }


    /**
     * 新增(修改)考勤
     * @param attendanceDTO
     * @return
     */
    public ResultVO addOrModifyAttendance(AttendanceDTO attendanceDTO) {

        String time = DateTool.getCurrentTime();

        Attendance attendance = BeanUtil.copyProperties(attendanceDTO, Attendance.class);
        attendance.setUpdateTime(time);

        if (StrUtil.isBlank(attendance.getId())) {
            attendance.setId(IdUtil.simpleUUID());
            attendance.setCreateTime(time);
            save(attendance);
        } else {
            updateById(attendance);
        }

        return new ResultVO(true);
    }


    /**
     * 一键签到
     * @return
     */
    public ResultVO signIn() {

        String time = DateTool.getCurrentTime();

        // 判断是否有今日的考勤记录
        if (baseMapper.selectCount(Wrappers.<Attendance>lambdaQuery().eq(Attendance::getType, "1")
                .between(Attendance::getCreateTime, StrUtil.format("{} 00:00:00", DateTool.getCurrentDate()),
                        StrUtil.format("{} 23:59:59", DateTool.getCurrentDate()))) > 0) {
            return new ResultVO(false, "今日员工已考勤");
        }

        // 获取所有员工 Id
        List<User> userList = SpringUtil.getBean(UserService.class).list(Wrappers.<User>lambdaQuery().eq(User::getRole, "3"));

        if (CollectionUtil.isEmpty(userList)) {
            return new ResultVO(false, "员工信息为空");
        }

        List<String> userIds = userList.stream().map(User::getId).collect(Collectors.toList());

        // 封装考勤记录集合
        List<Attendance> attendanceList = userIds.stream().map(id -> {
            Attendance attendance = new Attendance();
            attendance.setId(IdUtil.simpleUUID());
            attendance.setCreateTime(time);
            attendance.setUpdateTime(time);
            attendance.setType("1");
            attendance.setStaff(id);
            attendance.setOvertime("2");
            attendance.setRemark("一键签到");
            return attendance;
        }).collect(Collectors.toList());

        // 批量保存,签到记录
        Optional.ofNullable(attendanceList).ifPresent(this::saveBatch);

        return new ResultVO(true);
    }


    /**
     * 删除考勤
     * @param id
     * @return
     */
    public ResultVO deleteAttendance(String id) {

        Optional.ofNullable(id).orElseThrow(() -> new IllegalArgumentException("ID 参数错误"));

        return new ResultVO(removeById(id));
    }

}
