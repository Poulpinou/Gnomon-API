package com.gnomon.api.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import com.gnomon.api.agenda.models.AgendaConnection;
import com.gnomon.api.models.audits.DateAudit;

@Entity
@Table(
	name="users", 
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {
				"name"
		}),
		@UniqueConstraint(columnNames = {
				"email"
		})
	}
)
public class User extends DateAudit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(min = 3, max = 32)
	private String name;
	
	@NaturalId
	@NotBlank
	@Size(min = 3, max = 64)
	@Email
	private String email;
	
	@NotBlank
	@Size(min = 6, max=64)
	private String password;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "user_roles",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles = new HashSet<Role>();
	
	@OneToMany(mappedBy = "user")
	Set<AgendaConnection> agendaConnections;
	
	
	public User() {}
	
	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
