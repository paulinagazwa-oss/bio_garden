package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantCompanionEntity;
import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.exception.PlantCompanionException;
import com.github.paulinagazwa.oss.bio.garden.exception.PlantNotFoundException;
import com.github.paulinagazwa.oss.bio.garden.mapper.PlantCompanionMapper;
import com.github.paulinagazwa.oss.bio.garden.model.CompanionRequest;
import com.github.paulinagazwa.oss.bio.garden.model.CompanionUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCompanion;
import com.github.paulinagazwa.oss.bio.garden.model.RelationshipType;
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

	private final PlantCompanionMapper plantCompanionMapper;

	@Override
	public PlantCompanion createCompanionRelationship(CompanionRequest request) {

		Long plantId = request.getPlantId();
		Long companionPlantId = request.getCompanionPlantId();
		RelationshipType relationshipType = request.getRelationshipType();

		log.info("Creating companion relationship: plant={}, companion={}, type={}",
				plantId, companionPlantId, relationshipType);

		if (plantId.equals(companionPlantId)) {
			throw new PlantCompanionException();
		}

		PlantEntity plant = findByPlantIdOrThrow(plantId);

		PlantEntity companionPlant = findByPlantIdOrThrow(companionPlantId);

		if (plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(plantId, companionPlantId, relationshipType)) {
			throw new PlantCompanionException(plantId, companionPlantId);
		}

		PlantCompanionEntity companion = plantCompanionMapper.fromCompanionRequest(request, plant, companionPlant);

		PlantCompanionEntity saved = plantCompanionRepository.save(companion);

		if (request.getBidirectional() == null || request.getBidirectional()) {
			createReverseRelationshipIfNotExists(request, companionPlant, plant, relationshipType);
		}

		log.info("Companion relationship created with id: {}", saved.getId());
		return plantCompanionMapper.toModel(saved);
	}

	private PlantEntity findByPlantIdOrThrow(Long plantId) {

		return plantRepository.findById(plantId)
				.orElseThrow(() -> new PlantNotFoundException(plantId));
	}

	private void createReverseRelationshipIfNotExists(CompanionRequest request, PlantEntity companionPlant, PlantEntity plant, RelationshipType relationshipType) {

		if (!plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(companionPlant.getId(), plant.getId(), relationshipType)) {
			PlantCompanionEntity reverseCompanion = plantCompanionMapper.fromCompanionRequest(request, companionPlant, plant);
			plantCompanionRepository.save(reverseCompanion);
			log.info("Created bidirectional companion relationship");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlantCompanion> getCompanionsForPlant(Long plantId) {

		log.debug("Getting all companions for plant: {}", plantId);
		PlantEntity plant = findByPlantIdOrThrow(plantId);

		return plantCompanionRepository.findByPlant(plant)
				.stream()
				.map(plantCompanionMapper::toModel)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlantCompanion> getCompanionsByType(Long plantId, RelationshipType relationshipType) {

		log.debug("Getting companions of type {} for plant: {}", relationshipType, plantId);
		PlantEntity plant = findByPlantIdOrThrow(plantId);
		return plantCompanionRepository.findByPlantAndRelationshipType(plant, relationshipType)
				.stream()
				.map(plantCompanionMapper::toModel)
				.toList();
	}

	@Override
	public void deleteCompanionRelationship(Long id) {

		log.info("Deleting companion relationship: {}", id);
		PlantCompanionEntity companion = plantCompanionRepository.findById(id)
				.orElseThrow(() -> new PlantCompanionException(id));

		if (companion.getBidirectional()) {
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
	public PlantCompanion updateCompanionRelationship(Long plantId, CompanionUpdateRequest updateRequest) {

		Integer recommendedDistanceCm = updateRequest.getRecommendedDistanceCm();
		Boolean bidirectional = updateRequest.getBidirectional();

		log.info("Updating companion relationship: {}", plantId);
		PlantCompanionEntity companion = plantCompanionRepository.findById(plantId)
				.orElseThrow(() -> new PlantCompanionException(plantId));

		plantCompanionMapper.updateEntityFromRequest(updateRequest, companion);

		PlantCompanionEntity updated = plantCompanionRepository.save(companion);
		log.info("Companion relationship updated");
		return plantCompanionMapper.toModel(updated);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlantCompanion> getAllRelationshipsForPlant(Long plantId) {

		log.debug("Getting all relationships for plant: {}", plantId);
		return plantCompanionRepository.findByPlantIdOrCompanionPlantId(plantId, plantId)
				.stream()
				.map(plantCompanionMapper::toModel)
				.toList();
	}
}

