package com.pohlandt.security;

import java.util.Iterator;
import java.util.Optional;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.pohlandt.entity.IEntityManagerContext;
import com.pohlandt.entity.IEntityRepository;
import com.pohlandt.model.Role;
import com.pohlandt.model.User;

public class EntitySecurityManager implements ISecurityManager
{
	private final Logger logger;
	private final IHasher hasher;
	private final IEntityRepository entityRepository;
	private final IEntityManagerContext entityManagerContext;
	private final RandomNumberGenerator rng = new SecureRandomNumberGenerator();

	@Inject
	public EntitySecurityManager(Logger logger, IHasher hasher, IEntityRepository entityRepository, IEntityManagerContext entityManagerContext)
	{
		this.logger = logger;
		this.hasher = hasher;
		this.entityRepository = entityRepository;
		this.entityManagerContext = entityManagerContext;
	}

	@Override
	public void createUser(String name, String clearPass) {
		logger.info("creating user '{}'", name);
		
		User u = new User();
		u.setName(name);
		updatePassword(u, clearPass);
		
		entityRepository.persist(u);
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public Iterable<String> getUsernames() {
		final Iterator<String> it = entityRepository.findAll(User.class).stream().map(u -> u.getName()).iterator();
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {
				return it;
			}
		};
	}

	@Override
	public Iterable<String> getRolenames() {
		final Iterator<String> it = entityRepository.findAll(Role.class).stream().map(r -> r.getName()).iterator();
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {
				return it;
			}
		};
	}

	@Override
	public void createRole(String name) {
		Role r = new Role();
		r.setName(name);
		entityRepository.persist(r);
	}

	@Override
	public void updateUser(String name, String clearPass) {
		User u = getUser(name);
		if(u == null){
			throw new RuntimeException("unknown user: " + name);
		}
		
		updatePassword(u, clearPass);
		
		entityRepository.persist(u);		
	}
	
	private User updatePassword(User u, String clearPass){
		ByteSource salt = rng.nextBytes();
		u.setPassword(hasher.hash(clearPass, salt));
		u.setSalt(salt.toHex());
		return u;
	}
	
	private User getUser(String name){
		Optional<User> first = entityRepository.findAll(User.class).stream().filter(u -> u.getName() != null && u.getName().equals(name)).findFirst();
		if(first.isPresent()){
			return first.get();
		}
		
		return null;
	}

	@Override
	public void initialize() {
		if(!isReadOnly() && !getUsernames().iterator().hasNext()){
			entityManagerContext.runInTransaction(em -> {
				logger.info("no users found, creating default admin account");
				
				User u = new User();
				u.setName("admin");
				updatePassword(u, "admin");
				
				em.persist(u);
				
				return null;
			});
		}
	}
}