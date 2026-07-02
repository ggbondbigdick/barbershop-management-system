package com.barber.mapping;

import com.barber.tool.data.PageDTO;
import lombok.Data;

@Data
public class EvaluateDTO extends PageDTO {

    /**
     * ID
     */
    private String id;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 类型 1-项目 2-员工
     */
    private String type;

    /**
     * 对象
     */
    private String reserveObject;

    /**
     * 内容
     */
    private String content;

}
