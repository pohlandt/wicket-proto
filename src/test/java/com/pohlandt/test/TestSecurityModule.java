package com.pohlandt.test;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.guice.ShiroModule;
import org.slf4j.Logger;

import com.google.inject.name.Names;
import com.pohlandt.security.DefaultRealm;

public class TestSecurityModule extends ShiroModule {

	@Override
	protected void configureShiro() {
		try {
            bindRealm().toConstructor(DefaultRealm.class.getConstructor(Logger.class));
        } catch (NoSuchMethodException e) {
            addError(e);
        }
		
		bind(CredentialsMatcher.class).to(HashedCredentialsMatcher.class);
	    bind(HashedCredentialsMatcher.class);
	    bindConstant().annotatedWith(Names.named("shiro.hashAlgorithmName")).to(Md5Hash.ALGORITHM_NAME);
	}

}
