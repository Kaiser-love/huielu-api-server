package com.ronghui.service.common.util;

import com.ronghui.service.common.util.http.HttpHelper;
import com.ronghui.service.context.SpringContextBeanService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;


/**
 * @program: monitor
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-12 23:07
 */
@Component
public class TensorFlowUtil {
    private static HttpHelper httpHelper;

    public static String sendImgToDjango(MultipartFile file) throws Exception {
        if (httpHelper == null)
            httpHelper = SpringContextBeanService.getBean("HttpHelper");
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(file.getBytes());
        ResponseEntity result = httpHelper.postWithResultString(httpHelper.getDjangoUrl(), null, SqlUtil.getSqlMap("files", encode));
        return result.getBody().toString();
    }
}