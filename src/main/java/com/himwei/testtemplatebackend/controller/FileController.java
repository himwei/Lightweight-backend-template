package com.himwei.testtemplatebackend.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.himwei.testtemplatebackend.common.BaseResponse;
import com.himwei.testtemplatebackend.common.ResultUtils;
import com.himwei.testtemplatebackend.exception.BusinessException;
import com.himwei.testtemplatebackend.exception.ErrorCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    // 1. 指定本地保存位置 (项目根目录/uploaded_files/)
    // System.getProperty("user.dir") 获取当前项目路径
    private static final String UPLOAD_ROOT_PATH = System.getProperty("user.dir") + "/uploaded_files/";

    /**
     * 文件上传
     * @param file 前端传来的文件
     * @return 文件的访问 URL
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件为空");
        }

        // 2. 生成新文件名 (防止重名覆盖)
        String originalFilename = file.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename); // 需要引入 Hutool，或者自己写 split
        // 如果没引入 Hutool，可以用: String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        String uuid = IdUtil.simpleUUID(); // 或者 UUID.randomUUID().toString()
        String newFileName = uuid + "." + suffix;

        // 3. 创建文件夹
        File folder = new File(UPLOAD_ROOT_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 4. 保存文件
        File saveFile = new File(UPLOAD_ROOT_PATH + newFileName);
        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件保存失败");
        }

        // 5. 返回可访问的 URL
        // 假设你的后端端口是 9999，这里返回映射后的路径
        // 注意：这里返回的是相对路径，前端拼接域名，或者后端直接返回全路径
        String fileUrl = "/api/images/" + newFileName;
        return ResultUtils.success(fileUrl);
    }
}
