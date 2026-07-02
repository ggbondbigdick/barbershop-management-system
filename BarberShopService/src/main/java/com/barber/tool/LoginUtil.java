package com.barber.tool;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.barber.service.UserService;

/**
 * ClassName: LoginUtil
 * Description:
 */
public class LoginUtil {

    /**
     * 获取当前登录用户ID
     * @return
     */
    public static String getCurrentUserId() {
        // 是否已经登录
        if (StpUtil.isLogin()) {
            return StpUtil.getLoginIdAsString();
        }

        return "";
    }


    /**
     * 获取当前登录用户角色
     * @return
     */
    public static String getCurrentUserRole() {
        // 是否已经登录
        if (StpUtil.isLogin()) {
            UserService userService = SpringUtil.getBean(UserService.class);
            return userService.getById(getCurrentUserId()).getRole();
        }

        return "";
    }

}
