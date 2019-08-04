package com.ronghui.service.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ronghui.service.annotation.Pass;
import com.ronghui.service.common.exception.ApiException;
import com.ronghui.service.common.exception.ApiResultEnum;
import com.ronghui.service.common.request.RequestBean;
import com.ronghui.service.common.response.ResponseHelper;
import com.ronghui.service.common.response.ResultBean;
import com.ronghui.service.common.util.*;
import com.ronghui.service.dto.WxUser;
import com.ronghui.service.entity.*;
import com.ronghui.service.entity.input.UserPerfectInputVo;
import com.ronghui.service.service.*;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


/**
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-19 13:14
 */
@RestController
@Api(tags = "用户接口", description = "用户API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IPPTService pptService;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private ConferenceCollectionService conferenceCollectionService;

    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/user/currentUser")
    public ResponseEntity<ResultBean> getUser() {
        User user = SecureUtil.getDataBaseUser();
        Integer pptCount = pptService.getBaseMapper().selectCount(new QueryWrapper<PPT>().eq("uid", user.getId()));
        Integer pdfCount = pdfService.getBaseMapper().selectCount(new QueryWrapper<Pdf>().eq("uid", user.getId()));
        user.setPptCount(pptCount);
        user.setPdfCount(pdfCount);
        return ResponseHelper.OK(user);
    }

    @PostMapping("/user/registry")
    @ApiOperation(value = "微信用户注册")
    @Pass
    public ResponseEntity<ResultBean> registry(@RequestBody WxUser wxUser) {
        return userService.userRegistry(wxUser);
    }

    @PostMapping("/user/collectOrBrowsing/conferences")
    @ApiOperation(value = "获取用户收藏或浏览过的会议(默认排序为收藏和浏览时间由近到远) mode0收藏1浏览历史")
    public ResponseEntity<ResultBean> getConferenceCollection(@RequestParam(defaultValue = "0", required = false) Integer page, @RequestParam(defaultValue = "10", required = false) Integer count, @RequestParam(required = false) String query, @RequestParam(required = false) String connection, @RequestParam(required = false) String queryString, @RequestParam Integer mode) {
        Long dataBaseUserId = SecureUtil.getDataBaseUserId();
        return userService.getConferenceCollection(String.valueOf(dataBaseUserId), query, connection, queryString, page, count, mode);
    }


    @PostMapping("/requesthead")
    @ApiOperation(value = "获取用户头像")
    public ResponseEntity<ResultBean> requestHeadByPhone(@RequestParam("phone") String phone) {
        User user = userService.getBaseMapper().selectOne(new QueryWrapper<User>().eq("phone", phone));
        if (user != null) {
            return ResponseHelper.OK(user.getAvatarUrl());
        } else {
            return ResponseHelper.BadRequest("用户头像不存在");
        }
    }

    @PostMapping("/user/perfectInfo")
    @ApiOperation(value = "完善当前用户信息", notes = "传空则置空 不传则不变 传则修改 birthday样例为2019-07-14 20:41:12 account,password慎传")
    public ResponseEntity<ResultBean> requestHeadByPhone(@RequestBody UserPerfectInputVo userPerfectInputVo) {
        User user = new User();
        BeanUtils.copyProperties(userPerfectInputVo, user);
        user.setId(SecureUtil.getDataBaseUserId());
        boolean b = userService.updateById(user);
        return ResponseHelper.BooleanResultBean(userService.getById(user.getId()), "修改失败", b);
    }

    @PostMapping(value = "/getPPTList")
    @ApiIgnore
    public ResponseEntity<ResultBean> getPPTList() {
        long uid = SecureUtil.getUserId();
        List<PPT> pptList = pptService.getBaseMapper().selectList(new QueryWrapper<PPT>().eq("uid", uid));
        return ResponseHelper.OK(pptList);
    }
}