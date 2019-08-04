package com.ronghui.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ronghui.service.common.exception.ApiResultEnum;
import com.ronghui.service.common.exception.ApiException;
import com.ronghui.service.common.response.ResponseHelper;
import com.ronghui.service.common.response.SuccessAndErrorList;
import com.ronghui.service.common.util.*;
import com.ronghui.service.dto.BladeUser;
import com.ronghui.service.entity.*;
import com.ronghui.service.jpa.dao.PPTDao;
import com.ronghui.service.mapper.PPTMapper;
import com.ronghui.service.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.util.ArrayList;
import java.util.List;

import static com.ronghui.service.common.constant.ControllerModeConstant.*;

/**
 * @program: PPTServiceImpl
 * @description:
 * @author: dongyang_wu
 * @create: 2019-06-02 01:22
 */
@Service("PptService")
public class PPTServiceImpl extends ServiceImpl<PPTMapper, PPT> implements IPPTService {
    @Autowired
    private ZimgServiceUtil zimgServiceUtil;
    @Autowired
    private FileService fileService;
    @Autowired
    private PPTDao pptDao;

    @Override
    public SuccessAndErrorList uploadPicture2PPTOrPDF(List<MultipartFile> files, String name, Integer mode) {
        List<String> resultList = new ArrayList<>();
        List<String> successResultList = new ArrayList<>();
        List<String> errorResultList = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
//            String apiResult = zimgServiceUtil.sendPost(file);
            String apiResult = null;
            try {
                apiResult = TensorFlowUtil.sendImgToDjango(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!Func.isEmpty(apiResult)) {
                successResultList.add(fileName);
                resultList.add(apiResult);
            } else {
                errorResultList.add(fileName);
            }
        }
        if (CollectionUtils.isEmpty(resultList)) {
            throw new ApiException(ApiResultEnum.FILE_UPLOAD_ZIMG_ERROR);
        }
        Object obj = savePPTOrPDF(name, resultList, mode);
        return SuccessAndErrorList.builder().successResultList(successResultList).errorResultList(errorResultList).object(obj).build();
    }

    @Override
    public Integer countPPTs(long uid) {
        int count = pptDao.countPptsByUid(uid);
        return count;
    }

    @Override
    public Object savePPTOrPDF(String name, List<String> imgs, Integer mode) {
        BladeUser user = SecureUtil.getUser();
        switch (mode) {
            case PPTCONTROLLER_DO_PPT:
                String pptID = fileService.createPPTWithImgUrl(name, imgs);
                if (!StringUtils.isEmpty(pptID)) {
                    PPT ppt = PPT.builder()
                            .userId(Long.valueOf(user.getUserId())).name(name).datetime(TimeUtil.currentTimeStamp()).cover(imgs.get(0)).fileId(pptID).build();
                    ppt.insert();
                    return ppt;
                }
                break;
            case PPTCONTROLLER_DO_PDF:
                String pdfid = fileService.createPDFWithImgUrl(name, imgs);
                if (!StringUtils.isEmpty(pdfid)) {
                    if (!StringUtils.isEmpty(pdfid)) {
                        Pdf pdf = Pdf.builder()
                                .uid(Long.valueOf(user.getUserId())).name(name).datetime(TimeUtil.currentTimeStamp()).cover(imgs.get(0)).fileId(pdfid).build();
                        pdf.insert();
                        return pdf;
                    }
                }
                break;
            default:
                break;
        }
        throw new ApiException(ApiResultEnum.MODE_ERROR_CREATE_PPT_OR_PDF);
    }
}