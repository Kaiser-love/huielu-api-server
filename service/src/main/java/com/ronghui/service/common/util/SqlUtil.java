package com.ronghui.service.common.util;

import com.google.common.collect.ImmutableMap;
import com.ronghui.service.common.constant.StringConstant;
import com.ronghui.service.common.response.ResponseHelper;
import com.ronghui.service.common.response.ResultBean;
import com.ronghui.service.context.SpringContextBeanService;
import com.ronghui.service.entity.Conference;
import com.ronghui.service.jpa.BaseDao;
import lombok.val;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: monitor
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-12 10:18
 */
public class SqlUtil {

    public static Map<String, Object> getSqlMap(String query, String queryString) {
        Map<String, Object> claims = ImmutableMap.<String, Object>builder().put(query, queryString).build();
        return claims;
    }

    public static String getInSqlString(List<Long> ids) {
        StringBuffer sb = new StringBuffer();
        List<String> idStrs = ids.stream().map(String::valueOf).collect(Collectors.toList());
        String str = String.join(",", idStrs);
        return sb.append("(").append(str).append(")").toString();
    }

    public static ResponseEntity<ResultBean> getDataAndSizePage(StringBuffer sql, Integer page, Integer count) {
        BaseDao baseDao = SpringContextBeanService.getBean("BaseDao");
        val sizeSql = StringConstant.COUNT_SQL + sql.toString();
        val size = baseDao.findBySql(sizeSql).get(0);
        sql.append(" limit ").append(page * count).append(",").append(count);
        val dataSql = StringConstant.ALL_SQL + sql.toString();
        val data = baseDao.findBySql(dataSql, Conference.class);
        return ResponseHelper.OK(data, Integer.valueOf(size.toString()));
    }
}