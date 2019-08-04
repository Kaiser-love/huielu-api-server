package com.ronghui.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ronghui.service.common.base.BaseServiceImpl;
import com.ronghui.service.common.constant.StringConstant;
import com.ronghui.service.common.constant.TableConstant;
import com.ronghui.service.common.response.ResponseHelper;
import com.ronghui.service.common.response.ResultBean;
import com.ronghui.service.common.util.*;
import com.ronghui.service.common.util.http.HttpHelper;
import com.ronghui.service.dto.WxUser;
import com.ronghui.service.entity.*;
import com.ronghui.service.jpa.BaseDao;
import com.ronghui.service.mapper.UserMapper;
import com.ronghui.service.service.*;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @program: UserServiceImpl
 * @description:
 * @author: dongyang_wu
 * @create: 2019-06-01 23:24
 */
@Service("UserService")
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private HttpHelper httpHelper;
    @Autowired
    private ConferenceCollectionService conferenceCollectionService;
    @Autowired
    private BaseDao baseDao;
    @Autowired
    private ConferenceService conferenceService;
    @Autowired
    private BrowsingHistoryService browsingHistoryService;

    @Override
    public ResponseEntity<ResultBean> userRegistry(WxUser wxUser) {
        User user = null;
        if (Func.hasEmpty(wxUser.getEncryptedData(), wxUser.getIv()) && !StringUtils.isEmpty(wxUser.getNickName())) {
            user = User.builder().build();
            if (StringUtils.isEmpty(wxUser.getOpenid())) {
                ResponseEntity responseEntity = httpHelper.wxLogin(wxUser.getCode());
                JSONObject apiResult = JSON.parseObject(responseEntity.getBody().toString());
                String openid = (String) apiResult.getOrDefault("openid", "");
                wxUser.setOpenid(openid);
            }
            BeanUtils.copyProperties(wxUser, user);
            save(user);
        } else {
            ResponseEntity responseEntity = httpHelper.wxLogin(wxUser.getCode());
            JSONObject apiResult = JSON.parseObject(responseEntity.getBody().toString());
            String session_key = (String) apiResult.getOrDefault("session_key", "");
            String openid = (String) apiResult.getOrDefault("openid", "");
            if (!StringUtils.isEmpty(session_key)) {
                apiResult = JSON.parseObject(WXUtils.getUserInfo(wxUser.getEncryptedData(), session_key, wxUser.getIv()));
                System.out.println(apiResult);
                System.out.println(apiResult.get("phoneNumber"));
                String province = apiResult.get("province").toString();
                String nickName = apiResult.get("nickName").toString();
                String gender = apiResult.get("gender").toString();
                String city = apiResult.get("city").toString();
                String country = apiResult.get("country").toString();
                String avatarUrl = apiResult.get("avatarUrl").toString();
                String appid = JSON.parseObject(apiResult.get("watermark").toString()).get("appid").toString();
                if (!StringUtils.isEmpty(nickName)) {
                    user = User.builder().phone(apiResult.get("phoneNumber") != null ? apiResult.get("phoneNumber").toString() : null).openid(openid).province(province).nickName(nickName).gender(Byte.valueOf(gender)).city(city).country(country).avatarUrl(avatarUrl).appid(appid).build();
                    save(user);
                }
            }
        }
        return ResponseHelper.BooleanResultBean(user, "存储微信新用户失败", user != null);
    }

    @Override
    public ResponseEntity<ResultBean> getConferenceCollection(String userId, String query, String connection, String queryString, Integer page, Integer count, Integer mode) {
        List<Long> conferenceIds;
        if (mode == 0) {
            List<ConferenceCollection> conferenceCollections = conferenceCollectionService.getBaseMapper().selectList(new QueryWrapper<ConferenceCollection>().eq("user_id", SecureUtil.getDataBaseUserId()).orderByDesc("collect_time"));
            conferenceIds = conferenceCollections.stream().map(ConferenceCollection::getConferenceId).collect(Collectors.toList());
        } else {
            List<BrowsingHistory> browsingHistories = browsingHistoryService.getBaseMapper().selectList(new QueryWrapper<BrowsingHistory>().eq("user_id", SecureUtil.getDataBaseUserId()).orderByDesc("browsing_time"));
            conferenceIds = browsingHistories.stream().map(BrowsingHistory::getConferenceId).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(conferenceIds))
            return ResponseHelper.OK(Collections.emptyList(), 0);
        String inSqlString = SqlUtil.getInSqlString(conferenceIds);
        StringBuffer sb = new StringBuffer("from ").append(TableConstant.TABLE_CONFERENCE).append(" where id in ").append(inSqlString);
        boolean isDimQuery = false;
        boolean isLikeQuery = false;
        if (!Func.hasEmpty(query, queryString)) {
            isDimQuery = true;
            if (StringConstant.LIKE.equalsIgnoreCase(connection)) {
                isLikeQuery = true;
                queryString = "%" + queryString + "%";
            }
            sb.append(" and ").append(query + " ").append(connection).append(" \'" + queryString + "\'");
        }
        val sizeSql = StringConstant.COUNT_SQL + sb.toString();
        val size = baseDao.findBySql(sizeSql).get(0);
        List<Conference> data = new ArrayList<>();
        // 条数小于需要的数量且不需要模糊查询  全部返回
        if (conferenceIds.size() < count && !isDimQuery) {
            conferenceIds.forEach(c -> data.add(conferenceService.getById(c)));
        }
        // 条数小于需要的数量且需要模糊查询
        else if (conferenceIds.size() < count) {
            String finalQueryString = queryString;
            boolean finalIsLikeQuery = isLikeQuery;
            conferenceIds.forEach(c -> {
                Conference itemConference;
                if (finalIsLikeQuery)
                    itemConference = conferenceService.getBaseMapper().selectOne(new QueryWrapper<Conference>().eq("id", c).like(query, finalQueryString));
                else
                    itemConference = conferenceService.getBaseMapper().selectOne(new QueryWrapper<Conference>().eq("id", c).eq(query, finalQueryString));
                if (itemConference != null)
                    data.add(itemConference);
            });
        }
        // 条数大于需要的数量且不需要模糊查询
        else if (!isDimQuery) {
            for (int i = page * count; i < page * count + count; i++) {
                if (i >= conferenceIds.size())
                    break;
                data.add(conferenceService.getById(conferenceIds.get(i)));
            }
        }
        // 条数大于需要的数量且需要模糊查询
        else {
            for (int i = page * count; i < conferenceIds.size(); i++) {
                // 直到找到count数量需要的数据
                if (data.size() == count)
                    break;
                Conference itemConference;
                String finalQueryString = queryString;
                boolean finalIsLikeQuery = isLikeQuery;
                if (finalIsLikeQuery)
                    itemConference = conferenceService.getBaseMapper().selectOne(new QueryWrapper<Conference>().eq("id", conferenceIds.get(i)).like(query, finalQueryString));
                else
                    itemConference = conferenceService.getBaseMapper().selectOne(new QueryWrapper<Conference>().eq("id", conferenceIds.get(i)).eq(query, finalQueryString));
                if (itemConference != null)
                    data.add(itemConference);
            }
        }
        return ResponseHelper.OK(data, Integer.valueOf(size.toString()));
    }
}