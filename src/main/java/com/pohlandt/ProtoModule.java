package com.pohlandt;

import org.apache.wicket.request.cycle.IRequestCycleListener;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class ProtoModule extends AbstractModule {

	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named("persistence-unit-name")).to("com.pohlandt.wicket-proto");
		
		bind(JpaRequestCycleListener.class).in(Singleton.class);
		bind(IEntityManagerFactory.class).to(JpaRequestCycleListener.class);
		bind(IRequestCycleListener.class).to(JpaRequestCycleListener.class);
		
		bind(IEntityRepository.class).to(JpaEntityRepository.class);
	}
}
