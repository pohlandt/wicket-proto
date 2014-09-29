package com.google.inject.internal;

import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.spi.InjectionPoint;

public class Slf4jLoggerProvider implements Provider<Logger> {
	 
    /*===========================================[ INSTANCE VARIABLES ]===========*/
 
    private Injector injector;
    private java.util.logging.Logger logger;
 
    /*===========================================[ CONSTRUCTORS ]=================*/
 
    @Inject
    public void init(Injector injector, java.util.logging.Logger logger) {
        this.injector = injector;
        this.logger = logger;
    }
 
    /*===========================================[ INTERFACE METHODS ]============*/
 
    @Override
    public Logger get() {
        try {
            return ((InjectorImpl) injector).callInContext(new ContextualCallable<Logger>() {
                @Override
                public Logger call(InternalContext context) {
                    InjectionPoint injectionPoint = context.getDependency().getInjectionPoint();
                    Class<?> declaringClass = injectionPoint.getMember().getDeclaringClass();
                    return LoggerFactory.getLogger(declaringClass);
                }
            });
        } catch (ErrorsException e) {
            logger.log(Level.SEVERE, "failed to inject logger", e);
        }
 
        return LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    }
}