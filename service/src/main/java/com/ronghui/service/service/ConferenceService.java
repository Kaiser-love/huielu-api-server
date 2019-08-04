package com.ronghui.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ronghui.service.common.request.RequestBean;
import com.ronghui.service.common.response.ResultBean;
import com.ronghui.service.entity.Conference;
import org.springframework.http.ResponseEntity;

/**
 * @program: monitor
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-11 19:39
 */
public interface ConferenceService extends IService<Conference> {

    ResponseEntity<ResultBean> getsByTime(Integer timeType, String query, String timeValue, Integer type, Integer page, Integer count);

    ResponseEntity<ResultBean> collectConference(RequestBean requestBean, Integer mode);

}