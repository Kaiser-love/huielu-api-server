package com.ronghui.service.controller;

import com.alibaba.fastjson.JSON;
import com.ronghui.service.annotation.BeforeLog;
import com.ronghui.service.common.response.ResponseHelper;
import com.ronghui.service.common.response.ResultBean;
import com.ronghui.service.common.util.Func;
import com.ronghui.service.dto.WxUser;
import com.ronghui.service.service.AuthService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证模块
 *
 * @author dongyang_wu
 */
@RestController
@AllArgsConstructor
@Api(tags = "授权接口", description = "获取用户token")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/auth/token")
    @ApiOperation(value = "获取认证token", notes = "账号:account,密码:password")
    @BeforeLog("管理员账号登录")
    public ResponseEntity<ResultBean> token(@ApiParam(value = "账号", required = true) @RequestParam String account, @ApiParam(value = "密码", required = true) @RequestParam String password) {
        if (Func.hasEmpty(account, password)) {
            return ResponseHelper.BadRequest("接口调用不合法");
        }
        return authService.token(account, password);
    }

    @PostMapping("/auth/checkUser")
    @ApiOperation(value = "微信小程序用户登录", notes = "loginCode:判断用户是否已经注册使用的code,registryCode:用户未注册时会自动注册使用的code,encryptedData,iv:注册用的数据")
    @BeforeLog("小程序登录")
    public ResponseEntity<ResultBean> wxLoginByCode(@RequestBody String jsonParam) {
        Map<String, String> params = JSON.parseObject(jsonParam, Map.class);
        String loginCode = params.get("loginCode");
        String registryCode = params.getOrDefault("registryCode", "");
        String encryptedData = params.getOrDefault("encryptedData", "");
        String iv = params.getOrDefault("iv", "");
        if (StringUtils.isEmpty(registryCode) && StringUtils.isEmpty(encryptedData) && StringUtils.isEmpty(iv))
            return authService.wxLoginByCode(loginCode);
        else {
            WxUser wxUser = WxUser.builder().code(registryCode).encryptedData(encryptedData).iv(iv).build();
            return authService.wxLoginByCodeAndRegistry(loginCode, wxUser);
        }
    }
}
