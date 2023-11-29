package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(value = "通用接口", tags = {"通用接口"})
@Slf4j
public class CommonController {

    @Value("${sky.file.upload-dir}") // 获取配置中的文件上传路径
    private String uploadDir;


    /**
     * 上传文件并返回结果
     *
     * @return 上传结果，成功时返回"上传成功"
     */
    @PostMapping("/upload")
    @ApiOperation(value = "上传文件", notes = "上传文件并返回结果")
    public Result<String> upload(MultipartFile file){
        log.info("上传文件：{}", file);

        // 确保文件不为空
        if (file.isEmpty()) {
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }

        try {
            // 生成文件名
            String fileName = UUID.randomUUID().toString();
            // 获取文件扩展名
            String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // 创建新的文件名
            String newFileName = fileName + fileExtension;
            // 获取文件存储的路径
            String filePath = uploadDir + File.separator + newFileName;

            // 将文件保存到本地
            Path path = Paths.get(filePath);
            Files.copy(file.getInputStream(), path);

            // 构建文件访问的 URL
            String fileUrl = ServletUriComponentsBuilder.fromHttpUrl("http://localhost")
                    .path("/static/") // 文件访问路径
                    .path(newFileName) // 新文件名
                    .toUriString();

            return Result.success(fileUrl);
        } catch (IOException ex) {
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }

}
