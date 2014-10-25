package com.pohlandt.entity;

import java.util.function.Function;

import javax.persistence.EntityManager;

public interface IEntityManagerContext {
	EntityManager getRequestEntityManager();
	<T> T runInTransaction(Function<EntityManager, T> runnable);
}
