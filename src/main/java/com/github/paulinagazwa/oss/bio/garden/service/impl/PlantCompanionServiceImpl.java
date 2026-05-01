package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantCompanionEntity;
import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.exception.PlantCompanionException;
import com.github.paulinagazwa.oss.bio.garden.exception.PlantNotFoundException;
import com.github.paulinagazwa.oss.bio.garden.logging.LogMessages;
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

		log.info(LogMessages.COMPANION_CREATE_START,
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

		log.info(LogMessages.COMPANION_CREATE_SUCCESS, saved.getId());
		return plantCompanionMapper.toModel(saved);
	}

	private PlantEntity findByPlantIdOrThrow(Long plantId) {

		return plantRepository.findById(plantId)
				.orElseThrow(() -> new PlantNotFoundException(plantId));
	}

	private void createReverseRelationshipIfNotExists(Object request, PlantEntity companionPlant, PlantEntity plant, RelationshipType relationshipType) {

		if (!plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(companionPlant.getId(), plant.getId(), relationshipType)) {
			PlantCompanionEntity reverseCompanion = (request instanceof CompanionRequest)
					? plantCompanionMapper.fromCompanionRequest((CompanionRequest) request, companionPlant, plant)
					: plantCompanionMapper.fromCompanionUpdateRequest((CompanionUpdateRequest) request, companionPlant, plant);
			plantCompanionRepository.save(reverseCompanion);
			log.info(LogMessages.COMPANION_BIDIRECTIONAL_CREATED);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlantCompanion> getCompanionsForPlant(Long plantId) {

		log.debug(LogMessages.COMPANION_GET_ALL_FOR_PLANT, plantId);
		PlantEntity plant = findByPlantIdOrThrow(plantId);

		return plantCompanionRepository.findByPlant(plant)
				.stream()
				.map(plantCompanionMapper::toModel)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlantCompanion> getCompanionsByType(Long plantId, RelationshipType relationshipType) {

		log.debug(LogMessages.COMPANION_GET_BY_TYPE, relationshipType, plantId);
		PlantEntity plant = findByPlantIdOrThrow(plantId);
		return plantCompanionRepository.findByPlantAndRelationshipType(plant, relationshipType)
				.stream()
				.map(plantCompanionMapper::toModel)
				.toList();
	}

	@Override
	public void deleteCompanionRelationship(Long id) {

		log.info(LogMessages.COMPANION_DELETE_START, id);
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
				log.info(LogMessages.COMPANION_REVERSE_DELETE);
			}
		}

		plantCompanionRepository.delete(companion);
		log.info(LogMessages.COMPANION_DELETE_SUCCESS);
	}

	@Override
	public PlantCompanion updateCompanionRelationship(Long plantId, CompanionUpdateRequest updateRequest) {

		log.info(LogMessages.COMPANION_UPDATE_START, plantId);
		PlantCompanionEntity companion = plantCompanionRepository.findById(plantId)
				.orElseThrow(() -> new PlantCompanionException(plantId));

		deleteReverseRelationshipIfBidirectionalDisabled(updateRequest, companion);

		if (shouldCreateReverseRelationship(updateRequest, companion)) {
			createReverseRelationshipIfNotExists(updateRequest, companion.getCompanionPlant(), companion.getPlant(), companion.getRelationshipType());
		}

		plantCompanionMapper.updateEntityFromRequest(updateRequest, companion);

		PlantCompanionEntity updated = plantCompanionRepository.save(companion);
		log.info(LogMessages.COMPANION_UPDATE_SUCCESS);
		return plantCompanionMapper.toModel(updated);
	}

	private boolean isBidirectionalBeingDisabled(CompanionUpdateRequest updateRequest, PlantCompanionEntity companion) {

		return companion.getBidirectional() && (updateRequest.getBidirectional() != null && !updateRequest.getBidirectional());
	}

	private static boolean shouldCreateReverseRelationship(CompanionUpdateRequest updateRequest, PlantCompanionEntity companion) {

		return !companion.getBidirectional() && (updateRequest.getBidirectional() == null || updateRequest.getBidirectional());
	}

	private void deleteReverseRelationshipIfBidirectionalDisabled(CompanionUpdateRequest updateRequest, PlantCompanionEntity companion) {

		if (isBidirectionalBeingDisabled(updateRequest, companion)) {
			PlantCompanionEntity reverse = plantCompanionRepository.findByPlantIdAndCompanionPlantIdAndRelationshipType(
					companion.getCompanionPlant().getId(),
					companion.getPlant().getId(),
					companion.getRelationshipType()
			);
			if (reverse != null) {
				plantCompanionRepository.delete(reverse);
				log.info(LogMessages.COMPANION_REVERSE_DELETE_ON_UPDATE);
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlantCompanion> getAllRelationshipsForPlant(Long plantId) {

		log.debug(LogMessages.COMPANION_GET_ALL_RELATIONSHIPS, plantId);
		return plantCompanionRepository.findByPlantIdOrCompanionPlantId(plantId, plantId)
				.stream()
				.map(plantCompanionMapper::toModel)
				.toList();
	}
}

