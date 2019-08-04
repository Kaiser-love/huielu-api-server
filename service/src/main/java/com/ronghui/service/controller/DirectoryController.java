package com.ronghui.service.controller;

import com.ronghui.service.common.response.ResponseHelper;
import com.ronghui.service.common.response.ResultBean;
import com.ronghui.service.common.util.SecureUtil;
import com.ronghui.service.entity.*;
import com.ronghui.service.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

@RestController
@ApiIgnore
@Slf4j
public class DirectoryController {
    @Autowired
    private IUserService userService;
    @Autowired
    private DirectoryService directoryService;
    @Autowired
    private PictureService pictureService;


    /*
     * 获取用户目录列表
     * TEST
     */
    @PostMapping("/getdirectorylist")
    @ApiOperation(value = "获取用户目录列表")
    public ResponseEntity<ResultBean> getDirectoryList() {
        long uid = SecureUtil.getUserId();
        System.out.println(uid);
        // TODO: NEED SEARCH USER IN DATABASE?
        List<Directory> directories = directoryService.findByUser(uid);
        ArrayList<DirectoryResult> directoryResults = new ArrayList<>();
        for (Directory dir : directories) {
            DirectoryResult dr = new DirectoryResult();
            List<Picture> pictures = pictureService.findByDirId(dir.getDirid());
            dr.setDirid(dir.getDirid());
            dr.setName(dir.getName());
            dr.setPicPath(null);
            dr.setPicCount(pictures.size());
            if (dr.getPicCount() != 0)
                dr.setPicPath(pictures.iterator().next().getPath());
            directoryResults.add(dr);
        }
        System.out.println(directoryResults);
        return ResponseHelper.OK(directoryResults);
    }

    /*
     * 创建目录
     * TEST
     */
    @PostMapping("/createdirectory")
    @ApiOperation(value = "创建目录")
    public ResponseEntity<ResultBean> createDirectory(Directory directory) {
        if (directory.getName() == null || StringUtils.isEmpty(directory.getName())) {
            log.debug("相册名称不能为空");
            return ResponseHelper.BadRequest("相册名称不能为空");
        }
        long uid = SecureUtil.getUserId();
        // TODO: NEED SEARCH USER IN DATABASE?
        User dbUser = userService.getById(uid);
        directory.setUser(dbUser);
        directoryService.save(directory);
        return ResponseHelper.OK();
    }

    /*
     * 通过相册名称查找id
     * TEST
     */
    @PostMapping("/getDirectoryByName")
    @ApiOperation(value = "通过相册名称查找id")
    public ResponseEntity<ResultBean> getDirectoryByName(@RequestParam("name") String dirName) {
        long uid = SecureUtil.getUserId();
        Directory directory = directoryService.findByUserAndName(uid, dirName);
        return ResponseHelper.OK(directory);
    }

    /*
     * 更新目录
     * TEST
     */
    @PostMapping("/updatedirectory")
    @ApiOperation(value = "更新目录")
    public ResponseEntity<ResultBean> updateDirectory(Directory directory) {
        directoryService.update(directory);
        return ResponseHelper.OK();
    }

    /*
     * 删除目录
     * TEST
     */
    @PostMapping("/deletedirectory")
    @ApiOperation(value = "删除目录")
    public ResponseEntity<ResultBean> deleteDirectory(@RequestParam("dirid") long dirid) {
        directoryService.delete(dirid);
        return ResponseHelper.OK();
    }
}
