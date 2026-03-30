package com.github.paulinagazwa.oss.bio.garden.entity;

import com.github.paulinagazwa.oss.bio.garden.model.CropType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}

