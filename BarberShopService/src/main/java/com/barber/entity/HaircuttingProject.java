package com.barber.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 理发项目
 * </p>
 *
 * @author barber
 * @since 2025-04-08
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("haircutting_project")
public class HaircuttingProject extends Model<HaircuttingProject> {

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
     * 更新时间
     */
    @TableField("update_time")
    private String updateTime;

    /**
     * 类型 1-洗发 2-理发 3-烫发 4-染发 5-头发保养
     */
    @TableField("type")
    private String type;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 缩略图
     */
    @TableField("thumbnail")
    private String thumbnail;

    /**
     * 介绍
     */
    @TableField("introduce")
    private String introduce;

    /**
     * 价格
     */
    @TableField("price")
    private Double price;

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
