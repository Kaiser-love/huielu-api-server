package com.ronghui.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ronghui.service.common.util.ComUtil;
import com.ronghui.service.entity.UserToRole;
import com.ronghui.service.mapper.UserToRoleMapper;
import com.ronghui.service.service.IUserToRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserToRoleServiceImpl extends ServiceImpl<UserToRoleMapper, UserToRole> implements IUserToRoleService {

    @Override
//    @Cacheable(value = "UserToRole",keyGenerator="wiselyKeyGenerator")
    public UserToRole selectByUserNo(String userNo) {
        List<UserToRole> userToRoleList = this.list(new QueryWrapper<UserToRole>().eq("id", userNo));
        return ComUtil.isEmpty(userToRoleList) ? null : userToRoleList.get(0);
    }
}
