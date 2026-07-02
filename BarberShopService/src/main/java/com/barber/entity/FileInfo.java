package com.barber.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 文件
 * </p>
 *
 * @author qyl
 * @since 2023-10-15
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_file")
public class FileInfo extends Model<FileInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    private String id;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 上传文件名
     */
    private String fileNameUpload;

    /**
     * 保存文件名
     */
    private String fileNameSave;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 保存路径
     */
    private String savePath;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
