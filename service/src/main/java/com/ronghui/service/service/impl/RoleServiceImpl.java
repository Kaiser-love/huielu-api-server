package com.ronghui.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.ronghui.service.common.constant.Constant;
import com.ronghui.service.common.constant.PublicResultConstant;
import com.ronghui.service.common.exception.BusinessException;
import com.ronghui.service.common.util.ComUtil;
import com.ronghui.service.dto.RoleModel;
import com.ronghui.service.entity.*;
import com.ronghui.service.mapper.RoleMapper;
import com.ronghui.service.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {


    @Autowired
    private IRoleToMenuService roleToMenuService;

    @Autowired
    private IUserToRoleService userToRoleService;

    @Autowired
    private IMenuService menuService;

    @Override
    public boolean addRoleAndPermission(RoleModel roleModel) throws Exception {
        Role role = new Role();
        BeanUtils.copyProperties(roleModel, role);
        boolean result = this.getBaseMapper().insert(role) != 0;
        if (!result) {
            throw new BusinessException(PublicResultConstant.UPDATE_ROLEINFO_ERROR);
        }
        result = roleToMenuService.saveAll(role.getRoleCode(), roleModel.getMenuCodes());
        return result;
    }

    @Override
    public boolean updateRoleInfo(RoleModel roleModel) throws Exception {
        if (roleModel.getRoleCode().equals(
                this.getBaseMapper().selectOne(new QueryWrapper<Role>().eq("role_name", Constant.RoleType.SYS_ASMIN_ROLE)).getRoleCode())) {
            throw new BusinessException(PublicResultConstant.UPDATE_SYSADMIN_INFO_ERROR);
        }
        Role role = this.getBaseMapper().selectById(roleModel.getRoleCode());
        if (ComUtil.isEmpty(role)) {
            return false;
        }
        BeanUtils.copyProperties(roleModel, role);
        boolean result = this.updateById(role);
        if (!result) {
            throw new BusinessException(PublicResultConstant.UPDATE_ROLEINFO_ERROR);
        }
        Map<String, Object> paramMap = ImmutableMap.<String, Object>builder().put("role_code", roleModel.getRoleCode()).build();
        result = roleToMenuService.getBaseMapper().deleteByMap(paramMap) != 0;
        if (!result) {
            throw new BusinessException("删除权限信息失败");
        }
        result = roleToMenuService.saveAll(role.getRoleCode(), roleModel.getMenuCodes());
        if (!result) {
            throw new BusinessException("更新权限信息失败");
        }
        return result;

    }

    @Override
    public void getRoleIsAdminByUserNo(String userNo) throws Exception {
        UserToRole userToRole = userToRoleService.selectByUserNo(userNo);
        Role role = this.getBaseMapper().selectById(userToRole.getRoleCode());
        if (role.getRoleName().equals(Constant.RoleType.SYS_ASMIN_ROLE)) {
            throw new BusinessException(PublicResultConstant.UPDATE_SYSADMIN_INFO_ERROR);
        }
    }

    @Override
    public Map<String, Object> selectByRoleCode(String roleCode) throws Exception {
        Role role = this.getBaseMapper().selectById(roleCode);
        if (ComUtil.isEmpty(role)) {
            throw new BusinessException(PublicResultConstant.INVALID_ROLE);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("role", role);
        //权限信息
        result.put("nodes", this.getMenuByRoleCode(role.getRoleCode()));
        return result;
    }

    @Override
    public void deleteByRoleCode(String roleCode) throws Exception {
        if (ComUtil.isEmpty(this.getBaseMapper().selectById(roleCode))) {
            throw new BusinessException("角色不存在");
        }
        if (!ComUtil.isEmpty(userToRoleService.getBaseMapper().selectList(new QueryWrapper<UserToRole>().eq("role_code", roleCode)))) {
            throw new BusinessException("角色存在相关用户,请先删除相关角色的用户");
        }
        Map<String, Object> paramMap = ImmutableMap.<String, Object>builder().put("role_code", roleCode).build();
        this.getBaseMapper().deleteByMap(paramMap);
    }


    @Override
    public Map<String, Object> getMenuByRoleCode(String roleCode) {
        Map<String, Object> retMap = new HashMap<>();
        List<Menu> menuList = menuService.findMenuByRoleCode(roleCode);
        List<Menu> buttonList = new ArrayList<Menu>();
        List<Menu> retMenuList = menuService.treeMenuList(Constant.ROOT_MENU, menuList);
        for (Menu buttonMenu : menuList) {
            if (buttonMenu.getMenuType() == Constant.TYPE_BUTTON) {
                buttonList.add(buttonMenu);
            }
        }
        retMap.put("menuList", retMenuList);
        retMap.put("buttonList", buttonList);
        return retMap;
    }
}
