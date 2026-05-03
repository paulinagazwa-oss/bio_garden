package com.github.paulinagazwa.oss.bio.garden.entity;

import com.github.paulinagazwa.oss.bio.garden.converter.MonthDayAttributeConverter;
import com.github.paulinagazwa.oss.bio.garden.model.CropType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import java.time.MonthDay;
import java.util.HashSet;
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

	@Column(length = 5)
	@Convert(converter = MonthDayAttributeConverter.class)
	private MonthDay sowFrom;

	@Column(length = 5)
	@Convert(converter = MonthDayAttributeConverter.class)
	private MonthDay sowTo;

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

