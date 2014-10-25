package com.pohlandt.entity;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.wicket.util.lang.Args;

import com.google.inject.Inject;

public class JpaEntityRepository implements IEntityRepository {
	
	@Inject IEntityManagerContext entityManagerFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> findAll(Class<T> type) {
		final Query q = getEntityManager().createNamedQuery(String.format("%s.findAll", Args.notNull(type, "type").getSimpleName()));
		return (Collection<T>) q.getResultList(); 
	}

	@Override
	public <T> T persist(T entity) {
		EntityManager em = getEntityManager();
		
		if(em.contains(entity)){
			em.merge(entity);
		} else {
			em.persist(entity);
		}
				
		return entity;
	}
	
	public EntityManager getEntityManager(){
		return entityManagerFactory.getRequestEntityManager();
	}
}
