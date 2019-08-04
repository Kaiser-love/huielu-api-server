package com.ronghui.service.common.util;

import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: VideoUploadServiceUtil
 * @description:
 * @author: dongyang_wu
 * @create: 2019-06-11 12:06
 */
@Slf4j
public class VideoUploadServiceUtil {
    public static void videoUpload(String path) {
        VodUploadClient client = new VodUploadClient("AKIDzyqbM3y9RXTI0bsNWZR8f4HJD6pPq9HE", "vmS1fEcqunY8ZsBKJ7nQxKWXQOScp2BH");
        VodUploadRequest request = new VodUploadRequest();
        request.setMediaFilePath(path);
        // 携带封面
        // request.setCoverFilePath("/data/videos/Wildlife.jpg");
        try {
            VodUploadResponse response = client.upload("ap-guangzhou", request);
            System.out.println(response.getFileId());
            log.info("Upload FileId = {}", response.getFileId());
        } catch (Exception e) {
            // 业务方进行异常处理
            log.error("Upload Err", e);
        }
    }
}