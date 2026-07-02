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
 * 评价
 * </p>
 *
 * @author barber
 * @since 2025-04-08
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("evaluate")
public class Evaluate extends Model<Evaluate> {

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
     * 创建用户
     */
    @TableField("create_user")
    private String createUser;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private String updateTime;

    /**
     * 类型 1-项目 2-员工
     */
    @TableField("type")
    private String type;

    /**
     * 对象
     */
    @TableField("reserve_object")
    private String reserveObject;

    /**
     * 内容
     */
    @TableField("content")
    private String content;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
