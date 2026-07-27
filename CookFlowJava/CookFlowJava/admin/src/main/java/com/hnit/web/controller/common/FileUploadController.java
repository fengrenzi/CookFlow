package com.hnit.web.controller.common;

import com.hnit.common.annotation.Anonymous;
import com.hnit.common.config.hnitConfig;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.common.exception.NonCaptureException;
import com.hnit.common.utils.StringUtils;
import com.hnit.common.utils.file.FileUploadUtils;
import com.hnit.common.utils.file.FileUtils;
import com.hnit.framework.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    private Logger log = LoggerFactory.getLogger(FileUploadController.class);

    @Resource
    private ServerConfig serverConfig;

    @Anonymous
    @PostMapping("/upload")
    @SuppressWarnings("DuplicatedCode")
    public AjaxResult uploadFile(MultipartFile file) {
        try {
            log.info("文件 {} 上传中...", file.getOriginalFilename());
            // 上传文件路径
            String filePath = hnitConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            log.info("文件 {} 上传成功！", file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            throw new NonCaptureException(StringUtils.format("文件 {} 上传失败！", file.getOriginalFilename()), e);
        }
    }
}