package com.ronghui.service.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ronghui.service.annotation.Log;
import com.ronghui.service.common.exception.ApiException;
import com.ronghui.service.common.exception.ApiResultEnum;
import com.ronghui.service.common.request.QueryAllBean;
import com.ronghui.service.common.request.RequestBean;
import com.ronghui.service.common.response.ResponseHelper;
import com.ronghui.service.common.response.ResultBean;
import com.ronghui.service.common.util.*;
import com.ronghui.service.entity.BrowsingHistory;
import com.ronghui.service.entity.Conference;
import com.ronghui.service.service.*;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


/**
 * 认证模块
 *
 * @author dongyang_wu
 */
@RestController
@AllArgsConstructor
@Api(tags = "会议接口", description = "获取会议信息")
public class ConferenceController {
    @Autowired
    private BaseService baseService;
    @Autowired
    private ConferenceService conferenceService;
    @Autowired
    private BrowsingHistoryService browsingHistoryService;

    @GetMapping("/conferences")
    @ApiOperation(value = "获取会议列表", notes = "connection:(=,like),query:键,queryString:值,page:页数,count:条数,type:会议类型(-1未开始0已开始1已结束) 响应code:总数")
    @Log("获取会议列表")
    public ResponseEntity<ResultBean> gets(@RequestParam(required = false) String query, @RequestParam(required = false) String connection, @RequestParam(required = false) String queryString, @RequestParam(required = false) String orderBy, @RequestParam(required = false) String sortType, @RequestParam Integer page, @RequestParam Integer count, @RequestParam Integer type) throws Exception {
        String result = ConditionUtil.judgeArgument(query, queryString, page, count);
        return baseService.getEntityList(QueryAllBean.builder().query(query).connection(connection).queryString(queryString).orderBy(orderBy).sortType(sortType).type(type).page(page).pagecount(count).result(result).entityName("Conference").build());
    }

    @GetMapping("/conferencesByTime")
    @ApiOperation(value = "获取会议列表", notes = "tiemType,query:筛选时间字段(start_time,end_time,start_end),type:会议类型(-1未开始0已开始1已结束) 响应code:总数")
    @Log("获取会议列表")
    public ResponseEntity<ResultBean> getsByTime(@RequestParam Integer timeType, @RequestParam String query, @RequestParam String timeValue, @RequestParam Integer type, @RequestParam Integer page, @RequestParam Integer count) {
        if (Func.hasEmpty(timeType, timeValue, type, page, count))
            throw new ApiException(ApiResultEnum.ARGS_ERROR);
        return conferenceService.getsByTime(timeType, query, timeValue, type, page, count);
    }

    @PostMapping("/conference")
    @ApiOperation(value = "获取具体属性的会议信息(调用时会使对应的会议点击次数+1)", notes = "query:键,queryString:值")
    @Log("获取具体属性的会议信息(调用时会使对应的会议点击次数+1)")
    public ResponseEntity<ResultBean> get(@RequestBody RequestBean requestBean) {
        Map<String, Object> claims = SqlUtil.getSqlMap(requestBean.getItems().get(0).getQuery(), requestBean.getItems().get(0).getQueryString());
        List<Conference> result = conferenceService.getBaseMapper().selectByMap(claims);
        result.forEach(item -> {
            item.setClicksNumber(item.getClicksNumber() == null ? 1 : item.getClicksNumber() + 1);
            conferenceService.getBaseMapper().updateById(item);
            Long userId = SecureUtil.getDataBaseUserId();
            Long conferenceId = item.getId();
            Timestamp currentTimeStamp = TimeUtil.currentTimeStamp();
            BrowsingHistory browsingHistory = browsingHistoryService.getBaseMapper().selectOne(new QueryWrapper<BrowsingHistory>().eq("user_id", userId).eq("conference_id", conferenceId));
            if (browsingHistory != null) {
                browsingHistory.setBrowsingTime(currentTimeStamp);
                browsingHistory.updateById();
            } else {
                BrowsingHistory.builder().userId(userId).conferenceId(conferenceId).browsingTime(currentTimeStamp).build().insert();
            }
        });
        return ResponseHelper.OK(result);
    }

    @PostMapping("/conference/collect")
    @ApiOperation(value = "收藏或取消收藏指定会议", notes = "query:键,queryString:值,mode0取消1收藏")
    @Log("收藏指定会议")
    public ResponseEntity<ResultBean> collect(@RequestBody RequestBean requestBean, @RequestParam Integer mode) {
        return conferenceService.collectConference(requestBean, mode);
    }
}
