package com.barber.mapping;

import com.baomidou.mybatisplus.annotation.TableField;
import com.barber.tool.data.PageDTO;
import lombok.Data;

/**
 * ClassName: HaircuttingProjectDTO
 * Description:
 */
@Data
public class HaircuttingProjectDTO extends PageDTO {

    /**
     * ID
     */
    private String id;

    /**
     * 类型 1-洗发 2-理发 3-烫发 4-染发 5-头发保养
     */
    private String type;

    /**
     * 名称
     */
    private String name;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 介绍
     */
    private String introduce;

    /**
     * 价格
     */
    private Double price;

    /**
     * 备注
     */
    private String remark;

}
