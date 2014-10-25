package com.pohlandt.entity;

import javax.persistence.EntityManager;

public interface IEntityManagerFactory {
	EntityManager getEntityManager();
}
