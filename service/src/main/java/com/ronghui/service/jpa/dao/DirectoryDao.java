package com.ronghui.service.jpa.dao;

import com.ronghui.service.entity.Directory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface DirectoryDao extends JpaRepository<Directory, Long> {
    Directory findByDirid(long dirid);

    List<Directory> findByUserId(long userId);

    Directory findByUserIdAndName(long userId, String name);
}
