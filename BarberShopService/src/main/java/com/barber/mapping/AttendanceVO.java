package com.barber.mapping;

import lombok.Data;

/**
 * ClassName: AttendanceVO
 * Description:
 */
@Data
public class AttendanceVO {

    /**
     * ID
     */
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 类型 1-上班 2-请假
     */
    private String type;

    /**
     * 员工
     */
    private String staff;

    /**
     * 员工姓名
     */
    private String staffName;

    /**
     * 员工头像
     */
    private String staffAvatar;

    /**
     * 员工注册时间
     */
    private String staffRegisterTime;

    /**
     * 加班 1-是 2-否
     */
    private String overtime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 出勤天数
     */
    private int attendanceDays;

    /**
     * 加班天数
     */
    private int overTimeDays;

    /**
     * 请假天数
     */
    private int leaveDays;

    /**
     * 工资
     */
    private double salary;

}
