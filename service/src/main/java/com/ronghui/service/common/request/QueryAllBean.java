package com.ronghui.service.common.request;

import lombok.*;

/**
 * QueryAllBean
 *
 * @author dongyang_wu
 * @date 2019/5/13 15:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryAllBean {
    private String query;
    private String connection;
    private String queryString;
    private String orderBy;
    private String sortType;
    private Integer type;
    private Integer page;
    private Integer pagecount;
    private String result;
    private String entityName;
}
