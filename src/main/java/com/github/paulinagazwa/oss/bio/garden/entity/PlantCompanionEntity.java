package com.github.paulinagazwa.oss.bio.garden.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
	name = "plant_companion",
	schema = "bio_garden",
	uniqueConstraints = @UniqueConstraint(
		columnNames = {"plant_id", "companion_plant_id", "relationship_type"}
	)
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantCompanionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plant_companion_seq")
	@SequenceGenerator(name = "plant_companion_seq", sequenceName = "bio_garden.plant_companion_seq_id", allocationSize = 1)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "plant_id", nullable = false)
	private PlantEntity plant;

	@ManyToOne
	@JoinColumn(name = "companion_plant_id", nullable = false)
	private PlantEntity companionPlant;

	@Enumerated(EnumType.STRING)
	@Column(name = "relationship_type", nullable = false, length = 20)
	private RelationshipType relationshipType;

	@Column(name = "recommended_distance_cm")
	private Integer recommendedDistanceCm;

	@Column(name = "bidirectional", nullable = false)
	@Builder.Default
	private Boolean bidirectional = true;
}

