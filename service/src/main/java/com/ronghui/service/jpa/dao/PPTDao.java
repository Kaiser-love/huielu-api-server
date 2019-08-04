package com.ronghui.service.jpa.dao;

import com.ronghui.service.entity.PPT;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @program: PPTDao
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-20 03:54
 */
public interface PPTDao extends JpaRepository<PPT, Long> {

    Integer countPptsByUid(long uid);

}