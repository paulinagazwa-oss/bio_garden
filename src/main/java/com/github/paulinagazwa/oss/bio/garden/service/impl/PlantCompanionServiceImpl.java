package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantCompanionEntity;
import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.entity.RelationshipType;
import com.github.paulinagazwa.oss.bio.garden.exception.PlantCompanionException;
import com.github.paulinagazwa.oss.bio.garden.exception.PlantNotFoundException;
import com.github.paulinagazwa.oss.bio.garden.repository.PlantCompanionRepository;
import com.github.paulinagazwa.oss.bio.garden.repository.PlantRepository;
import com.github.paulinagazwa.oss.bio.garden.service.PlantCompanionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PlantCompanionServiceImpl implements PlantCompanionService {

	private final PlantCompanionRepository plantCompanionRepository;

	private final PlantRepository plantRepository;

	@Override
	public PlantCompanionEntity createCompanionRelationship(
			Long plantId,
			Long companionPlantId,
			RelationshipType relationshipType,
			Integer recommendedDistanceCm,
			Boolean bidirectional
	) {

		log.info("Creating companion relationship: plant={}, companion={}, type={}",
				plantId, companionPlantId, relationshipType);

		if (plantId.equals(companionPlantId)) {
			throw new PlantCompanionException();
		}

		PlantEntity plant = plantRepository.findById(plantId)
				.orElseThrow(() -> new PlantNotFoundException(plantId));

		PlantEntity companionPlant = plantRepository.findById(companionPlantId)
				.orElseThrow(() -> new PlantNotFoundException(companionPlantId));

		if (plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(plantId, companionPlantId, relationshipType)) {
			throw new PlantCompanionException(plantId, companionPlantId);
		}

		PlantCompanionEntity companion = PlantCompanionEntity.builder()
				.plant(plant)
				.companionPlant(companionPlant)
				.relationshipType(relationshipType)
				.recommendedDistanceCm(recommendedDistanceCm)
				.bidirectional(bidirectional != null ? bidirectional : true)
				.build();

		PlantCompanionEntity saved = plantCompanionRepository.save(companion);

		if (bidirectional == null || bidirectional) {
			if (!plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(companionPlantId, plantId, relationshipType)) {
				PlantCompanionEntity reverseCompanion = PlantCompanionEntity.builder()
						.plant(companionPlant)
						.companionPlant(plant)
						.relationshipType(relationshipType)
						.recommendedDistanceCm(recommendedDistanceCm)
						.bidirectional(true)
						.build();
				plantCompanionRepository.save(reverseCompanion);
				log.info("Created bidirectional companion relationship");
			}
		}

		log.info("Companion relationship created with id: {}", saved.getId());
		return saved;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlantCompanionEntity> getCompanionsForPlant(Long plantId) {

		log.debug("Getting all companions for plant: {}", plantId);
		PlantEntity plant = plantRepository.findById(plantId)
				.orElseThrow(() -> new PlantNotFoundException(plantId));
		return plantCompanionRepository.findByPlant(plant);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlantCompanionEntity> getCompanionsByType(Long plantId, RelationshipType relationshipType) {

		log.debug("Getting companions of type {} for plant: {}", relationshipType, plantId);
		PlantEntity plant = plantRepository.findById(plantId)
				.orElseThrow(() -> new PlantNotFoundException(plantId));
		return plantCompanionRepository.findByPlantAndRelationshipType(plant, relationshipType);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlantCompanionEntity> getGoodCompanions(Long plantId) {

		log.debug("Getting good companions for plant: {}", plantId);
		return plantCompanionRepository.findByPlantIdAndRelationshipType(plantId, RelationshipType.GOOD);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlantCompanionEntity> getBadCompanions(Long plantId) {

		log.debug("Getting bad companions for plant: {}", plantId);
		return plantCompanionRepository.findByPlantIdAndRelationshipType(plantId, RelationshipType.BAD);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlantCompanionEntity> getCompanionRowPlants(Long plantId) {

		log.debug("Getting companion row plants for plant: {}", plantId);
		return plantCompanionRepository.findByPlantIdAndRelationshipType(plantId, RelationshipType.COMPANION_ROW);
	}

	@Override
	public void deleteCompanionRelationship(Long id) {

		log.info("Deleting companion relationship: {}", id);
		PlantCompanionEntity companion = plantCompanionRepository.findById(id)
				.orElseThrow(() -> new PlantCompanionException(id));

		if (Boolean.TRUE.equals(companion.getBidirectional())) {
			PlantCompanionEntity reverse = plantCompanionRepository.findByPlantIdAndCompanionPlantIdAndRelationshipType(
					companion.getCompanionPlant().getId(),
					companion.getPlant().getId(),
					companion.getRelationshipType()
			);
			if (reverse != null) {
				plantCompanionRepository.delete(reverse);
				log.info("Deleted reverse companion relationship");
			}
		}

		plantCompanionRepository.delete(companion);
		log.info("Companion relationship deleted");
	}

	@Override
	public PlantCompanionEntity updateCompanionRelationship(
			Long id,
			String effectDescription,
			Integer recommendedDistanceCm,
			Boolean bidirectional
	) {

		log.info("Updating companion relationship: {}", id);
		PlantCompanionEntity companion = plantCompanionRepository.findById(id)
				.orElseThrow(() -> new PlantCompanionException(id));

		if (recommendedDistanceCm != null) {
			companion.setRecommendedDistanceCm(recommendedDistanceCm);
		}
		if (bidirectional != null) {
			companion.setBidirectional(bidirectional);
		}

		PlantCompanionEntity updated = plantCompanionRepository.save(companion);
		log.info("Companion relationship updated");
		return updated;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlantCompanionEntity> getAllRelationshipsForPlant(Long plantId) {

		log.debug("Getting all relationships for plant: {}", plantId);
		return plantCompanionRepository.findByPlantIdOrCompanionPlantId(plantId, plantId);
	}
}

