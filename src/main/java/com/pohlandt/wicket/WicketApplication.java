package com.pohlandt.wicket;

import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse;
import org.apache.wicket.markup.html.IHeaderResponseDecorator;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.slf4j.Logger;
import org.wicketstuff.shiro.annotation.AnnotationsShiroAuthorizationStrategy;
import org.wicketstuff.shiro.authz.ShiroUnauthorizedComponentListener;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.pohlandt.security.ISecurityManager;
import com.pohlandt.wicket.pages.HomePage;
import com.pohlandt.wicket.pages.LoginPage;
import com.pohlandt.wicket.pages.UnauthorizedPage;

import de.agilecoders.wicket.core.Bootstrap;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see com.pohlandt.test.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{
	@Inject Logger logger;
	@Inject ISecurityManager securityManager;
		
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
		
		Injector injector = getInjector();
		injector.injectMembers(this);
		
		final GuiceComponentInjector guiceListener = new GuiceComponentInjector(this, injector);
		getComponentInstantiationListeners().add(guiceListener);
		getBehaviorInstantiationListeners().add(guiceListener);
		
		setHeaderResponseDecorator(new IHeaderResponseDecorator()
        {
            @Override
            public IHeaderResponse decorate(IHeaderResponse response)
            {
                return new JavaScriptFilteredIntoFooterHeaderResponse(response, "bottomJS");
            }
        });
		
		Bootstrap.install(this);
		
		AnnotationsShiroAuthorizationStrategy authz = new AnnotationsShiroAuthorizationStrategy();
		getSecuritySettings().setAuthorizationStrategy(authz);
		getSecuritySettings().setUnauthorizedComponentInstantiationListener(
			new ShiroUnauthorizedComponentListener(LoginPage.class, UnauthorizedPage.class, authz));

		securityManager.initialize();
				
		mountPage("login", LoginPage.class);
		mountPage("unauthorized", UnauthorizedPage.class);
		
		
	}
	
	protected Injector getInjector() {
		return (Injector) getServletContext().getAttribute(Injector.class.getName());
	}
}
