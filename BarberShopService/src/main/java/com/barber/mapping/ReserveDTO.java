package com.barber.mapping;

import com.barber.tool.data.PageDTO;
import lombok.Data;

/**
 * ClassName: ReserveDTO
 * Description:
 */
@Data
public class ReserveDTO extends PageDTO {

    /**
     * ID
     */
    private String id;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 类型 1-项目 2-员工
     */
    private String type;

    /**
     * 对象
     */
    private String reserveObject;

    /**
     * 指定理发师
     */
    private String reserveStaff;

    /**
     * 时间
     */
    private String reserveTime;

    /**
     * 状态 1.预约中 2.预约成功 3.待支付 4.正在服务 5.服务完成 6.预约失败 7.已取消
     */
    private String state;

    /**
     * 审核状态 1.通过 2.拒绝
     */
    private String checkState;

    /**
     * 是否评价 1-是 2-否
     */
    private String evaluate;

    /**
     * 备注
     */
    private String remark;

}
