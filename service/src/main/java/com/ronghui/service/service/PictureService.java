package com.ronghui.service.service;

import com.ronghui.service.entity.Picture;

import java.util.ArrayList;
import java.util.List;

public interface PictureService {
    void save(Picture picture);

    void update(Picture picture);

    void delete(long picid);

    void deleteList(ArrayList<Long> picList);

    Picture findById(long picid);

    List<Picture> findByDirId(long dirid);

    void clearLazyField(List<Picture> pictures);
}
