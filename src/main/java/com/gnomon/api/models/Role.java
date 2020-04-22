package com.gnomon.api.models;

import org.hibernate.annotations.NaturalId;

import com.gnomon.api.models.enums.RoleName;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@NaturalId
	@Column(length = 60)
	@NonNull
	private RoleName name;
}
