package com.barber.tool;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.barber.entity.FileInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Optional;

/**
 * ClassName: FileTool
 * Description: 文件工具
 */
@Component
public class FileTool {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return
     */
    public static FileInfo upload(MultipartFile file) {

        // 获取项目所在路径
        String systemPath = System.getProperty("user.dir");

        // 文件保存路径
        String filePath = StrUtil.format("{}/{}", systemPath, "/upload");

        // 上传文件名称、后缀
        String name = FileNameUtil.getName(file.getOriginalFilename());
        String suffix = FileNameUtil.getSuffix(name);

        String fileId = IdUtil.simpleUUID();
        String fileName = StrUtil.format("{}.{}", fileId, suffix);

        File targetFile = new File(new File(filePath).getAbsolutePath() + File.separator + fileName);
        String savePath = targetFile.getAbsolutePath().replaceAll("^[a-zA-Z]:", "");

        // 封装文件信息
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(fileId);
        fileInfo.setFileNameUpload(name);
        fileInfo.setFileNameSave(fileName);
        fileInfo.setFileSuffix(suffix);
        fileInfo.setSavePath(savePath);
        fileInfo.setCreateTime(DateUtil.now());
        fileInfo.insert();

        // 判断目标文件所在的目录是否存在
        if (!targetFile.getParentFile().exists()) {
            // 如果目标文件所在的目录不存在，则创建父目录
            targetFile.getParentFile().mkdirs();
        }

        try {
            file.transferTo(targetFile);
            return fileInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return new FileInfo();
        }
    }


    /**
     * 预览文件
     *
     * @param id       文件ID
     * @param response
     * @throws IOException
     */
    public static void preview(String id, HttpServletResponse response) {

        FileInfo fileInfo = new FileInfo().setId(id).selectById();

        if (Optional.ofNullable(fileInfo).isPresent()) {

            File file = new File(fileInfo.getSavePath());

            if (file.exists()) {

                FileInputStream inputStream = null;
                OutputStream outputStream = null;

                try {

                    inputStream = new FileInputStream(fileInfo.getSavePath());
                    outputStream = response.getOutputStream();

                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }

                    //修正 Excel在“xxx.xlsx”中发现不可读取的内容。是否恢复此工作薄的内容？如果信任此工作簿的来源，请点击"是"
                    response.setHeader("Content-Length", String.valueOf(inputStream.getChannel().size()));

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        assert inputStream != null;
                        inputStream.close();
                        assert outputStream != null;
                        outputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * 删除文件
     *
     * @param id 文件ID
     * @return
     */
    public static boolean delete(String id) {

        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(id);
        fileInfo = fileInfo.selectById();

        if (Optional.ofNullable(fileInfo).isPresent()) {

            File file = new File(fileInfo.getSavePath());

            if (file.exists()) {
                // 删除数据库文件保存记录
                fileInfo.deleteById();
                return file.delete();
            }
        }

        return false;
    }


    /**
     * 判断文件是否存在
     * @param id 文件ID
     * return true 存在，false 不存在
     */
    public static boolean exists(String id) {

        FileInfo fileInfo = new FileInfo().setId(id).selectById();

        if (Optional.ofNullable(fileInfo).isPresent()) {

            File file = new File(fileInfo.getSavePath());

            return file.exists();
        }

        return false;
    }


    /**
     * 下载文件
     *
     * @param id       文件ID
     * @param response
     * @throws IOException
     */
    public static void download(String id, HttpServletResponse response) {

        FileInfo fileInfo = new FileInfo().setId(id).selectById();

        if (Optional.ofNullable(fileInfo).isPresent()) {

            File file = new File(fileInfo.getSavePath());

            if (file.exists()) {

                OutputStream outputStream = null;

                try {

                    // 取得文件名。
                    String fileName = fileInfo.getFileNameUpload();

                    // 以流的形式下载文件。
                    InputStream fis = new BufferedInputStream(new FileInputStream(fileInfo.getSavePath()));
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    fis.close();

                    // 清空response
                    response.reset();
                    // 设置response的Header
                    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                    response.addHeader("Content-Length", "" + file.length());
                    outputStream = new BufferedOutputStream(response.getOutputStream());
                    response.setContentType("application/octet-stream");
                    response.setCharacterEncoding("UTF-8");
                    outputStream.write(buffer);
                    outputStream.flush();
                    outputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
