package com.pohlandt.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the text database table.
 * 
 */
@Entity
@Table(name="User", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
@NamedQuery(name="User.findAll", query="SELECT e FROM User e")
public class User extends com.pohlandt.entity.Entity {
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String password;
	
	private String salt;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = { 
			@JoinColumn(name = "user_id", nullable = false, updatable = false, referencedColumnName="id") }, 
			inverseJoinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false, referencedColumnName="id") })
	private Set<Role> roles = new HashSet<Role>(0);

	public User() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}