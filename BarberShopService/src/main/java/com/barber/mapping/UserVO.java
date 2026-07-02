package com.barber.mapping;

import lombok.Data;

/**
 * ClassName: UserVO
 * Description:
 */
@Data
public class UserVO {

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
    private String age;

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
    private double password;

    /**
     * 余额
     */
    private String balance;

    /**
     * 角色 1-管理员 2-用户 3-员工
     */
    private String role;
}
