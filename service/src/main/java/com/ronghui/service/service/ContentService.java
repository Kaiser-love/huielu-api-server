package com.ronghui.service.service;

import com.ronghui.service.entity.Content;

import java.util.List;

public interface ContentService {
    Content findByName(String name);

    void save(Content content);

    void delete(long cid);

    List<Content> findByType(String type);
}
