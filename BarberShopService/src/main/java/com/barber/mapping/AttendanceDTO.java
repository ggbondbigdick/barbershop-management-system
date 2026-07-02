package com.barber.mapping;

import com.barber.tool.data.PageDTO;
import lombok.Data;

/**
 * ClassName: AttendanceDTO
 * Description:
 */
@Data
public class AttendanceDTO extends PageDTO {

    /**
     * ID
     */
    private String id;

    /**
     * 类型 1-上班 2-请假
     */
    private String type;

    /**
     * 员工
     */
    private String staff;

    /**
     * 员工名称
     */
    private String staffName;

    /**
     * 加班 1-是 2-否
     */
    private String overtime;

    /**
     * 备注
     */
    private String remark;


    /**
     * 开始月份
     */
    private String startMonth;


    /**
     * 结束月份
     */
    private String endMonth;

}
