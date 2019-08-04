package com.ronghui.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.ronghui.service.common.util.ComUtil;
import com.ronghui.service.entity.RoleToMenu;
import com.ronghui.service.mapper.RoleToMenuMapper;
import com.ronghui.service.service.IRoleToMenuService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleToMenuServiceImpl extends ServiceImpl<RoleToMenuMapper, RoleToMenu> implements IRoleToMenuService {

    @Override
    //redis生成key注解，以类名方法名和参数组成key
//    @Cacheable(value = "UserToRole",keyGenerator="wiselyKeyGenerator")
    public List<RoleToMenu> selectByRoleCode(String roleCode) {
        return this.getBaseMapper().selectList(new QueryWrapper<RoleToMenu>().eq("role_code", roleCode));
    }

    @Override
    public boolean saveAll(String roleCode, List<String> menuCodes) {
        boolean result = true;
        if (!ComUtil.isEmpty(menuCodes)) {
            List<RoleToMenu> modelList = new ArrayList<>();
            for (String menuCode : menuCodes) {
                modelList.add(RoleToMenu.builder().roleCode(roleCode).menuCode(menuCode).build());
            }
            result = this.saveBatch(modelList);
        }
        return result;
    }

    @Override
    public boolean deleteAllByRoleCode(String roleCode) {
        Map<String, Object> paramMap = ImmutableMap.<String, Object>builder().put("role_code", roleCode).build();
        return this.getBaseMapper().deleteByMap(paramMap) != 0;
    }
}
