package com.ronghui.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ronghui.service.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> findMenuByRoleCode(@Param("roleCode") String roleCode);
}
