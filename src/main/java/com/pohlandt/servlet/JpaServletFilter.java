package com.pohlandt.servlet;

import java.io.IOException;

import javax.persistence.*;
import javax.servlet.*;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.pohlandt.entity.AbstractEntityManagerContext;

public class JpaServletFilter extends AbstractEntityManagerContext implements Filter {

	@Inject
	private Logger logger;

	@Inject
	@Named("persistence-unit-name")
	private String puName;

	private static final ThreadLocal<EntityManager> entityManagerHolder = new ThreadLocal<EntityManager>();
	private static final ThreadLocal<EntityTransaction> transactionHolder = new ThreadLocal<EntityTransaction>();
	private EntityManagerFactory emf;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// eager initialization
		getFactory();
	}

	private EntityManagerFactory getFactory() {
		if (emf == null) {
			try {
				logger.info(
						"initializing entity manager factory to persistence unit name: {}",
						puName);
				emf = Persistence.createEntityManagerFactory(puName);
			} catch (Exception ex) {
				logger.error("failed to create EntityManagerFactory", ex);
				throw ex;
			}
		}

		return emf;
	}

	@Override
	public void destroy() {
		emf.close();
		emf = null;
	}

	@Override
	public void doFilter(ServletRequest requ, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(requ, resp);
			EntityTransaction tx = transactionHolder.get();
			if (tx != null) {
				tx.commit();
			}
		} finally {
			EntityTransaction tx = transactionHolder.get();
			EntityManager em = entityManagerHolder.get();
			rollBackUncommitedAndClose(tx, em);
			entityManagerHolder.remove();
			transactionHolder.remove();
		}
	}

	public EntityManager getRequestEntityManager() {
		EntityManager em = entityManagerHolder.get();
		if (em == null) {
			em = getFactory().createEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			entityManagerHolder.set(em);
			transactionHolder.set(tx);
		}

		return em;
	}

	@Override
	protected EntityManager getOneShotEntityManager() {
		return getFactory().createEntityManager();
	}

	

}