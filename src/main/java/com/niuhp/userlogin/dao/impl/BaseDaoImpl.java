package com.niuhp.userlogin.dao.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.niuhp.userlogin.dao.BaseDao;
import com.niuhp.userlogin.util.GenericsUtils;

@SuppressWarnings("unchecked")
public class BaseDaoImpl<T> extends HibernateDaoSupport implements BaseDao<T> {

	private Class<T> entityClass = GenericsUtils.getSuperClassGenricType(getClass());

	@Override
	public T findById(Serializable id) {
		return (T) getSessionFactory().getCurrentSession().get(entityClass, id);
	}

	@Override
	public List<T> findAll() {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("from ").append(entityClass.getSimpleName());
		Query query = getSessionFactory().getCurrentSession().createQuery(queryBuilder.toString());
		return query.list();
	}

	@Override
	public List<T> findByProperty(String propertyName, Object propertyValue) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("from ").append(entityClass.getSimpleName()).append(" t where t.").append(propertyName)
				.append("=:propertyName");
		Query query = getSessionFactory().getCurrentSession().createQuery(queryBuilder.toString());
		query.setParameter(propertyName, propertyValue);
		return query.list();
	}

	@Override
	public List<T> findByPropertyMap(Map<String, Object> propMap) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("from ").append(entityClass.getSimpleName());
		if (propMap != null && !propMap.isEmpty()) {
			queryBuilder.append(" t where");

			Iterator<String> iterator = propMap.keySet().iterator();
			String propertyName = iterator.next();
			queryBuilder.append(" t.").append(propertyName).append("=:").append(propertyName);

			while (iterator.hasNext()) {
				propertyName = iterator.next();
				queryBuilder.append(" and t.").append(propertyName).append("=:").append(propertyName);
			}
		}

		Query query = getSessionFactory().getCurrentSession().createQuery(queryBuilder.toString());
		if (propMap != null && !propMap.isEmpty()) {
			Iterator<String> iterator = propMap.keySet().iterator();
			while (iterator.hasNext()) {
				String propertyName = iterator.next();
				Object propertyValue = propMap.get(propertyName);
				query.setParameter(propertyName, propertyValue);
			}
		}

		return query.list();
	}

	@Override
	public T save(T entity) {
		return (T) getSessionFactory().getCurrentSession().save(entity);

	}

	@Override
	public void update(T entity) {
		getSessionFactory().getCurrentSession().update(entity);

	}

	@Override
	public void delete(T entity) {
		getSessionFactory().getCurrentSession().delete(entity);

	}

	@Override
	public void deleteById(Serializable id) {
		T entity = findById(id);
		if (entity != null) {
			delete(entity);
		}
	}

}
