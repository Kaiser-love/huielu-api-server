package com.ronghui.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ronghui.service.entity.OperationLog;
import com.ronghui.service.jpa.dao.OperationLogDao;
import com.ronghui.service.mapper.OperationLogMapper;
import com.ronghui.service.service.IOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {
    @Autowired
    private OperationLogDao operationLogDao;

    @Override
    public String getMethodArgs(Object[] args) {
        StringBuffer sb = new StringBuffer();
        for (Object o : args)
            sb.append(o + " ");
        if (sb.toString().contains("password"))
            return "***********";
        return StringUtils.isEmpty(sb) ? "无参数" : sb.toString();
    }

    @Override
    @Async
    public void deleteLogs() {
        if (!CollectionUtils.isEmpty(operationLogDao.findAll())) {
            List<Long> ids = operationLogDao.findTopRecords(50 - 1);
            this.remove(new QueryWrapper<OperationLog>().notIn("id", ids));
        }
    }
}
