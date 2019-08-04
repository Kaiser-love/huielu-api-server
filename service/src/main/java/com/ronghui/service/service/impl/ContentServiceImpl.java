package com.ronghui.service.service.impl;

import com.ronghui.service.entity.Content;
import com.ronghui.service.jpa.dao.ContentDao;
import com.ronghui.service.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private ContentDao contentDao;

    @Override
    public Content findByName(String name) {
        return contentDao.findByName(name);
    }

    @Override
    public void save(Content content) {
        contentDao.save(content);
    }

    @Override
    public void delete(long cid) {
        contentDao.deleteById(cid);
    }

    @Override
    public List<Content> findByType(String type) {
        if (StringUtils.isEmpty(type)) {
            return contentDao.findContentsByTypeOrType(Content.ContextType.All, Content.ContextType.All);
        }
        if (type.equals(Content.ContextType.Android.name())) {
            return contentDao.findContentsByTypeOrType(Content.ContextType.Android, Content.ContextType.All);
        } else if (type.equals(Content.ContextType.Web.name())) {
            return contentDao.findContentsByTypeOrType(Content.ContextType.Web, Content.ContextType.All);
        } else if (type.equals(Content.ContextType.WeChat.name())) {
            return contentDao.findContentsByTypeOrType(Content.ContextType.WeChat, Content.ContextType.All);
        } else {
            return contentDao.findContentsByTypeOrType(Content.ContextType.All, Content.ContextType.All);
        }
    }


}
