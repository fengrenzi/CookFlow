package com.hnit.system.controller;

import com.hnit.common.core.controller.BaseController;
import com.hnit.common.core.domain.AjaxResult;
import com.hnit.common.core.page.TableDataInfo;
import com.hnit.system.domain.ImageManagement;
import com.hnit.system.service.IImageManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Api(tags = "图片管理")
@RestController
@RequestMapping("/image")
public class ImageManagementController extends BaseController {
    @Resource
    private IImageManagementService imageManagementService;

    @ApiOperation("查询图片列表")
    @GetMapping("/list")
    public TableDataInfo list(ImageManagement imageManagement) {
        startPage();
        List<ImageManagement> list = imageManagementService.selectImageManagementList(imageManagement);
        return getDataTable(list);
    }

    @ApiOperation("获取图片信息（JSON）")
    @GetMapping("/info/{id}")
    public AjaxResult getInfo(@PathVariable String id) {
        return success(imageManagementService.selectImageManagementById(id));
    }

    /**
     * 根据 storedName 返回图片流
     *
     * @param storedName 图片存储名（含扩展名，如 abc.jpg）
     * @param response   HttpServletResponse
     */
    @GetMapping(value = "/{storedName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public void getImage(@PathVariable String storedName, HttpServletResponse response) {
        // 1. 查询数据库
        ImageManagement image = imageManagementService.selectImageManagementByStoredName(storedName);
        if (image == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 2. 构建文件完整路径
        String fullPath = image.getStoragePath() + File.separator + image.getStoredName();
        File file = new File(fullPath);
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 3. 设置响应头 Content-Type（根据文件扩展名简单判断）
        String fileName = file.getName();
        if (fileName.endsWith(".png")) {
            response.setContentType("image/png");
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            response.setContentType("image/jpeg");
        } else if (fileName.endsWith(".gif")) {
            response.setContentType("image/gif");
        } else {
            response.setContentType("application/octet-stream");
        }

        // 4. 将文件写入响应流
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation("新增图片")
    @PostMapping
    public AjaxResult add(@RequestBody ImageManagement imageManagement) {
        imageManagement.setId(UUID.randomUUID().toString());
        return toAjax(imageManagementService.insertImageManagement(imageManagement));
    }

    @ApiOperation("修改图片")
    @PutMapping
    public AjaxResult edit(@RequestBody ImageManagement imageManagement) {
        return toAjax(imageManagementService.updateImageManagement(imageManagement));
    }

    @ApiOperation("删除图片")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(imageManagementService.deleteImageManagementByIds(ids));
    }
}