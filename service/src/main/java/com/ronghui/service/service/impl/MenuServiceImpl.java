package com.ronghui.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ronghui.service.common.constant.Constant;
import com.ronghui.service.entity.Menu;
import com.ronghui.service.mapper.MenuMapper;
import com.ronghui.service.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    //redis方法级别的缓存，需要做缓存打开改注解即可
    @Cacheable(value = "UserToRole", keyGenerator = "wiselyKeyGenerator")
    public List<Menu> selectByIds(List<Integer> permissionIds) {
        return this.getBaseMapper().selectList(new QueryWrapper<Menu>().in("menu_id", permissionIds));
    }

    @Override
//    @Cacheable(value = "UserToRole",keyGenerator="wiselyKeyGenerator")
    public List<Menu> findMenuByRoleCode(String roleId) {
        return menuMapper.findMenuByRoleCode(roleId);
    }

    @Override
    public List<Menu> treeMenuList(String pId, List<Menu> list) {
        List<Menu> IteratorMenuList = new ArrayList<>();
        for (Menu m : list) {
            if (String.valueOf(m.getParentId()).equals(pId)) {
                List<Menu> childMenuList = treeMenuList(String.valueOf(m.getMenuId()), list);
                m.setChildMenu(childMenuList);
                if (m.getMenuType() == Constant.TYPE_MENU) {
                    IteratorMenuList.add(m);
                }
            }
        }
        return IteratorMenuList;
    }


}
