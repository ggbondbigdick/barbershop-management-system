package com.barber.mapping;

import lombok.Data;

/**
 * ClassName: ReserveVO
 * Description:
 */
@Data
public class ReserveVO {

    /**
     * ID
     */
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 预约人
     */
    private String reservePerson;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 类型 1-项目 2-员工
     */
    private String type;

    /**
     * 对象
     */
    private String reserveObject;

    /**
     * 对象图片
     */
    private String relatedThumbnail;

    /**
     * 对象名称
     */
    private String relatedName;

    /**
     * 对象介绍
     */
    private String relatedIntroduce;

    /**
     * 对象价格
     */
    private double relatedPrice;

    /**
     * 指定理发师
     */
    private String reserveStaff;

    /**
     * 指定理发师名称
     */
    private String reserveStaffName;

    /**
     * 指定理发师头像
     */
    private String reserveStaffAvatar;

    /**
     * 指定理发师擅长类型
     */
    private String reserveStaffGoodType;

    /**
     * 时间
     */
    private String reserveTime;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 状态 1.预约中 2.预约成功 3.待支付 4.正在服务 5.服务完成 6.预约失败 7.已取消
     */
    private String state;

    /**
     * 是否评价 1-是 2-否
     */
    private String evaluate;

    /**
     * 备注
     */
    private String remark;

}
