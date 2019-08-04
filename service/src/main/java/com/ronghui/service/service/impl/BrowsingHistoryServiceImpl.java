package com.ronghui.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ronghui.service.entity.BrowsingHistory;
import com.ronghui.service.mapper.BrowsingHistoryMapper;
import com.ronghui.service.service.BrowsingHistoryService;
import org.springframework.stereotype.Service;

/**
 * @program: monitor
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-11 19:39
 */
@Service("BrowsingHistoryService")
public class BrowsingHistoryServiceImpl extends ServiceImpl<BrowsingHistoryMapper, BrowsingHistory> implements BrowsingHistoryService {

}