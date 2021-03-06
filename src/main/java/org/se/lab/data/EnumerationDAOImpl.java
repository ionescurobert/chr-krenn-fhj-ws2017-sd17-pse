package org.se.lab.data;

import java.util.List;

import org.apache.log4j.Logger;
import org.se.lab.service.dao.EnumerationDAO;

public class EnumerationDAOImpl extends DAOImplTemplate<Enumeration> implements EnumerationDAO {

	private final Logger LOG = Logger.getLogger(EnumerationDAOImpl.class);

	@Override
	public Enumeration insert(Enumeration enumeration) {
		LOG.info("insert(" + enumeration + ")");
		return super.insert(enumeration);
	}

	@Override
	public Enumeration update(Enumeration enumeration) {
		LOG.info("update(" + enumeration + ")");
		return super.update(enumeration);
	}

	@Override
	public void delete(Enumeration enumeration) {
		LOG.info("delete(" + enumeration + ")");
		super.delete(enumeration);
	}
	
	@Override
	public Enumeration createEnumeration(int id) {
		Enumeration e = new Enumeration();
		
		switch (id) {
			case 1:
				e.setName("PENDING");
				break;
			case 2:
				e.setName("APPROVED");
				break;
			case 3:
				e.setName("REFUSED");
				break;
			case 4:
				e.setName("ADMIN");
				break;
			case 5:
				e.setName("PORTALADMIN");
				break;
			case 6:
				e.setName("USER");
				break;
			case 7: 
				e.setName("LIKE");
				break;
			default:
				throw new IllegalArgumentException();
		}
		
		insert(e);
		
		return e;
	}
	
	@Override
	public Enumeration findById(int id) {
		LOG.info("findById(" + id + ")");       
        return super.findById(id);
	}

	@Override
	public List<Enumeration> findAll() {
		LOG.info("findAll()");
        return super.findAll();		
	}

	@Override
	public List<User> findUsersByEnumeration(int id) {
		return findById(id).getUser();
	}

	@Override
	public List<Community> findCommunitiesByEnumeration(int id) {
		return findById(id).getCom();
	}

	@Override
	public List<Post> findLikedPostsByEnumeration(int id) {
		return findById(id).getLikedPosts();
	}

	@Override
	public List<User> findLikedUsersByEnumeration(int id) {
		return findById(id).getLikedBy();
	}

	@Override
	protected Class<Enumeration> getEntityClass() {
		return Enumeration.class;
	}
}
