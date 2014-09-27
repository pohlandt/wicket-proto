package com.pohlandt;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see com.pohlandt.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{
	private final Injector injector;
	@Inject Logger log;
	
	public WicketApplication() {
		injector = Guice.createInjector(newGuiceModules());
		injector.injectMembers(this);
	}
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();
		
		log.log(Level.INFO, "Here we go...");
		
		final GuiceComponentInjector guiceListener = new GuiceComponentInjector(this, injector);
		getComponentInstantiationListeners().add(guiceListener);
		getBehaviorInstantiationListeners().add(guiceListener);
	}
	
	protected Module[] newGuiceModules() {
		return new Module[]{ new ProtoModule() };
	}
}
