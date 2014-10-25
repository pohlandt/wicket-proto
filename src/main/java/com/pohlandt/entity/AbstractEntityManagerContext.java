package com.pohlandt.entity;

import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.slf4j.Logger;

import com.google.inject.Inject;

public abstract class AbstractEntityManagerContext implements
		IEntityManagerContext {

	@Inject
	Logger logger;

	protected abstract EntityManager getOneShotEntityManager();

	@Override
	public <T> T runInTransaction(Function<EntityManager, T> runnable) {
		final EntityManager em = getOneShotEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			T result = runnable.apply(em);
			tx.commit();
			return result;
		} finally {
			rollBackUncommitedAndClose(tx, em);
		}
	}

	protected void rollBackUncommitedAndClose(EntityTransaction tx, EntityManager em) {
		if (tx != null && tx.isActive()) {
			try {
				tx.rollback();
			} catch (Exception ex) {
				logger.error("failed to roll back", ex);
			}
		}

		if (em != null) {
			try {
				em.close();
			} catch (Exception ex) {
				logger.error("failed to close entity manager", ex);
			}
		}
	}
}
