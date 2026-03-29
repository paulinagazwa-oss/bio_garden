package com.github.paulinagazwa.oss.bio.garden.service;

import com.github.paulinagazwa.oss.bio.garden.model.Plant;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCreateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantUpdateRequest;

import java.util.List;

public interface PlantService {

	List<Plant> findAllPlants();

	Plant findPlantById(Long id);

	Plant createPlant(PlantCreateRequest plantCreateRequest);

	Plant updatePlant(PlantUpdateRequest plantUpdateRequest);

	void deletePlant(Long id);
}
