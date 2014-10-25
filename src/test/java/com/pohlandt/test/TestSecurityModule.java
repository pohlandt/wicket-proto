package com.pohlandt.test;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.guice.ShiroModule;
import org.slf4j.Logger;

import com.google.inject.name.Names;
import com.pohlandt.entity.IEntityRepository;
import com.pohlandt.security.EntityRealm;
import com.pohlandt.security.EntitySecurityManager;
import com.pohlandt.security.ISecurityManager;

public class TestSecurityModule extends ShiroModule {

	@Override
	protected void configureShiro() {
		try {
            bindRealm().toConstructor(EntityRealm.class.getConstructor(Logger.class, IEntityRepository.class, CredentialsMatcher.class));
        } catch (NoSuchMethodException e) {
            addError(e);
        }
		
		bind(CredentialsMatcher.class).to(HashedCredentialsMatcher.class);
	    bind(HashedCredentialsMatcher.class);
	    bindConstant().annotatedWith(Names.named("shiro.hashAlgorithmName")).to(Sha256Hash.ALGORITHM_NAME);
	    bind(ISecurityManager.class).to(EntitySecurityManager.class);
	}

}
