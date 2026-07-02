package com.barber.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

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
     * 头像
     */
    private String avatar;

    /**
     * 账号
     */
    private String account;

    /**
     * 名称
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别 1-男 2-女
     */
    private String sex;

    /**
     * 联系电话
     */
    private String contactTel;

    /**
     * 密码
     */
    private String password;

    /**
     * 擅长类型 1-洗发 2-理发 3-烫发 4-染发 5-头发保养
     */
    private String goodType;

    /**
     * 余额
     */
    private double balance;

    /**
     * 角色 1-管理员 2-用户 3-员工
     */
    private String role;
}
