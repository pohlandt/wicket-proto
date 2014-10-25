package com.pohlandt.security;

public interface ISecurityManager {
	
	public boolean isReadOnly();
	
	public Iterable<String> getUsernames();
	
	public Iterable<String> getRolenames();

	public void createRole(String name);
	
	public void createUser(String name, String clearPass);
	
	public void updateUser(String name, String clearPass);

	public void initialize();

}
