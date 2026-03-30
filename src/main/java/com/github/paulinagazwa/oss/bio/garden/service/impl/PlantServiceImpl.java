package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.mapper.PlantMapper;
import com.github.paulinagazwa.oss.bio.garden.model.Plant;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCreateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.repository.PlantRepository;
import com.github.paulinagazwa.oss.bio.garden.service.PlantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
	public Plant findPlantById(Long id) {

		return plantRepository.findById(id)
				.map(plantMapper::toModel)
				.orElse(null);
	}

	@Override
	public Plant createPlant(PlantCreateRequest plantCreateRequest) {

		return plantMapper.toModel(
				plantRepository.save(
						plantMapper.fromCreateRequest(plantCreateRequest)
				)
		);
	}

	@Override
	public Plant updatePlant(PlantUpdateRequest plant) {

		return plantMapper.toModel(
				plantRepository.save(
						plantMapper.fromUpdateRequest(plant)
				)
		);
	}

	@Override
	public void deletePlant(Long id) {

		plantRepository.delete(
				plantRepository.findById(id)
						.orElseThrow(() -> new RuntimeException("Plant not found with id: " + id))
		);
	}
}
