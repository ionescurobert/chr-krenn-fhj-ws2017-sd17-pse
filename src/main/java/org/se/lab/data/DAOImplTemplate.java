package org.se.lab.data;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.se.lab.service.dao.DAOTemplate;

public abstract class DAOImplTemplate<E> implements DAOTemplate<E> {
	/*
	 * Constructor injection
	 */
	@PersistenceContext
	protected EntityManager em;

	public DAOImplTemplate() {}
	
	/**
	 * Set the EntityManager for DAO
	 * @param em
	 */
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	public EntityManager getEntityManager() {
		return em;
	}

	protected abstract Class<E> getEntityClass();

	/*
	 * CRUD methods
	 */

	@Override
	public E insert(E entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public E update(E entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(E entity) {
		em.remove(entity);
	}

	@Override
	public E findById(int id) {
		return em.find(getEntityClass(), id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAll() {
		final String hql = "SELECT u FROM " + getEntityClass().getName() + " AS u";
		return em.createQuery(hql).getResultList();
	}
}
