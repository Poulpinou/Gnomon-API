package com.gnomon.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gnomon.api.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Optional<User> findByNameOrEmail(String name, String email);

	List<User> findByIdIn(List<Long> userIds);

	Optional<User> findByName(String name);

	Boolean existsByName(String name);

	Boolean existsByEmail(String email);
}
