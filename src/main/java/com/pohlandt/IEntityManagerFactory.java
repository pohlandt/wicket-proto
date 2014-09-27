package com.pohlandt;

import javax.persistence.EntityManager;

public interface IEntityManagerFactory {
	EntityManager getEntityManager();
}
