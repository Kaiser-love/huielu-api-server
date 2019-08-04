package com.ronghui.service.jpa.dao;


import com.ronghui.service.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentDao extends JpaRepository<Content, Long> {
    Content findByCid(int cid);

    Content findByName(String name);


    List<Content> findContentsByTypeOrType(Content.ContextType type1, Content.ContextType type2);
}
