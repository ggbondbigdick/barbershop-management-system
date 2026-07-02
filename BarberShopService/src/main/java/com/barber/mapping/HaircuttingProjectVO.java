package com.barber.mapping;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * ClassName: HaircuttingProjectVO
 * Description:
 */
@Data
public class HaircuttingProjectVO {

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
     * 缩略图地址
     */
    private String thumbnailUrl;

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
