package com.barber.tool;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ClassName: PreviewTool
 * Description:
 */
@Component
public class PreviewTool {

    @Value("${file-preview-url}")
    private String filePreviewUrl;

    /**
     * 获取文件预览地址
     *
     * @param fileId 文件id
     * @return 文件预览地址
     */
    public String getFilePreviewUrl(String fileId) {
        return StrUtil.format("{}{}", filePreviewUrl, fileId);
    }

}
