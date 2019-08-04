package com.ronghui.service.service;


import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.ronghui.service.common.constant.*;
import com.ronghui.service.common.request.QueryAllBean;
import com.ronghui.service.common.response.ResponseHelper;
import com.ronghui.service.common.response.ResultBean;
import com.ronghui.service.common.util.*;
import com.ronghui.service.entity.Conference;
import com.ronghui.service.jpa.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @program: guns
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-19 15:48
 */
@Service
public class BaseService {
    @Autowired
    private BaseDao baseDao;

    public ResponseEntity<ResultBean> getEntityList(QueryAllBean queryAllBean) throws Exception {
        String result = queryAllBean.getResult(), entityName = queryAllBean.getEntityName();
        if (result == null)
            throw new ApiException(ApiErrorCode.FAILED);
        String tableName = TableConstant.entityNameToTableName.get(entityName);
        Class clazz = Class.forName(String.format(TableConstant.ENTITY_BASE_PACKAGE, entityName));
        List resultList = null;
        Object listSize = 0;
        String query = queryAllBean.getQuery(),
                queryString = queryAllBean.getQueryString(),
                connection = queryAllBean.getConnection(),
                orderBy = StringUtils.isEmpty(queryAllBean.getOrderBy()) ? "id" : queryAllBean.getOrderBy(),
                sortType = StringUtils.isEmpty(queryAllBean.getSortType()) ? "desc" : queryAllBean.getSortType();
        Integer page = queryAllBean.getPage(), count = queryAllBean.getPagecount(), type = queryAllBean.getType();
        // 带条件或查询
        if (query != null && query.contains(" ")) {
            resultList = baseDao.findAllBySql(tableName, "like", query, queryString, page, count, clazz);
        }
        // 查询全部
        if (result.equals(ConditionUtil.QUERY_ALL)) {
            resultList = baseDao.findAll(tableName, clazz);
        }
        // 查询全部分页
        if (result.equals(ConditionUtil.QUERY_ALL_PAGE)) {
            if (Func.isEmpty(type)) {
                listSize = baseDao.findAllSize(tableName);
                resultList = baseDao.findAllByPage(tableName, orderBy, sortType, page, count, clazz);
            } else {
                listSize = baseDao.findAllSize(tableName, StringConstant.STATUS, type);
                resultList = baseDao.findAllByPage(tableName, orderBy, sortType, StringConstant.STATUS, type, page, count, clazz);
            }
        }
        // 带条件查询全部
        if (result.equals(ConditionUtil.QUERY_ATTRIBUTE_ALL)) {
            resultList = baseDao.findAllBySql(tableName, query, connection, queryString, clazz);
        }
        // 带条件查询分页
        if (result.equals(ConditionUtil.QUERY_ATTRIBUTE_PAGE)) {
            if (Func.isEmpty(type)) {
                listSize = baseDao.findAllSizeByCondition(tableName, query, connection, queryString);
                resultList = baseDao.findAllBySql(tableName, connection, query, queryString, orderBy, sortType, page, count, clazz);
            } else {
                listSize = baseDao.findAllSizeByCondition(tableName, query, connection, queryString, StringConstant.STATUS, type);
                resultList = baseDao.findAllBySql(tableName, connection, query, queryString, orderBy, sortType, StringConstant.STATUS, type, page, count, clazz);
            }
        }
        if (resultList == null)
            return ResponseHelper.BadRequest("查询组合出错 函数未执行！");
        return ResponseHelper.OK(resultList, Integer.valueOf(listSize.toString()));

    }

}