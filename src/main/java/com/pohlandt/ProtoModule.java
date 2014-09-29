package com.pohlandt;

import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.internal.Slf4jLoggerProvider;
import com.google.inject.name.Names;

public class ProtoModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Logger.class).toProvider(new Slf4jLoggerProvider());
		
		bindConstant().annotatedWith(Names.named("persistence-unit-name")).to("com.pohlandt.wicket-proto");
		
		bind(JpaRequestCycleListener.class).in(Singleton.class);
		bind(IEntityManagerFactory.class).to(JpaRequestCycleListener.class);
		bind(IRequestCycleListener.class).to(JpaRequestCycleListener.class);
		
		bind(IEntityRepository.class).to(JpaEntityRepository.class);
	}
}
