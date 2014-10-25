package com.pohlandt.test;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.SecurityUtils;

import com.google.inject.Module;
import com.pohlandt.inject.LoggingModule;
import com.pohlandt.wicket.WicketApplication;

public class TestWicketApplication extends WicketApplication {
	
	public TestWicketApplication() {
		super();
		SecurityManager securityManager = injector.getInstance(SecurityManager.class);
	    SecurityUtils.setSecurityManager(securityManager);
	}
	
	@Override 
	protected Module[] newGuiceModules() {
		return new Module[]{ new LoggingModule(), new TestSecurityModule(), new TestModule() };
	}
}
