package com.ronghui.service.jpa.dao;

import com.ronghui.service.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperationLogDao extends JpaRepository<OperationLog, Long> {
    @Query(value = "SELECT id FROM operation_log ORDER BY create_time DESC LIMIT ?1", nativeQuery = true)
    List<Long> findTopRecords(Integer logNumber);
}
