package com.barber.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.barber.entity.FileInfo;
import com.barber.mapper.FileMapper;
import com.barber.tool.FileTool;
import com.barber.tool.PageTool;
import com.barber.tool.data.ResultVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 文件 服务实现类
 * </p>
 *
 * @author qyl
 * @since 2023-10-15
 */
@Service
public class FileService extends ServiceImpl<FileMapper, FileInfo> {

    @Value("${file-preview-url}")
    private String previewUrl;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return {@link ResultVO}
     */
    public ResultVO uploadFile(MultipartFile file) {

        FileInfo fileInfo = FileTool.upload(file);

        JSONObject data = new JSONObject();
        data.put("id", fileInfo.getId());
        data.put("name", fileInfo.getFileNameSave());
        data.put("url", StrUtil.format("{}{}", previewUrl, fileInfo.getId()));

        return PageTool.convertResult(data);
    }


    /**
     * 获取文件信息
     * @param fileId
     * @return
     */
    public ResultVO getFileInfo(String fileId) {

        FileInfo fileInfo = getById(fileId);

        JSONObject data = new JSONObject();
        data.put("id", fileInfo.getId());
        data.put("name", fileInfo.getFileNameSave());
        data.put("url", StrUtil.format("{}{}", previewUrl, fileInfo.getId()));

        return PageTool.convertResult(data);
    }


    /**
     * 文件预览
     *
     * @param fileId 文件ID文件ID
     * @param res    事由
     */
    public void filePreview(String fileId, HttpServletResponse res) { FileTool.preview(fileId, res); }


    /**
     * 下载文件
     *
     * @param fileId 文件ID文件ID
     * @param res    事由
     */
    public void fileDownload(String fileId, HttpServletResponse res) { FileTool.download(fileId, res); }


    /**
     * 文件删除
     *
     * @param fileId 文件ID文件ID
     * @return {@link ResultVO}
     */
    public ResultVO fileDelete(String fileId) { return new ResultVO<>(FileTool.delete(fileId)); }
}
