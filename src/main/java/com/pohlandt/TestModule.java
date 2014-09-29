package com.pohlandt;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.spi.PersistenceUnitTransactionType;

import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.slf4j.Logger;

import model.Text;

import com.google.inject.AbstractModule;
import com.google.inject.internal.Slf4jLoggerProvider;
import com.google.inject.matcher.Matchers;
import com.google.inject.util.Providers;

public class TestModule extends AbstractModule {

	// org.eclipse.persistence.config.PersistenceUnitProperties
	public static final String TRANSACTION_TYPE = "javax.persistence.transactionType";
	public static final String JDBC_DRIVER = "javax.persistence.jdbc.driver";
	public static final String JDBC_URL = "javax.persistence.jdbc.url";
	public static final String NON_JTA_DATASOURCE = "javax.persistence.nonJtaDataSource";
	
	
	private EntityManagerFactory entityManagerFactory;
	
	private final IEntityManagerFactory entityManagerFactoryImpl = new IEntityManagerFactory() {
		@Override
		public EntityManager getEntityManager() {
			if(entityManagerFactory == null){
				/*
				 <property name="javax.persistence.jdbc.driver" value="org.h2.Driver" />
            	 <property name="javax.persistence.jdbc.url" value="jdbc:h2:mem:test;DB_CLOSE_DELAY=-1" />
            	 <property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect" />
            	 <property name="hibernate.show_sql" value="true" />
            	 <property name="hibernate.hbm2ddl.auto" value="create" />
				 */
				
				Map<String, String> properties = new HashMap<>();
				 
			    // Ensure RESOURCE_LOCAL transactions is used.
			    properties.put(TRANSACTION_TYPE,
			        PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
			 
			    properties.put(NON_JTA_DATASOURCE, null);
			    
			    // Configure the internal EclipseLink connection pool
			    properties.put(JDBC_DRIVER, "org.h2.Driver");
			    properties.put(JDBC_URL, "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
			    
			    properties.put("hibernate.show_sql", "true");
			    properties.put("hibernate.hbm2ddl.auto", "create");
			 
			    entityManagerFactory = Persistence.createEntityManagerFactory("com.pohlandt.wicket-proto", properties);
			    intializeTestDatastore(entityManagerFactory.createEntityManager());
			}
			
			return entityManagerFactory.createEntityManager();
		}
	};

	private static void intializeTestDatastore(EntityManager entityManager){
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();
		
		Text t = new Text();
		t.setId(1);
		t.setText("numero uno");
		entityManager.persist(t);
		
		tx.commit();
		entityManager.close();
	}
	
	@Override
	protected void configure() {
		bind(Logger.class).toProvider(new Slf4jLoggerProvider());
		bind(IRequestCycleListener.class).toInstance(new AbstractRequestCycleListener() {
		});
		bind(IEntityRepository.class).to(JpaEntityRepository.class);
		bind(IEntityManagerFactory.class).toInstance(entityManagerFactoryImpl);
	}

}
