package com.ronghui.service.service;

import com.ronghui.service.entity.Directory;

import java.util.List;

public interface DirectoryService {
    void save(Directory directory);

    void update(Directory directory);

    void delete(long dirid);

    Directory findById(long dirid);

    List<Directory> findByUser(long uid);

    Directory findByUserAndName(long uid, String name);

}
