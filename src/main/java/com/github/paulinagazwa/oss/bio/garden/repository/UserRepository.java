package com.github.paulinagazwa.oss.bio.garden.repository;

import com.github.paulinagazwa.oss.bio.garden.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <UserEntity, Long> {

	Optional<UserEntity> findByEmailIgnoreCase(String email);

	Optional<UserEntity> findByUsernameIgnoreCase(String username);

	Optional<UserEntity> findByEmailIgnoreCaseOrUsernameIgnoreCase(String email, String username);

	boolean existsByEmailIgnoreCase(String email);

	boolean existsByUsernameIgnoreCase(String username);

	List<UserEntity> findByNotificationsEnabledTrue();

}
