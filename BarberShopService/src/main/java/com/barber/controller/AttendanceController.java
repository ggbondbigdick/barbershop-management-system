package com.barber.controller;


import com.barber.mapping.AttendanceDTO;
import com.barber.service.AttendanceService;
import com.barber.tool.data.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 考勤 前端控制器
 * </p>
 *
 * @author barber
 * @since 2025-04-08
 */
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Resource
    private AttendanceService attendanceService;


    @PostMapping("/list")
    public ResultVO getAttendancePaginationList(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.getAttendancePaginationList(attendanceDTO);
    }


    @GetMapping("/info")
    public ResultVO getAttendanceInfo(@RequestParam("id") String id) {
        return attendanceService.getAttendanceInfo(id);
    }


    @PostMapping("/add")
    public ResultVO addAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.addOrModifyAttendance(attendanceDTO);
    }


    @PostMapping("/modify")
    public ResultVO modifyAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.addOrModifyAttendance(attendanceDTO);
    }


    @GetMapping("/sign")
    public ResultVO signIn() {
        return attendanceService.signIn();
    }


    @GetMapping("/del")
    public ResultVO deleteAttendance(@RequestParam("id") String id) {
        return attendanceService.deleteAttendance(id);
    }

}
