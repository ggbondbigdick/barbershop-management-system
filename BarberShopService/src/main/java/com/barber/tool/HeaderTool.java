package com.barber.tool;

import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName: HeaderTool
 * Description:
 */
public class HeaderTool {

    /**
     * 获取请求头中的时区信息
     * get the timezone information from the request header
     * @param request
     * @return
     */
    public static String getHeaderTimezoneString(HttpServletRequest request) {
        String header = request.getHeader("timezone");
        return StrUtil.isBlank(header) ? "UTC" : header;
    }

}
