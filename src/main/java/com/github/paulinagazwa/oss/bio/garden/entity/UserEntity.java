package com.github.paulinagazwa.oss.bio.garden.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_account", schema = "bio_garden")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 320, unique = true)
	private String email;

	@Column(length = 100, unique = true)
	private String username;

	@Column(name = "password_hash", nullable = false, length = 255)
	private String passwordHash;

	@Column(columnDefinition = "NUMERIC")
	private Double latitude;

	@Column(columnDefinition = "NUMERIC")
	private Double longitude;

	private Boolean enabled;

	private Boolean notificationsEnabled;

	private LocalDateTime creationDate;

	private LocalDateTime lastUpdateDate;
}
