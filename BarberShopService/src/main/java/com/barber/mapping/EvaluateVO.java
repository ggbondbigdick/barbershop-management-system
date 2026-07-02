package com.barber.mapping;

import lombok.Data;

@Data
public class EvaluateVO {

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
     * 评价人
     */
    private String evaluator;

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
     * 项目名称
     */
    private String projectName;

    /**
     * 内容
     */
    private String content;

}
