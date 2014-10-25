package com.pohlandt.wicket;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.pohlandt.IDestroyable;
import com.pohlandt.entity.IEntityManagerFactory;

public class JpaRequestCycleListener extends AbstractRequestCycleListener implements IDestroyable, IEntityManagerFactory {
	
	private static final MetaDataKey<EntityManager> EntityManagerKey = new MetaDataKey<EntityManager>() {
		private static final long serialVersionUID = 1L;
	};
	
	private static final MetaDataKey<EntityTransaction> TransactionKey = new MetaDataKey<EntityTransaction>() {
		private static final long serialVersionUID = 1L;
	};
	
	private final Logger logger;
	private final EntityManagerFactory emf;
		
	@Inject
	public JpaRequestCycleListener(Logger logger, @Named("persistence-unit-name")String puName) {
		this.logger = logger;
		logger.info("initializing with persistence unit name {}", puName);
		emf = Persistence.createEntityManagerFactory( puName );   
	}

	public EntityManager getEntityManager() {
		RequestCycle cycle = RequestCycle.get();
		EntityManager em = cycle.getMetaData(EntityManagerKey);
		if (em == null) {
			logger.debug("creating entity manager...");
			em = emf.createEntityManager();
			cycle.setMetaData(EntityManagerKey, em);
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			cycle.setMetaData(TransactionKey, tx);
		}
		
		return em;
	}
	
	@Override
	public void onEndRequest(RequestCycle cycle) {
		logger.debug("on end request");
		EntityManager em = cycle.getMetaData(EntityManagerKey);
		if (em != null){
			logger.debug("entity manager found in cycle");
			EntityTransaction tx = cycle.getMetaData(TransactionKey);
			try {
				logger.debug("committing...");
				tx.commit();	
		      } finally {
				if (tx.isActive()) {
					logger.warn("rolling back...");
					tx.rollback();
				}
				
				logger.debug("closing...");
				em.close();
			}
			
			cycle.setMetaData(EntityManagerKey, null);
			cycle.setMetaData(TransactionKey, null);
		}	
	}
		
	@Override
	public void onDestroy() {
		logger.info("destroying jpa request cycle listener");
		emf.close();
	}
}
