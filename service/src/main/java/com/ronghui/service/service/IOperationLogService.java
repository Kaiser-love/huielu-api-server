package com.ronghui.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ronghui.service.entity.OperationLog;

public interface IOperationLogService extends IService<OperationLog> {
    String getMethodArgs(Object[] args);

    void deleteLogs();
}
