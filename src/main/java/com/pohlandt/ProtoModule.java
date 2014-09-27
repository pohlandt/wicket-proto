package com.pohlandt;

import javax.persistence.EntityManager;

import com.google.inject.AbstractModule;

public class ProtoModule extends AbstractModule {

	private static final IEntityManagerFactory entityManagerFactory = new IEntityManagerFactory() {
		@Override
		public EntityManager getEntityManager() {
			return JpaServletFilter.getEntityManager();
		}
	}; 
	
	@Override
	protected void configure() {
		bind(IEntityRepository.class).to(JpaEntityRepository.class);
		bind(IEntityManagerFactory.class).toInstance(entityManagerFactory);
	}
}
