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
@Table(name="Role", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
@NamedQuery(name="Role.findAll", query="SELECT e FROM Role e")
public class Role extends com.pohlandt.entity.Entity {
	private static final long serialVersionUID = 1L;

	private String name;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = { 
			@JoinColumn(name = "role_id", nullable = false, updatable = false, referencedColumnName="ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "user_id", nullable = false, updatable = false, referencedColumnName="ID") })
	private Set<User> users = new HashSet<User>(0);

	public Role() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
}