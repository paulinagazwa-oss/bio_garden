package com.github.paulinagazwa.oss.bio.garden.entity;

import com.github.paulinagazwa.oss.bio.garden.model.CropType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "plant", schema = "bio_garden")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlantEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Enumerated(EnumType.ORDINAL)
	private CropType crop;

	private Integer daysToHarvest;

	private LocalDateTime creationDate;

	private LocalDateTime lastUpdateDate;

	private LocalDateTime sowFrom;

	private LocalDateTime sowTo;

	/**
	 * Neighborhood list, where this plant is the main plant and has companions
	 */
	@OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PlantCompanionEntity> companions = new HashSet<>();

	/**
	 * Neighborhood list, where this plant is the companion plant for other main plants
	 */
	@OneToMany(mappedBy = "companionPlant", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PlantCompanionEntity> companionFor = new HashSet<>();
}

