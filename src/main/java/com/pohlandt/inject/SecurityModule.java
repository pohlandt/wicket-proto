package com.pohlandt.inject;

import javax.servlet.ServletContext;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;

import com.google.inject.name.Names;
import com.pohlandt.entity.IEntityRepository;
import com.pohlandt.security.EntityRealm;
import com.pohlandt.security.EntitySecurityManager;
import com.pohlandt.security.IHasher;
import com.pohlandt.security.ISecurityManager;

public class SecurityModule extends ShiroWebModule {

	public SecurityModule(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	protected void configureShiroWeb() {
		try {
            bindRealm().toConstructor(EntityRealm.class.getConstructor(Logger.class, IEntityRepository.class, CredentialsMatcher.class));
        } catch (NoSuchMethodException e) {
            addError(e);
        }
		
		bind(CredentialsMatcher.class).to(HashedCredentialsMatcher.class);
	    bind(HashedCredentialsMatcher.class);
	    bindConstant().annotatedWith(Names.named("shiro.hashAlgorithmName")).to(Sha256Hash.ALGORITHM_NAME);
	    bind(IHasher.class).toInstance(new IHasher() {
			@Override
			public String hash(String text, ByteSource salt) {
				return new Sha256Hash(text, salt).toHex();
			}
		});
	    bind(ISecurityManager.class).to(EntitySecurityManager.class);
	    expose(ISecurityManager.class);
	}

}