package com.ronghui.service.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ronghui.service.entity.UserToRole;

public interface IUserToRoleService extends IService<UserToRole> {

    /**
     * 根据用户ID查询人员角色
     * @param userNo 用户ID
     * @return  结果
     */
    UserToRole selectByUserNo(String userNo);

}
