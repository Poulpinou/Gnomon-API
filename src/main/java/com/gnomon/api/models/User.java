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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends DateAudit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(min = 3, max = 32)
	@NonNull
	private String name;
	
	@NaturalId
	@NotBlank
	@Size(min = 3, max = 64)
	@Email
	@NonNull
	private String email;
	
	@NotBlank
	@Size(min = 6, max=64)
	@NonNull
	private String password;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "user_roles",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles = new HashSet<Role>();
	
	@OneToMany(mappedBy = "user")
	private Set<AgendaConnection> agendaConnections;

	public User(String name, String email, String password) {	
		this.name = name;	
		this.email = email;	
		this.password = password;	
	}
}
