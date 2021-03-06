package org.se.lab.data;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.se.lab.service.dao.CommunityDAO;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;

public class CommunityDAOImpl extends DAOImplTemplate<Community> implements CommunityDAO{
	private final Logger LOG = Logger.getLogger(CommunityDAOImpl.class);
	
	/*
	 * class constructor
	 */
	
	public CommunityDAOImpl() {}
	
	@Override
	protected Class<Community> getEntityClass() {
		return Community.class;
	}
	
	@Override
	public Community findById(int id) {
		LOG.info("findById(int " + id + ")");
		Community c  = em.find(Community.class, id);
		return initializeCom(c);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Community> findAll() {
		LOG.info("findAll()");
		final String hql = "SELECT c FROM " + Community.class.getName() + " AS c";
		List<Community> coms = em.createQuery(hql).getResultList();
		for(Community c : coms) {
			initializeCom(c);
		}
		return coms;
	}
	
	@Override
	public Community findByName(String name) {
		LOG.info("findByName(name = " + name + ")");
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Community> criteria = builder.createQuery(Community.class);
		Root<Community> community = criteria.from(Community.class);
		criteria.where(builder.equal(community.get("name"), name));
		TypedQuery<Community> query = em.createQuery(criteria);
		try {
			Community c = query.getSingleResult();
			return initializeCom(c);
		} catch (NoResultException e) {
			return null;
		}
	}	

	@Override
	public List<Community> findPendingCommunities() {
		LOG.info("findPendingCommunites()");
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Community> criteria = builder.createQuery(Community.class);
		Root<Community> community = criteria.from(Community.class);
		criteria.where(builder.equal(community.get("state"), new Enumeration(1)));
		TypedQuery<Community> query = em.createQuery(criteria);
		try {
			List <Community> coms = query.getResultList();
			for(Community c : coms) {
				initializeCom(c);
			}
			return coms;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Community> findApprovedCommunities() {
		LOG.info("findApprovedCommunites()");
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Community> criteria = builder.createQuery(Community.class);
		Root<Community> community = criteria.from(Community.class);
		criteria.where(builder.equal(community.get("state"), new Enumeration(2)));
		TypedQuery<Community> query = em.createQuery(criteria);
		try {
			List <Community> coms = query.getResultList();
			for(Community c : coms) {
				initializeCom(c);
			}
			return coms;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Community createCommunity(String name, String description) {
		LOG.info("createCommunity(name = "+ name +", description = "+ description + ")");
		Community c = new Community();
		c.setName(name);
		c.setDescription(description);
		Enumeration e = getValidEnumeration(em.find(Enumeration.class, 1));
		c.setState(e);
		insert(c);
		return c;
	}
	
	
	private Enumeration getValidEnumeration(Enumeration find) {
		if (find != null )
			return find;
		EnumerationDAOImpl edao = new EnumerationDAOImpl();
		edao.setEntityManager(em);
		return edao.insert(edao.createEnumeration(1));
	}
	
	/*
	 * helper
	 */
	private Community initializeCom(Community c) {
		Hibernate.initialize(c.getState());
		Hibernate.initialize(c.getUsers());
		return c;
	}
	
}
