package com.ronghui.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ronghui.service.common.response.SuccessAndErrorList;
import com.ronghui.service.entity.PPT;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 操作日志 服务类
 * </p>
 *
 * @author dongyang_wu
 * @since 2018-05-08
 */
public interface IPPTService extends IService<PPT> {
    Object savePPTOrPDF(String name, List<String> imgs, Integer mode);

    SuccessAndErrorList uploadPicture2PPTOrPDF(List<MultipartFile> files, String name, Integer mode) throws Exception;

    Integer countPPTs(long uid);
}
