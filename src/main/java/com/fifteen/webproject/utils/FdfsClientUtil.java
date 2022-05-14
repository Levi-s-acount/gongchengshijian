package com.fifteen.webproject.utils;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author Pymjl
 * @date 2022/1/21 20:30
 */
@Log4j2
@Component
@Data
public class FdfsClientUtil {
    @Autowired
    private FastFileStorageClient storageClient;

    @Value("${fdfs.prefix}")
    private String prefix;

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return 文件访问地址
     */
    public String uploadFile(MultipartFile file) throws IOException {
        log.info(file.getName());
        log.info(FilenameUtils.getExtension(file.getOriginalFilename()));
        log.info(file.getContentType());
        log.info(FilenameUtils.getExtension(file.getName()));
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), null);
        return getResAccessUrl(storePath);
    }

    /**
     * 将一段字符串生成一个文件上传
     *
     * @param content       文件内容
     * @param fileExtension 文件扩展名
     */
    public String uploadFile(String content, String fileExtension) {
        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = storageClient.uploadFile(stream, buff.length, fileExtension, null);
        return getResAccessUrl(storePath);
    }

    /**
     * 封装图片完整URL地址
     *
     * @param storePath 返回的path
     * @return String
     */
    private String getResAccessUrl(StorePath storePath) {
        String fileUrl = storePath.getFullPath();
        return prefix + fileUrl;
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件访问地址
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            log.warn("文件路径为空");
            return;
        }
        log.info("开始删除文件==>" + fileUrl);
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
            log.info("删除文件成功");
        } catch (FdfsUnsupportStorePathException e) {
            e.printStackTrace();
            log.warn(e.getMessage());
        }
    }
}