package com.ronghui.service.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ronghui.service.common.constant.ControllerModeConstant;
import com.ronghui.service.common.request.QueryAllBean;
import com.ronghui.service.common.response.*;
import com.ronghui.service.common.util.*;
import com.ronghui.service.dto.BladeUser;
import com.ronghui.service.entity.*;
import com.ronghui.service.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: PPT、PDF模块
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-28 16:07
 */
@RestController
@Api(tags = "文件接口", description = "PPT相关API")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class FileController {
    @Autowired
    private BaseService baseService;
    @Autowired
    private IPPTService pptService;
    @Autowired
    private FileService fileService;

    @GetMapping(value = "/ppt/savePPt")
    @ApiOperation("根据多张zimg的图片路径生成PPT")
    public ResponseEntity<ResultBean> completePic2PPT(@RequestParam(value = "pptName") String pptName, @RequestParam(value = "imgPaths") ArrayList<String> imgs) throws Exception {
        Object ppt = pptService.savePPTOrPDF(pptName, imgs, ControllerModeConstant.PPTCONTROLLER_DO_PPT);
        return ResponseHelper.OK(ppt);
    }

    @GetMapping("/pdf/savePDF")
    @ApiOperation("根据多张zimg的图片路径生成PDF")
    public ResponseEntity<ResultBean> completePic2PDF(@RequestParam(value = "pdfName") String pdfName, @RequestParam(value = "imgPaths") ArrayList<String> imgs) throws Exception {
        Object pdf = pptService.savePPTOrPDF(pdfName, imgs, ControllerModeConstant.PPTCONTROLLER_DO_PDF);
        return ResponseHelper.OK(pdf);
    }

    @GetMapping(value = "/pptOrpdf/download")
    @ApiOperation("根据mongodb的id以指定的名字下载ppt或者pdf,0为PPT，1为PDF")
    public void downLoadPPTOrPDF(@RequestParam String id, @RequestParam Integer mode, @ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response, @RequestParam String name) {
        byte[] file = fileService.findFileById(id);
        try {
            long start = System.currentTimeMillis();
            log.info("----------开始下载文件，文件长度[" + file.length + "]");
            HttpResponseUtil.writeToResponse(request, response, name, mode);
            HttpResponseUtil.exportFile(response, file);
            System.out.println("耗时:[" + (System.currentTimeMillis() - start) + "]ms");
            log.info("----------下载文件完成");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    @GetMapping(value = "/ppt/getPPTList")
    @ApiOperation("获得PPT列表")
    public ResponseEntity<ResultBean> getAllPPT(@RequestParam(required = false) String query, @RequestParam(required = false) String queryString, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer count) throws Exception {
        String result = ConditionUtil.judgeArgument(query, queryString, page, count);
        return baseService.getEntityList(QueryAllBean.builder().query(query).queryString(queryString).page(page).pagecount(count).result(result).entityName("PPT").build());
    }

    @GetMapping(value = "/pdf/getPDFList")
    @ApiOperation("获得PDF列表")
    @ApiIgnore
    public ResponseEntity<ResultBean> getAllPDF(@RequestParam(required = false) String query, @RequestParam(required = false) String queryString, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer count) throws Exception {
        String result = ConditionUtil.judgeArgument(query, queryString, page, count);
        return baseService.getEntityList(QueryAllBean.builder().query(query).queryString(queryString).page(page).pagecount(count).result(result).entityName("PPT").build());
    }


    @ApiOperation(value = "图片批量上传生成PPT或PDF,0为PPT,1为PDF")
    @PostMapping(value = "/pptOrpdf/upload", headers = "content-type=multipart/form-data")
    public ResponseEntity<ResultBean> uploadPicture2PPTOrPDF(@RequestBody List<MultipartFile> files, @RequestParam String name, @RequestParam(required = false) Integer mode) throws Exception {
        SuccessAndErrorList successAndErrorList = pptService.uploadPicture2PPTOrPDF(files, name, mode);
        return ResponseHelper.OK(successAndErrorList);
    }

    @ApiOperation(value = "获取当前用户的ppt")
    @GetMapping("/user/ppts")
    public ResponseEntity<ResultBean> ppts(@RequestParam(defaultValue = "0", required = false) Long page, @RequestParam(defaultValue = "10", required = false) Long pageCount, @RequestParam(required = false) String query, @RequestParam(required = false) String queryString) {
        BladeUser user = SecureUtil.getUser();
        if (!StringUtils.isEmpty(query) && !StringUtils.isEmpty(queryString)) {
            IPage<PPT> list = PPT.builder().build().selectPage(new Page<>(page, pageCount), new QueryWrapper<PPT>().eq("uid", user.getUserId()).like(query, queryString));
            return ResponseHelper.OK(list.getRecords(), (int) list.getTotal());
        }
        IPage<PPT> list = PPT.builder().build().selectPage(new Page<>(page, pageCount), new QueryWrapper<PPT>().eq("uid", user.getUserId()));
        return ResponseHelper.OK(list.getRecords(), (int) list.getTotal());
    }

    @ApiOperation(value = "获取当前用户的pdf")
    @GetMapping("/user/pdfs")
    public ResponseEntity<ResultBean> pdfs(@RequestParam(defaultValue = "0", required = false) Long page, @RequestParam(defaultValue = "10", required = false) Long pageCount, @RequestParam(required = false) String query, @RequestParam(required = false) String queryString) {
        BladeUser user = SecureUtil.getUser();
        if (!StringUtils.isEmpty(query) && !StringUtils.isEmpty(queryString)) {
            IPage<Pdf> list = Pdf.builder().build().selectPage(new Page<>(page, pageCount), new QueryWrapper<Pdf>().eq("uid", user.getUserId()).like(query, queryString));
            return ResponseHelper.OK(list.getRecords(), (int) list.getTotal());
        }
        IPage<Pdf> list = Pdf.builder().build().selectPage(new Page<>(page, pageCount), new QueryWrapper<Pdf>().eq("uid", user.getUserId()));
        return ResponseHelper.OK(list.getRecords(), (int) list.getTotal());
    }

    @PostMapping("/savepdf")
    public ResponseEntity<ResultBean> savepdf(@RequestParam(value = "pptName") String pptname, @RequestParam(value = "imgPaths") ArrayList<String> imgs) {
        String pdfid = fileService.createPDFWithImgUrl(pptname, imgs);
        if (!StringUtils.isEmpty(pdfid))
            return ResponseHelper.OK();
        return ResponseHelper.BadRequest();
    }

    @PostMapping(value = "saveppt")
    public ResponseEntity<ResultBean> saveppt(@RequestParam(value = "pptName") String pptname, @RequestParam(value = "imgPaths") ArrayList<String> imgs) {
        User user = User.builder().build().selectById(SecureUtil.getUserId());
        if (user == null) {
            return ResponseHelper.BadRequest("用户未登录");
        }
        if (StringUtils.isEmpty(pptname)) {
            return ResponseHelper.BadRequest("ppt名字为空");
        }
        if (imgs.size() == 0) {
            log.error("生成PPT的图片列表为空");
            return ResponseHelper.BadRequest("生成PPT的图片列表为空");
        }
        if (!pptname.endsWith(".ppt")) {
            pptname += ".ppt";
        }
        String pptID = fileService.createPPTWithImgUrl(pptname, imgs);
        if (!StringUtils.isEmpty(pptID)) {
            PPT ppt = new PPT();
            // TODO: NEED SEARCH USER IN DATABASE?
            ppt.setUid(user);
            ppt.setName(pptname);
            ppt.setDatetime(new Timestamp(System.currentTimeMillis()));
            ppt.setCover(imgs.get(0));
            ppt.setFileId(pptID);
            pptService.save(ppt);
            return ResponseHelper.OK(pptID);
        } else {
            return ResponseHelper.BadRequest("PPT ID为空");
        }
    }
}