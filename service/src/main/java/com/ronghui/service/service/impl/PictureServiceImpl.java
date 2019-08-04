package com.ronghui.service.service.impl;

import com.ronghui.service.entity.Picture;
import com.ronghui.service.jpa.dao.PictureDao;
import com.ronghui.service.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {
    @Autowired
    private PictureDao pictureDao;

    @Override
    public void save(Picture picture) {
        pictureDao.save(picture);
    }

    @Override
    public void update(Picture picture) {
        pictureDao.save(picture);
    }

    @Override
    public void delete(long picid) {
        pictureDao.deleteById(picid);
    }

    @Override
    @Transactional
    public void deleteList(ArrayList<Long> picList) {
        pictureDao.deletePictureList(picList);
    }

    @Override
    public Picture findById(long picid) {
        return pictureDao.findByPicid(picid);
    }

    @Override
    public List<Picture> findByDirId(long dirid) {
        return pictureDao.findPicturesByDirectory_Dirid(dirid);
    }

    @Override
    public void clearLazyField(List<Picture> pictures) {
        pictures.forEach(picture -> {
            picture.setDirectory(null);
        });
    }
}
