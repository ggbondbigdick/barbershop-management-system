package com.barber.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 预约
 * </p>
 *
 * @author barber
 * @since 2025-04-08
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("reserve")
public class Reserve extends Model<Reserve> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private String id;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private String createTime;

    /**
     * 创建用户
     */
    @TableField("create_user")
    private String createUser;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private String updateTime;

    /**
     * 类型 1-项目 2-员工
     */
    @TableField("type")
    private String type;

    /**
     * 对象
     */
    @TableField("reserve_object")
    private String reserveObject;

    /**
     * 指定理发师
     */
    @TableField("reserve_staff")
    private String reserveStaff;

    /**
     * 时间
     */
    @TableField("reserve_time")
    private String reserveTime;

    /**
     * 状态 1.预约中 2.预约成功 3.待支付 4.正在服务 5.服务完成 6.预约失败 7.已取消
     */
    @TableField("state")
    private String state;

    /**
     * 支付时间
     */
    @TableField("pay_time")
    private String payTime;

    /**
     * 是否评价 1-是 2-否
     */
    @TableField("evaluate")
    private String evaluate;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
