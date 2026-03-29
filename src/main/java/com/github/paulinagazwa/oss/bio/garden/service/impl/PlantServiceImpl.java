package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.model.Plant;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCreateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.repository.PlantRepository;
import com.github.paulinagazwa.oss.bio.garden.service.PlantService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantServiceImpl implements PlantService {

	private PlantRepository plantRepository;

	@Override
	public List<Plant> findAllPlants() {

		return null;
	}

	@Override
	public Plant findPlantById(Long id) {

		return null;
	}

	@Override
	public Plant createPlant(PlantCreateRequest plantCreateRequest) {

		return null;
	}

	@Override
	public Plant updatePlant(PlantUpdateRequest plant) {

		return null;
	}

	@Override
	public void deletePlant(Long id) {

	}
}
