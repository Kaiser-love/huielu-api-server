package com.ronghui.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ronghui.service.common.request.RequestBean;
import com.ronghui.service.common.response.ResultBean;
import com.ronghui.service.dto.WxUser;
import com.ronghui.service.entity.User;
import org.springframework.http.ResponseEntity;

public interface IUserService extends IService<User> {
    ResponseEntity<ResultBean> userRegistry(WxUser wxUser);

    ResponseEntity<ResultBean> getConferenceCollection(String userId, String query, String connection, String queryString, Integer page, Integer count, Integer mode);
}
