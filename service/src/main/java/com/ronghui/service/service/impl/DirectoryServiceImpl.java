package com.ronghui.service.service.impl;

import com.ronghui.service.entity.Directory;
import com.ronghui.service.jpa.dao.DirectoryDao;
import com.ronghui.service.service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectoryServiceImpl implements DirectoryService {
    @Autowired
    private DirectoryDao directoryDao;

    @Override
    public void save(Directory directory) {
        directoryDao.save(directory);
    }

    @Override
    public void update(Directory directory) {
        directoryDao.save(directory);
    }

    @Override
    public void delete(long dirid) {
        directoryDao.deleteById(dirid);
    }

    @Override
    public Directory findById(long dirid) {
        return directoryDao.findByDirid(dirid);
    }

    @Override
    public List<Directory> findByUser(long uid) {
        return directoryDao.findByUserId(uid);
    }

    @Override
    public Directory findByUserAndName(long uid, String name) {
        return directoryDao.findByUserIdAndName(uid, name);
    }
}
