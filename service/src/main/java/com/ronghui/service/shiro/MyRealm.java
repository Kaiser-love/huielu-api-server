package com.ronghui.service.shiro;

import com.ronghui.service.common.constant.Constant;
import com.ronghui.service.common.exception.UnauthorizedException;
import com.ronghui.service.common.util.*;
import com.ronghui.service.context.SpringContextBeanService;
import com.ronghui.service.entity.*;
import com.ronghui.service.service.*;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.*;

public class MyRealm extends AuthorizingRealm {
    private IUserService userService;
    private IUserToRoleService userToRoleService;
    private IMenuService menuService;
    private IRoleService roleService;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (userToRoleService == null) {
            this.userToRoleService = SpringContextBeanService.getBean(IUserToRoleService.class);
        }
        if (menuService == null) {
            this.menuService = SpringContextBeanService.getBean(IMenuService.class);
        }
        if (roleService == null) {
            this.roleService = SpringContextBeanService.getBean(IRoleService.class);
        }

        String userNo = SecureUtil.getUserId(principals.toString()).toString();
        User user = userService.getById(userNo);
        UserToRole userToRole = userToRoleService.selectByUserNo(String.valueOf(user.getId()));

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        /*
        Role role = roleService.selectOne(new EntityWrapper<Role>().eq("role_code", userToRole.getRoleCode()));
        //添加控制角色级别的权限
        Set<String> roleNameSet = new HashSet<>();
        roleNameSet.add(role.getRoleName());
        simpleAuthorizationInfo.addRoles(roleNameSet);
        */
        //控制菜单级别按钮  类中用@RequiresPermissions("user:list") 对应数据库中code字段来控制controller
        ArrayList<String> pers = new ArrayList<>();
        List<Menu> menuList = menuService.findMenuByRoleCode(userToRole.getRoleCode());
        for (Menu per : menuList) {
            if (!ComUtil.isEmpty(per.getCode())) {
                pers.add(String.valueOf(per.getCode()));
            }
        }
        Set<String> permission = new HashSet<>(pers);
        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws UnauthorizedException {
        if (userService == null) {
            this.userService = SpringContextBeanService.getBean(IUserService.class);
        }
        String token = (String) auth.getCredentials();
        if (Constant.isPass) {
            return new SimpleAuthenticationInfo(token, token, this.getName());
        }
        // 解密获得username，用于和数据库进行对比
        Integer userNo;
        try {
            userNo = SecureUtil.getUserId(token);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("token过期 请重新登录");
        }
        if (userNo == null) {
            throw new UnauthorizedException("token invalid");
        }
        User userBean = userService.getById(userNo);
        if (userBean == null) {
            throw new UnauthorizedException("User didn't existed!");
        }
        return new SimpleAuthenticationInfo(token, token, this.getName());
    }
}
