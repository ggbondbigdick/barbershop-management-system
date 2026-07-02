package com.barber.mapping;

import com.barber.tool.data.PageDTO;
import lombok.Data;

/**
 * ClassName: UserParam
 * Description:
 */
@Data
public class UserDTO extends PageDTO {

    /**
     * ID
     */
    private String id;

    /**
     * 查询参数
     */
    private String queryParam;

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
    private String password;

    /**
     * 擅长类型
     */
    private String goodType;

    /**
     * 角色 1-管理员 2-用户 3-员工
     */
    private String role;

    /**
     * 余额
     */
    private double balance;

    /**
     * 原密码
     */
    private String oldPassword;

}
