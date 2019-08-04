package com.ronghui.service.controller;

import com.ronghui.service.common.response.ResponseHelper;
import com.ronghui.service.common.response.ResultBean;
import com.ronghui.service.common.util.SecureUtil;
import com.ronghui.service.dto.BladeUser;
import com.ronghui.service.entity.*;
import com.ronghui.service.service.ContentService;
import com.ronghui.service.service.IPPTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;

/**
 * @program: MainController
 * @description:
 * @author: dongyang_wu
 * @create: 2019-06-17 16:14
 */
@RestController
@ApiIgnore
@Slf4j
public class MainController {
    @Autowired
    private ContentService contentService;
    @Autowired
    private IPPTService pptService;

    @GetMapping("/appInit")
    public ResponseEntity<ResultBean> initApp(@RequestParam String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        BladeUser user = SecureUtil.getUser();
        System.out.println(type);
        hashMap.put("login", user != null);
        List<Content> contents = contentService.findByType(type);
        contents.forEach(content -> hashMap.put(content.getName(), content.getData()));
        if (user != null) {
            User dbUser = User.builder().build().selectById(user.getUserId());
            dbUser.setPptCount(pptService.countPPTs(user.getUserId()));
            hashMap.put("user", dbUser);
        }
        return ResponseHelper.OK(hashMap);
    }
}