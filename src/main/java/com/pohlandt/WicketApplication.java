package com.pohlandt;

import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse;
import org.apache.wicket.markup.html.IHeaderResponseDecorator;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.slf4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

import de.agilecoders.wicket.core.Bootstrap;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see com.pohlandt.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{
	private final Injector injector;
	
	@Inject IRequestCycleListener requestCycleListener;
	@Inject Logger logger;
	
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
		
		logger.info("Here we go...");
		
		final GuiceComponentInjector guiceListener = new GuiceComponentInjector(this, injector);
		getComponentInstantiationListeners().add(guiceListener);
		getBehaviorInstantiationListeners().add(guiceListener);
		
		if(requestCycleListener != null){
			getRequestCycleListeners().add(requestCycleListener);	
		}
		
		setHeaderResponseDecorator(new IHeaderResponseDecorator()
        {
            @Override
            public IHeaderResponse decorate(IHeaderResponse response)
            {
                return new JavaScriptFilteredIntoFooterHeaderResponse(response, "bottomJS");
            }
        });
		
		Bootstrap.install(this);
	}
	
	@Override
	protected void onDestroy() {
		logger.info("destroying application...");
		super.onDestroy();
		if(requestCycleListener instanceof IDestroyable){
			logger.info("destroying {}", requestCycleListener.getClass().getSimpleName());
			((IDestroyable)requestCycleListener).onDestroy();
		}
	}
	
	protected Module[] newGuiceModules() {
		return new Module[]{ new ProtoModule() };
	}
}
