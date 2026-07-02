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
 * 考勤
 * </p>
 *
 * @author barber
 * @since 2025-04-08
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("attendance")
public class Attendance extends Model<Attendance> {

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
     * 类型 1-上班 2-请假
     */
    @TableField("type")
    private String type;

    /**
     * 员工
     */
    @TableField("staff")
    private String staff;

    /**
     * 加班 1-是 2-否
     */
    @TableField("overtime")
    private String overtime;

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
