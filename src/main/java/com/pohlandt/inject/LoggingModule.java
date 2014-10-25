package com.pohlandt.inject;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.internal.Slf4jLoggerProvider;

public class LoggingModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Logger.class).toProvider(new Slf4jLoggerProvider());
	}
}
