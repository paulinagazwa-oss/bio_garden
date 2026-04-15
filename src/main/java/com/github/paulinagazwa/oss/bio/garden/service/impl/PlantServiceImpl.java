package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.exception.PlantNotFoundException;
import com.github.paulinagazwa.oss.bio.garden.mapper.PlantMapper;
import com.github.paulinagazwa.oss.bio.garden.model.Plant;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCreateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantWithCompanionsPage;
import com.github.paulinagazwa.oss.bio.garden.repository.PlantRepository;
import com.github.paulinagazwa.oss.bio.garden.service.PlantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PlantServiceImpl implements PlantService {

	private final PlantRepository plantRepository;

	private final PlantMapper plantMapper;

	@Override
	public List<Plant> findAllPlants() {

		return plantRepository.findAll().stream()
				.map(plantMapper::toModel)
				.toList();
	}

	@Override
	public PlantWithCompanionsPage findAllPlantsWithCompanions() {

		//TODO implement pagination and filtering / use list / some mapper?
		// use plantRepository.findAllWithCompanions()
		return null;
	}

	@Override
	public Plant findPlantById(Long id) {

		return plantRepository.findById(id)
				.map(plantMapper::toModel)
				.orElse(null);
	}

	@Override
	public Plant createPlant(PlantCreateRequest plantCreateRequest) {

		PlantEntity entity = plantMapper.fromCreateRequest(plantCreateRequest);
		entity.setCreationDate(LocalDateTime.now());
		return plantMapper.toModel(plantRepository.save(entity));
	}

	@Override
	public Plant updatePlant(Long id, PlantUpdateRequest plant) {

		PlantEntity existing = plantRepository.findById(id)
				.orElseThrow(() -> new PlantNotFoundException(id));

		plantMapper.updateEntityFromRequest(plant, existing);
		existing.setLastUpdateDate(LocalDateTime.now());

		return plantMapper.toModel(plantRepository.save(existing));
	}

	@Override
	public void deletePlant(Long id) {

		//TODO remove relationships with companions before deleting the plant
		plantRepository.delete(
				plantRepository.findById(id)
						.orElseThrow(() -> new PlantNotFoundException(id))
		);
	}
}
