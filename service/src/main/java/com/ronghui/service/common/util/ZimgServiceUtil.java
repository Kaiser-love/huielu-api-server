package com.ronghui.service.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ronghui.service.common.response.ResponseHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @program: ZimgUtil
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-19 23:13
 */
@Component
public class ZimgServiceUtil {
    @Value("${zimg.zimgUpload}")
    private String zimgUrl = "http://47.112.204.145:4869/upload";
    @Value("${zimg.uriTemp}")
    private String zimgTemp = "http://47.112.204.145:4869/%s";

    public byte[] imageBinary(MultipartFile file) {
        String fileType = getExtensionName(file.getContentType());
        BufferedImage bi;
        try {
            bi = ImageIO.read(file.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bi, fileType, bos);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 向指定 URL 发送POST方法的请求
    public String sendPost(MultipartFile file) {
        String fileType = getExtensionName(file.getContentType());
        byte[] PostData = imageBinary(file);
        OutputStream outStream = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(zimgUrl);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", fileType);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //二进制
            outStream = conn.getOutputStream();
            outStream.write(PostData);
            outStream.flush();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        JSONObject apiResult = JSON.parseObject(result);
        if (apiResult.getBoolean("ret")) {
            JSONObject data = JSON.parseObject(apiResult.get("info").toString());
            return data.getString("md5");
        } else
            return ResponseHelper.OPERATION_BADREQUEST;
    }

    public byte[] getImgFromZimg(String zimgId) throws Exception {
        BufferedImage sourceImg;
        InputStream imgUrl = new URL(String.format(zimgTemp, zimgId)).openStream();
        sourceImg = ImageIO.read(imgUrl);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        ImageIO.write(sourceImg, "png", baos);//写入流中
        byte[] bytes = baos.toByteArray();//转换成字节
        return bytes;
    }

    public String getExtensionName(String filename) {
        return filename.substring(filename.indexOf("/") + 1);
    }
}