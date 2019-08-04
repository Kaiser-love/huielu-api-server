package com.ronghui.service.jpa;

import com.ronghui.service.common.constant.SqlConstant;
import com.ronghui.service.common.request.RequestBean;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.List;

@Component("BaseDao")
public class BaseDaoImpl<T> implements BaseDao<T> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List findBySql(String sql) {
        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }

    @Override
    public List findBySql(String sql, Class clazz) {
        Query query = entityManager.createNativeQuery(sql, clazz);
        return query.getResultList();
    }

    @Override
    public List findAllBySql(String table, String query, String queryString, int page, int count, Class clazz) {
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.getQuerySql(table, query, "like", "%" + queryString + "%"))
                .append("limit ")
                .append(page * count + "," + count);
        return entityManager.createNativeQuery(sql.toString(), clazz).getResultList();
    }

    @Override
    public List findAllBySql(String table, String connection, RequestBean requestBean, int page, int count, Class clazz) {
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.getQuerySql(table, connection, requestBean))
                .append("limit ")
                .append(page * count + "," + count);
        return entityManager.createNativeQuery(sql.toString(), clazz).getResultList();
    }

    @Override
    public List findAllBySql(String table, String connection, String query, String queryString, int page, int count, Class clazz) {
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.getQuerySql(table, query, connection, queryString))
                .append("limit ")
                .append(page * count + "," + count);
        return entityManager.createNativeQuery(sql.toString(), clazz).getResultList();
    }

    @Override
    public List findAllBySql(String table, String connection, String query, String queryString, String orderBy, String sortType, int page, int count, Class clazz) {
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.getQuerySql(table, query, connection, queryString))
                .append(" order by ").append(orderBy)
                .append(" ").append(sortType)
                .append(" limit ")
                .append(page * count + "," + count);
        return entityManager.createNativeQuery(sql.toString(), clazz).getResultList();
    }

    @Override
    public List findAllBySql(String table, String connection, String query, String queryString, String orderBy, String sortType, String keyString, Integer type, int page, int count, Class clazz) {
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.getQuerySql(table, query, connection, queryString))
                .append(" and ").append(keyString).append("= ").append(type)
                .append(" order by ").append(orderBy)
                .append(" ").append(sortType)
                .append(" limit ")
                .append(page * count + "," + count);
        return entityManager.createNativeQuery(sql.toString(), clazz).getResultList();
    }

    @Override
    public List findAll(String table, Class clazz) {
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.SELECT_QUERY).append(table);
        return entityManager.createNativeQuery(sql.toString(), clazz).getResultList();
    }

    @Override
    public Object findAllSize(String table) {
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.SELECT_QUERY_SIZE).append(table);
        List resultList = entityManager.createNativeQuery(sql.toString()).getResultList();
        if (!StringUtils.isEmpty(resultList))
            return resultList.get(0);
        return 0;
    }

    @Override
    public Object findAllSize(String table, String keyString, Integer type) {
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.SELECT_QUERY_SIZE).append(table)
                .append(" where " + keyString + " = ")
                .append(type);
        List resultList = entityManager.createNativeQuery(sql.toString()).getResultList();
        if (!StringUtils.isEmpty(resultList))
            return resultList.get(0);
        return 0;
    }

    @Override
    public Object findAllSizeByCondition(String table, String query, String connection, String queryString) {
        StringBuffer sql = new StringBuffer();
        if ("like".equals(connection)) {
            queryString = "\'%" + queryString + "%\'";
        }
        sql.append(SqlConstant.SELECT_QUERY_SIZE).append(table + " where ")
                .append(query)
                .append(" ")
                .append(connection)
                .append(" ")
                .append('\'' + queryString + "\'");
        List resultList = entityManager.createNativeQuery(sql.toString()).getResultList();
        if (!StringUtils.isEmpty(resultList))
            return resultList.get(0);
        return 0;
    }

    @Override
    public Object findAllSizeByCondition(String table, String query, String connection, String queryString, String keyString, Integer type) {
        StringBuffer sql = new StringBuffer();
        if ("like".equals(connection)) {
            queryString = "\'%" + queryString + "%\'";
        }
        sql.append(SqlConstant.SELECT_QUERY_SIZE).append(table + " where ")
                .append(query)
                .append(" ")
                .append(connection)
                .append(" ")
                .append('\'' + queryString + "\'")
                .append(" and ")
                .append(keyString + " =")
                .append(type);
        List resultList = entityManager.createNativeQuery(sql.toString()).getResultList();
        if (!StringUtils.isEmpty(resultList))
            return resultList.get(0);
        return 0;
    }

    @Override
    public List findAllByPage(String table, Integer page, Integer pageSize, Class clazz) {
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.SELECT_QUERY).append(table)
                .append(" limit ")
                .append(page * pageSize + "," + pageSize);
        return entityManager.createNativeQuery(sql.toString(), clazz).getResultList();
    }

    @Override
    public List findAllByPage(String table, String orderBy, String sortType, Integer page, Integer pageSize, Class clazz) {
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.SELECT_QUERY).append(table)
                .append(" order by ").append(orderBy)
                .append(" ").append(sortType)
                .append(" limit ")
                .append(page * pageSize + "," + pageSize);
        return entityManager.createNativeQuery(sql.toString(), clazz).getResultList();
    }

    @Override
    public List findAllByPage(String table, String orderBy, String sortType, String keyString, Integer type, Integer page, Integer pageSize, Class clazz) {
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.SELECT_QUERY).append(table)
                .append(" where ").append(keyString).append(" = ").append(type)
                .append(" order by ").append(orderBy)
                .append(" ").append(sortType)
                .append(" limit ")
                .append(page * pageSize + "," + pageSize);
        return entityManager.createNativeQuery(sql.toString(), clazz).getResultList();
    }

    @Override
    @Transactional
    public void save(T bean) {
        entityManager.persist(bean);
    }

    @Override
    @Transactional
    public void update(T bean) {
        entityManager.merge(bean);
    }

    @Override
    public T findById(Long id, Class clazz) {
        return (T) entityManager.find(clazz, id);
    }

    @Override
    @Transactional
    public int deleteById(Long id, Class clazz) {
        Query query = entityManager.createQuery("delete from " + clazz.getSimpleName() + " p where p.id = ?1");
        query.setParameter(1, id);
        return query.executeUpdate();
    }

    @Override
    public List<T> findAll(Class clazz) {
        String hql = "select t from " + clazz.getSimpleName() + " t";
        Query query = entityManager.createQuery(hql);
        List<T> beans = query.getResultList();
        return beans;
    }

    @Override
    public List<T> findAll(Integer page, Integer pageSize, Class clazz) {
        return entityManager.createQuery("from " + clazz.getSimpleName()).setFirstResult(page).setMaxResults(pageSize).getResultList();
    }

    @Override
    @Transactional
    public void refreshSession(Object o) {
        Session session = entityManager.unwrap(Session.class);
        session.clear();
    }

    @Override
    @Transactional
    public Integer updateByArrtribute(String table, RequestBean source, RequestBean target) {
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.getUpdateSql(table, source, target));
        return entityManager.createNativeQuery(sql.toString()).executeUpdate();
    }

    @Override
    @Transactional
    public Integer deleteByIdList(String table, String query, List<Long> idList) {
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.getDeleteSql(table, query, idList));
        return entityManager.createNativeQuery(sql.toString()).executeUpdate();
    }

    @Override
    public List findByArrtribute(String table, String query, String queryString, Class clazz) {
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.getQuerySql(table, query, "=", queryString));
        return entityManager.createNativeQuery(sql.toString(), clazz).getResultList();
    }

    @Override
    public List findAllBySql(String table, String query, String queryString, Class clazz) {
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.getQuerySql(table, query, "like", "%" + queryString + "%"));
        return entityManager.createNativeQuery(sql.toString(), clazz).getResultList();
    }

    @Override
    public List findAllBySql(String table, String query, String connection, String queryString, Class clazz) {
        StringBuffer sql = new StringBuffer();
        if ("like".equals(connection)) {
            queryString = "%" + queryString + "%";
        }
        sql.append(SqlConstant.getQuerySql(table, query, connection, queryString));
        return entityManager.createNativeQuery(sql.toString(), clazz).getResultList();
    }
}
