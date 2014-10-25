package com.pohlandt.entity;

import java.util.Collection;

public interface IEntityRepository {
	<T> Collection<T> findAll(Class<T> type);
	<T> T persist(T entity);
}
