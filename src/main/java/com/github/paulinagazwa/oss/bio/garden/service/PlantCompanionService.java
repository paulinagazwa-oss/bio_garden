package com.github.paulinagazwa.oss.bio.garden.service;

import com.github.paulinagazwa.oss.bio.garden.model.CompanionRequest;
import com.github.paulinagazwa.oss.bio.garden.model.CompanionUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCompanion;
import com.github.paulinagazwa.oss.bio.garden.model.RelationshipType;

import java.util.List;

public interface PlantCompanionService {

	PlantCompanion createCompanionRelationship(CompanionRequest companionRequest);

	List<PlantCompanion> getCompanionsForPlant(Long plantId);

	List<PlantCompanion> getCompanionsByType(Long plantId, RelationshipType relationshipType);

	void deleteCompanionRelationship(Long id);

	PlantCompanion updateCompanionRelationship(Long plantId, CompanionUpdateRequest updateRequest);

	List<PlantCompanion> getAllRelationshipsForPlant(Long plantId);
}

