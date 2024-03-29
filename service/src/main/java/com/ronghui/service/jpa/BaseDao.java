package com.ronghui.service.jpa;

import com.ronghui.service.common.request.RequestBean;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BaseDao<T> {
    // 执行具体的SQL语句
    List findBySql(String sql);

    // 执行具体的SQL语句
    List findBySql(String sql, Class clazz);

    // 通过传入的属性和值查询具体的结果
    List findByArrtribute(String table, String query, String queryString, Class clazz);

    // 根据传入的属性和值模糊查询不分页
    List findAllBySql(String table, String query, String queryString, Class clazz);

    // 根据传入的属性和值查询不分页
    List findAllBySql(String table, String query, String connection, String queryString, Class clazz);

    // 通过传入的参数查询
    List findAllBySql(String table, String query, String queryString, int page, int count, Class clazz);

    // 根据传入的参数修改指定数据
    Integer updateByArrtribute(String table, RequestBean source, RequestBean target);

    // 根据ID批量删除表中的多条数据
    Integer deleteByIdList(String table, String query, List<Long> idList);

    // 根据传入的多属性进行搜索
    List findAllBySql(String table, String connection, RequestBean requestBean, int page, int count, Class clazz);

    // 根据传入的属性进行或查询
    List findAllBySql(String table, String connection, String query, String queryString, int page, int count, Class clazz);

    // 根据传入的属性进行或查询
    List findAllBySql(String table, String connection, String query, String queryString, String orderBy, String sortType, int page, int count, Class clazz);

    List findAllBySql(String table, String connection, String query, String queryString, String orderBy, String sortType, String keyString, Integer type, int page, int count, Class clazz);

    List findAll(String table, Class clazz);

    Object findAllSize(String table);

    Object findAllSize(String table, String keyString, Integer type);

    Object findAllSizeByCondition(String table, String query, String connection, String queryString);

    Object findAllSizeByCondition(String table, String query, String connection, String queryString, String keyString, Integer type);

    List findAllByPage(String table, Integer page, Integer pageSize, Class clazz);

    List findAllByPage(String table, String orderBy, String sortType, Integer page, Integer pageSize, Class clazz);

    List findAllByPage(String table, String orderBy, String sortType, String keyString, Integer type, Integer page, Integer pageSize, Class clazz);

    void save(T bean);

    void update(T bean);

    T findById(Long id, Class clazz);

    int deleteById(Long id, Class clazz);

    List<T> findAll(Class clazz);

    List<T> findAll(Integer page, Integer pageSize, Class clazz);

    void refreshSession(Object o);

}
