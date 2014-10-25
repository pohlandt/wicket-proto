package com.pohlandt.inject;

import javax.servlet.ServletContext;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.slf4j.Logger;

import com.google.inject.name.Names;
import com.pohlandt.security.DefaultRealm;

public class SecurityModule extends ShiroWebModule {

	public SecurityModule(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	protected void configureShiroWeb() {
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
