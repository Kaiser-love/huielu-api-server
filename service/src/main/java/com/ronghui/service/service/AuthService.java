package com.ronghui.service.service;

import com.ronghui.service.common.response.ResultBean;
import com.ronghui.service.dto.WxUser;
import com.ronghui.service.entity.User;
import org.springframework.http.ResponseEntity;

/**
 * @program: AuthService
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-30 22:39
 */
public interface AuthService {

    ResponseEntity<ResultBean> wxLoginByCode(String code);

    ResponseEntity<ResultBean> wxLoginByCodeAndRegistry(String code, WxUser wxUser);

    ResponseEntity<ResultBean> token(String userName, String password);

}
