package com.ronghui.service.jpa.dao;

import com.ronghui.service.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: PPTDao
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-20 03:54
 */
@Component("ConferenceDao")
public interface ConferenceDao extends JpaRepository<Conference, Long> {

}