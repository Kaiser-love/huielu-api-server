package com.ronghui.service.controller;

import com.ronghui.service.common.response.ResponseHelper;
import com.ronghui.service.common.response.ResultBean;
import com.ronghui.service.common.util.SecureUtil;
import com.ronghui.service.entity.*;
import com.ronghui.service.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

@RestController
@ApiIgnore
@Slf4j
public class PictureController {
    @Autowired
    private DirectoryService directoryService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private IUserService userService;
    /**
     * 获取图片列表
     *
     * @param dirid
     * @return
     */
    @PostMapping(value = "/getpicturelist")
    public ResponseEntity<ResultBean> getPictureList(@RequestParam("dirid") long dirid) {
        List<Picture> pictures = pictureService.findByDirId(dirid);
        pictureService.clearLazyField(pictures);
        pictures.forEach(picture -> picture.setPath(picture.getPath()));
        System.out.println(pictures);
        return ResponseHelper.OK(pictures);
    }

    /**
     * 上传图片
     *
     * @param dirid
     * @param path
     * @param session
     * @return
     */
    @PostMapping(value = "/uploadpicture")
    public ResponseEntity<ResultBean> uploadPicture(@RequestParam("dirid") long dirid, @RequestParam("path") String path) {
        Directory dir = directoryService.findById(dirid);
        Picture p = new Picture();
        p.setDirectory(dir);
        p.setPath(path);
        pictureService.save(p);
        return ResponseHelper.OK();
    }

    /*
     * 上传图片，如果相册不存在则创建相册
     * TODO 应该是按照id传图片
     * TEST
     */
    @PostMapping(value = "/uploadpictureByName")
    public ResponseEntity<ResultBean> uploadPicture(@RequestParam("path") String path,
                                                    @RequestParam("dirName") String dirName) {
        long uid = SecureUtil.getUserId();
        // TODO: NEED SEARCH USER IN DATABASE?
        User dbUser = userService.getById(uid);
        Directory directory = directoryService.findByUserAndName(uid, dirName);
        if (directory == null) {
            directory = new Directory();
            directory.setName(dirName);
            directory.setUser(dbUser);
            directoryService.save(directory);
        }
        Picture picture = new Picture();
        picture.setPath(path);
        picture.setDirectory(directory);
        pictureService.save(picture);
        return ResponseHelper.OK();
    }

    /*
     * 更新图片目录
     * TEST
     */
    @PostMapping(value = "/updatepicture")
    public ResponseEntity<ResultBean> updatePicture(Picture picture) {
        pictureService.update(picture);
        return ResponseHelper.OK();
    }

    /*
     * 删除图片
     * TEST
     */
    @PostMapping(value = "/deletepicture")
    public ResponseEntity<ResultBean> deletePicture(@RequestParam("picid") Integer picid) {
        pictureService.delete(picid);
        return ResponseHelper.OK();
    }

    /**
     * 批量删除
     *
     * @param picList
     * @return
     */
    @PostMapping(value = "/deletepicturelist")
    public ResponseEntity<ResultBean> deletePictureList(@RequestParam("piclist") ArrayList<Long> picList) {
        if (picList.size() > 0)
            pictureService.deleteList(picList);
        return ResponseHelper.OK();
    }
}
