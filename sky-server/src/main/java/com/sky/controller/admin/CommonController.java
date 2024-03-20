package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传{}", file.getName());

        try {
            String file_name = file.getOriginalFilename();

            String extention = file_name.substring(file_name.lastIndexOf("."));

            String objectName = UUID.randomUUID().toString() + extention;
            String upload_filepath = null;

            upload_filepath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(upload_filepath);
        } catch (IOException e) {
            log.info("未上传文件或上传错误：{}", file);
//            throw new RuntimeException(e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
