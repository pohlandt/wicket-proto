package com.pohlandt.inject;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.pohlandt.entity.IEntityManagerContext;
import com.pohlandt.entity.IEntityRepository;
import com.pohlandt.entity.JpaEntityRepository;
import com.pohlandt.servlet.JpaServletFilter;

public class EntityModule extends AbstractModule {

	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named("persistence-unit-name")).to("com.pohlandt.wicket-proto");
		
		/*
		  	@Provides 
			IEntityManagerFactory provideEntityManagerFactory(){
				return JpaServletFilter.get();
			}
			
			fails :( Java 8 issue?
		 */
		
		/*
		bind(IEntityManagerFactory.class).toInstance(new IEntityManagerFactory() {
			@Override
			public EntityManager getEntityManager() {
				return JpaServletFilter.get().getEntityManager();
			}
		});
		*/
		
		bind(IEntityManagerContext.class).to(JpaServletFilter.class);
		
		bind(IEntityRepository.class).to(JpaEntityRepository.class);
	}
}
