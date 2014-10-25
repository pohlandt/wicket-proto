package com.pohlandt.test;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.SecurityUtils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.pohlandt.inject.LoggingModule;
import com.pohlandt.wicket.WicketApplication;

public class TestWicketApplication extends WicketApplication {
	
	public TestWicketApplication() {
		super();
	}
	
	@Override
	protected Injector getInjector() {
		Injector injector = Guice.createInjector( new Module[]{ new LoggingModule(), new TestEntityModule(), new TestSecurityModule() });

		SecurityManager securityManager = injector.getInstance(SecurityManager.class);
	    SecurityUtils.setSecurityManager(securityManager);
	    
		return injector;
	}
}
