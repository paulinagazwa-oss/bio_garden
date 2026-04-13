package com.github.paulinagazwa.oss.bio.garden.repository;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantCompanionEntity;
import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.model.RelationshipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantCompanionRepository extends JpaRepository<PlantCompanionEntity, Long> {

	List<PlantCompanionEntity> findByPlant(PlantEntity plant);

	List<PlantCompanionEntity> findByPlantAndRelationshipType(PlantEntity plant, RelationshipType relationshipType);

	List<PlantCompanionEntity> findByPlantIdAndRelationshipType(Long plantId, RelationshipType relationshipType);

	List<PlantCompanionEntity> findByPlantIdOrCompanionPlantId(Long plantId, Long companionPlantId);

	PlantCompanionEntity findByPlantIdAndCompanionPlantIdAndRelationshipType(Long plantId, Long companionPlantId, RelationshipType relationshipType);

	boolean existsByPlantIdAndCompanionPlantIdAndRelationshipType(Long plantId, Long companionPlantId, RelationshipType relationshipType);
}

