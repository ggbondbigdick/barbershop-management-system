package com.barber.controller;


import com.barber.service.FileService;
import com.barber.tool.data.ResultVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 文件 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileService fileService;


    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return {@link ResultVO}
     */
    @PostMapping("upload")
    public ResultVO uploadFile(MultipartFile file) { return fileService.uploadFile(file); }


    /**
     * 获取文件信息
     * @param id
     * @return
     */
    @GetMapping("info")
    public ResultVO getFileInfo(@RequestParam("id") String id) { return fileService.getFileInfo(id); }


    /**
     * 预览文件
     *
     * @param id  文件 ID
     * @param res Http Servlet 响应
     */
    @GetMapping("preview/{id}")
    public void previewFile(@PathVariable String id, HttpServletResponse res) { fileService.filePreview(id, res); }


    /**
     * 下载文件
     *
     * @param id  文件 ID
     * @param res Http Servlet 响应
     */
    @GetMapping("download/{id}")
    public void downloadFile(@PathVariable String id, HttpServletResponse res) { fileService.fileDownload(id, res); }



    /**
     * 删除文件
     *
     * @param id ID
     * @return {@link ResultVO}
     */
    @GetMapping("del/{id}")
    public ResultVO deleteFile(@PathVariable String id) { return fileService.fileDelete(id); }
}
