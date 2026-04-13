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
		Integer recommendedDistanceCm = request.getRecommendedDistanceCm();
		Boolean bidirectional = request.getBidirectional();

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

		// TODO mapper
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
		return plantCompanionMapper.toModel(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlantCompanion> getCompanionsForPlant(Long plantId) {

		log.debug("Getting all companions for plant: {}", plantId);
		PlantEntity plant = plantRepository.findById(plantId)
				.orElseThrow(() -> new PlantNotFoundException(plantId));

		return plantCompanionRepository.findByPlant(plant)
				.stream()
				.map(plantCompanionMapper::toModel)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlantCompanion> getCompanionsByType(Long plantId, RelationshipType relationshipType) {

		log.debug("Getting companions of type {} for plant: {}", relationshipType, plantId);
		PlantEntity plant = plantRepository.findById(plantId)
				.orElseThrow(() -> new PlantNotFoundException(plantId));
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
	public PlantCompanion updateCompanionRelationship(Long plantId, CompanionUpdateRequest updateRequest) {

		Integer recommendedDistanceCm = updateRequest.getRecommendedDistanceCm();
		Boolean bidirectional = updateRequest.getBidirectional();

		log.info("Updating companion relationship: {}", plantId);
		PlantCompanionEntity companion = plantCompanionRepository.findById(plantId)
				.orElseThrow(() -> new PlantCompanionException(plantId));

		if (recommendedDistanceCm != null) {
			companion.setRecommendedDistanceCm(recommendedDistanceCm);
		}
		if (bidirectional != null) {
			companion.setBidirectional(bidirectional);
		}

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

