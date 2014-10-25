package com.pohlandt.security;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.pohlandt.entity.IEntityRepository;
import com.pohlandt.model.User;

public class EntityRealm extends AuthorizingRealm
{
	private final Logger logger;
	private final IEntityRepository entityRepository;

	@Inject
	public EntityRealm(Logger logger, IEntityRepository entityRepository, CredentialsMatcher credentialsMatcher)
	{
		this.logger = logger;
		this.entityRepository = entityRepository;

		setCacheManager(new MemoryConstrainedCacheManager());
		setCredentialsMatcher(credentialsMatcher);
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
		throws AuthenticationException
	{
		UsernamePasswordToken upToken = (UsernamePasswordToken)token;
		
		try{
			User u = getUser(upToken.getUsername());
			if(u != null){
				return newAccount(u);
			}
		} catch(Exception ex){
			logger.error("failed to fetch user", ex);
			throw new AuthenticationException(ex);
		}
		
		throw new UnknownAccountException("No account found for user [" + upToken.getUsername() + "]");
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
	{
		@SuppressWarnings("unchecked")
		Collection<String> thisRealmPrincipals = principals.fromRealm(getName());
		if (thisRealmPrincipals == null || thisRealmPrincipals.isEmpty())
		{
			return null;
		}

		try  {
			String name = thisRealmPrincipals.iterator().next();
			User u = getUser(name);
			if(u == null){
				logger.error("authenticated user not found: " + name);
				return null;
			}
			return newAccount(u);
		} catch(Exception ex){
			logger.error("failed to fetch user/roles/permissions");
		}
		
		return null;
	}
	
	private User getUser(String name){
		Optional<User> first = entityRepository.findAll(User.class).stream().filter(u -> u.getName() != null && u.getName().equals(name)).findFirst();
		if(first.isPresent()){
			return first.get();
		}
		
		return null;
	}
	
	private SimpleAccount newAccount(User u){
		SimpleAccount a = new SimpleAccount(u.getName(), u.getPassword(), new SimpleByteSource(Hex.decode(u.getSalt())), getName());
		if (u.getRoles() != null) {
			a.addRole(u.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList()));
		}
		
		return a;
	}
}