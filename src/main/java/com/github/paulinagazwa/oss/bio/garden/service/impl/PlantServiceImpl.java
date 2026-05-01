package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantCompanionEntity;
import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.exception.PlantNotFoundException;
import com.github.paulinagazwa.oss.bio.garden.logging.LogMessages;
import com.github.paulinagazwa.oss.bio.garden.mapper.PlantMapper;
import com.github.paulinagazwa.oss.bio.garden.model.PageInfo;
import com.github.paulinagazwa.oss.bio.garden.model.Plant;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCreateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantPage;
import com.github.paulinagazwa.oss.bio.garden.model.PlantUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantWithCompanions;
import com.github.paulinagazwa.oss.bio.garden.model.PlantWithCompanionsPage;
import com.github.paulinagazwa.oss.bio.garden.repository.PlantRepository;
import com.github.paulinagazwa.oss.bio.garden.service.PlantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PlantServiceImpl implements PlantService {

	private final PlantRepository plantRepository;

	private final PlantMapper plantMapper;

	@Override
	public PlantPage findAllPlants(Pageable pageable) {

		log.debug(LogMessages.PLANT_GET_ALL, pageable.getPageNumber(), pageable.getPageSize());

		Page<PlantEntity> page = plantRepository.findAll(pageable);

		List<Plant> plants = page.getContent().stream()
				.map(plantMapper::toModel)
				.toList();

		PageInfo pageInfo = createPageInfo(page);

		return new PlantPage()
				.content(plants)
				.page(pageInfo);
	}

	private PageInfo createPageInfo(Page<PlantEntity> page) {

		return new PageInfo(
				page.getNumber(),
				page.getSize(),
				(int) page.getTotalElements(),
				page.getTotalPages()
		);
	}

	@Override
	public PlantWithCompanionsPage findAllPlantsWithCompanions(Pageable pageable) {

		log.debug(LogMessages.PLANT_GET_ALL_WITH_COMPANIONS, pageable.getPageNumber(), pageable.getPageSize());

		Page<PlantEntity> page = plantRepository.findAll(pageable);

		List<PlantWithCompanions> plants = page.getContent().stream()
				.map(plantMapper::toModelWithCompanions)
				.toList();

		PageInfo pageInfo = createPageInfo(page);

		return new PlantWithCompanionsPage()
				.content(plants)
				.page(pageInfo);
	}

	@Override
	public Plant findPlantById(Long id) {

		log.debug(LogMessages.PLANT_GET_BY_ID, id);

		return plantRepository.findById(id)
				.map(plantMapper::toModel)
				.orElse(null);
	}

	@Override
	public Plant createPlant(PlantCreateRequest plantCreateRequest) {

		log.info(LogMessages.PLANT_CREATE_START);

		PlantEntity entity = plantMapper.fromCreateRequest(plantCreateRequest);
		entity.setCreationDate(LocalDateTime.now());
		PlantEntity saved = plantRepository.save(entity);
		log.info(LogMessages.PLANT_CREATE_SUCCESS, saved.getId());
		return plantMapper.toModel(saved);
	}

	@Override
	public Plant updatePlant(Long id, PlantUpdateRequest plant) {

		log.info(LogMessages.PLANT_UPDATE_START, id);

		PlantEntity existing = plantRepository.findById(id)
				.orElseThrow(() -> new PlantNotFoundException(id));

		plantMapper.updateEntityFromRequest(plant, existing);
		existing.setLastUpdateDate(LocalDateTime.now());

		PlantEntity updated = plantRepository.save(existing);
		log.info(LogMessages.PLANT_UPDATE_SUCCESS, updated.getId());
		return plantMapper.toModel(updated);
	}

	@Override
	public void deletePlant(Long id) {

		log.info(LogMessages.PLANT_DELETE_START, id);

		PlantEntity plant = plantRepository.findById(id)
				.orElseThrow(() -> new PlantNotFoundException(id));

		plant.getCompanions().forEach(this::clearRelation);
		plant.getCompanions().clear();

		plant.getCompanionFor().forEach(this::clearRelation);
		plant.getCompanionFor().clear();

		plantRepository.delete(plant);
		log.info(LogMessages.PLANT_DELETE_SUCCESS, id);
	}

	private void clearRelation(PlantCompanionEntity relation) {

		relation.setPlant(null);
		relation.setCompanionPlant(null);
	}
}
