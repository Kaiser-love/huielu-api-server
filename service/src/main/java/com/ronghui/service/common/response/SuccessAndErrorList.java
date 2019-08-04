package com.ronghui.service.common.response;

import lombok.*;

import java.util.List;

/**
 * SuccessAndErrorList
 *
 * @author dongyang_wu
 * @date 2019/5/21 10:57
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SuccessAndErrorList {
    private List<String> successResultList;
    private List<String> errorResultList;
    private Object object;
}
