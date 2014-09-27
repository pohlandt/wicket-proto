package com.pohlandt;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.wicket.util.lang.Args;

import com.google.inject.Inject;

public class JpaEntityRepository implements IEntityRepository {
	
	@Inject IEntityManagerFactory entityManagerFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> findAll(Class<T> type) {
		final Query q = getEntityManager().createNamedQuery(String.format("%s.findAll", Args.notNull(type, "type").getSimpleName()));
		return (Collection<T>) q.getResultList(); 
	}
	
	public EntityManager getEntityManager(){
		return entityManagerFactory.getEntityManager();
	}

}
