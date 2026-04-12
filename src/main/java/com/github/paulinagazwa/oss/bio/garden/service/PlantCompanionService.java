package com.github.paulinagazwa.oss.bio.garden.service;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantCompanionEntity;
import com.github.paulinagazwa.oss.bio.garden.entity.RelationshipType;

import java.util.List;

public interface PlantCompanionService {

	PlantCompanionEntity createCompanionRelationship(
			Long plantId,
			Long companionPlantId,
			RelationshipType relationshipType,
			Integer recommendedDistanceCm,
			Boolean bidirectional
	);

	List<PlantCompanionEntity> getCompanionsForPlant(Long plantId);

	List<PlantCompanionEntity> getCompanionsByType(Long plantId, RelationshipType relationshipType);

	List<PlantCompanionEntity> getGoodCompanions(Long plantId);

	List<PlantCompanionEntity> getBadCompanions(Long plantId);

	List<PlantCompanionEntity> getCompanionRowPlants(Long plantId);

	void deleteCompanionRelationship(Long id);

	PlantCompanionEntity updateCompanionRelationship(
			Long id,
			String effectDescription,
			Integer recommendedDistanceCm,
			Boolean bidirectional
	);

	List<PlantCompanionEntity> getAllRelationshipsForPlant(Long plantId);
}

