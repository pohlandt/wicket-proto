package com.pohlandt.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.pohlandt.inject.EntityModule;
import com.pohlandt.inject.LoggingModule;
import com.pohlandt.inject.SecurityModule;
import com.pohlandt.inject.WicketServletModule;

public class GuiceWicketContextListener extends GuiceServletContextListener {

	private ServletContext servletContext; 
	
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new LoggingModule(), new EntityModule(), new SecurityModule(servletContext), new WicketServletModule());
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		this.servletContext = servletContextEvent.getServletContext();
		super.contextInitialized(servletContextEvent);
	}
}
