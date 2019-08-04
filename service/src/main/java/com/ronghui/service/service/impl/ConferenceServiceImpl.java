package com.ronghui.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ronghui.service.common.constant.StringConstant;
import com.ronghui.service.common.request.RequestBean;
import com.ronghui.service.common.response.ResponseHelper;
import com.ronghui.service.common.response.ResultBean;
import com.ronghui.service.common.util.*;
import com.ronghui.service.entity.Conference;
import com.ronghui.service.entity.ConferenceCollection;
import com.ronghui.service.jpa.BaseDao;
import com.ronghui.service.mapper.ConferenceMapper;
import com.ronghui.service.service.ConferenceCollectionService;
import com.ronghui.service.service.ConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: monitor
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-11 19:40
 */
@Service("ConferenceService")
public class ConferenceServiceImpl extends ServiceImpl<ConferenceMapper, Conference> implements ConferenceService {
    @Autowired
    private BaseDao baseDao;
    @Autowired
    private ConferenceCollectionService conferenceCollectionService;

    @Override
    public ResponseEntity<ResultBean> getsByTime(Integer timeType, String query, String timeValue, Integer type, Integer page, Integer count) {
        StringBuffer sql = new StringBuffer();
        String[] split = timeValue.split(" ");
        switch (timeType) {
            // 查找指定日期的数据(timeValue为%Y-%m-%d代表年月日,) 查找本年 本年本月 本年本月本日
            case 0:
                sql.append("FROM conference WHERE DATE_FORMAT(")
                        .append(query).append(",").append("\'" + timeValue + "\'").append(") = DATE_FORMAT(SYSDATE(),").append("\'" + timeValue + "\'")
                        .append(") and ").append(StringConstant.STATUS).append(" = ").append(type);
                break;
            // 查找指定日期上午、下午或晚上的数据（timeValue为0 %Y-%m 2019-08） 2 %Y-%m 2019-07
            case 1:
                // 查找指定日期的数据(timeValue为%Y-%m-%d代表年月日,) 查找本年 本年本月 本年本月本日
                switch (split[0]) {
                    case "0":
                        sql.append("FROM conference WHERE DATE_FORMAT(")
                                .append(query).append(",").append("\'" + "%H:%m:%s" + "\'")
                                .append(")<=").append("\'" + "12:00:00" + "\' and DATE_FORMAT(")
                                .append(query).append(",").append("\'" + split[1] + "\'").append(") = ")
                                .append("\'" + split[2] + "\'")
                                .append(" and ").append(StringConstant.STATUS).append(" = ").append(type);
                        break;
                    case "1":
                        sql.append("FROM conference WHERE DATE_FORMAT(")
                                .append(query).append(",").append("\'" + "%H:%m:%s" + "\'")
                                .append(")>").append("\'" + "12:00:00" + "\'")
                                .append(" and DATE_FORMAT(")
                                .append(query).append(",").append("\'" + "%H:%m:%s" + "\'")
                                .append(")<=").append("\'" + "18:00:00" + "\' and DATE_FORMAT(")
                                .append(query).append(",").append("\'" + split[1] + "\'").append(") = ")
                                .append("\'" + split[2] + "\'")
                                .append(" and ").append(StringConstant.STATUS).append(" = ").append(type);
                        break;
                    case "2":
                        sql.append("FROM conference WHERE DATE_FORMAT(")
                                .append(query).append(",").append("\'" + "%H:%m:%s" + "\'")
                                .append(")>").append("\'" + "18:00:00" + "\' and DATE_FORMAT(")
                                .append(query).append(",").append("\'" + split[1] + "\'").append(") = ")
                                .append("\'" + split[2] + "\'")
                                .append(" and ").append(StringConstant.STATUS).append(" = ").append(type);
                        break;
                    default:
                        break;
                }
                break;
            // timeValue为具体天数 代表近几天的数据
            case 2:
                sql.append("FROM conference WHERE DATE_SUB(")
                        .append(query).append(", INTERVAL ").append(timeValue).append(" DAY) <= SYSDATE() AND TO_DAYS(")
                        .append(query).append(")  >= TO_DAYS(NOW()) ")
                        .append(" and ").append(StringConstant.STATUS).append(" = ").append(type);
                break;
            // %Y-%m-%d-%H:%i:%s 2019-10-02-18:00:00 %Y-%m-%d-%H:%i:%s 2019-11-02-18:00:00
            case 3:
                sql.append("FROM conference WHERE DATE_FORMAT(")
                        .append("start_time").append(",").append("\'" + split[0] + "\'")
                        .append(")>").append("\'" + split[1] + "\'")
                        .append(" and ")
                        .append("DATE_FORMAT(")
                        .append("end_time").append(",").append("\'" + split[2] + "\'")
                        .append(")<").append("\'" + split[3] + "\'")
                        .append(" and ").append(StringConstant.STATUS).append(" = ").append(type);
                break;
            default:
                break;
        }
        return SqlUtil.getDataAndSizePage(sql, page, count);
    }

    @Override
    public ResponseEntity<ResultBean> collectConference(RequestBean requestBean, Integer mode) {
        Map<String, Object> claims = SqlUtil.getSqlMap(requestBean.getItems().get(0).getQuery(), requestBean.getItems().get(0).getQueryString());
        List<Conference> result = getBaseMapper().selectByMap(claims);
        switch (mode) {
            case 0: {
                int delete;
                try {
                    delete = conferenceCollectionService.getBaseMapper().delete(new QueryWrapper<ConferenceCollection>().eq("user_id", SecureUtil.getDataBaseUserId()).eq("conference_id", result.get(0).getId()));
                } catch (Exception e) {
                    return ResponseHelper.BadRequest("取消收藏会议失败");
                }
                return ResponseHelper.OK("取消收藏会议成功", "取消收藏会议失败(用户未收藏此会议)", delete == 1);

            }
            case 1: {
                boolean insert;
                try {
                    insert = ConferenceCollection.builder().userId(SecureUtil.getDataBaseUserId()).conferenceId(result.get(0).getId()).collectTime(TimeUtil.currentTimeStamp()).build().insert();
                } catch (Exception e) {
                    return ResponseHelper.BadRequest("会议不可重复收藏");
                }
                return ResponseHelper.OK("收藏会议成功", "收藏会议失败", insert);
            }
            default:
                return ResponseHelper.BadRequest();
        }
    }
}