package com.pohlandt.inject;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.wicket.protocol.http.WicketFilter;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.pohlandt.wicket.WicketApplication;

public class WicketServletModule extends ServletModule {
	@Override
	protected void configureServlets() {
		Map<String, String> initParams = new HashMap<String, String>(2);
        initParams.put("applicationClassName", WicketApplication.class.getName());
        initParams.put(WicketFilter.FILTER_MAPPING_PARAM, "/*");
        
        bind(WicketFilter.class).in(Singleton.class);
        filterRegex("/.*").through(WicketFilter.class, initParams);
        
        ShiroWebModule.bindGuiceFilter(binder());
	}
}
