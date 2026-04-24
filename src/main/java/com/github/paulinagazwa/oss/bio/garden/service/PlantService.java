package com.github.paulinagazwa.oss.bio.garden.service;

import com.github.paulinagazwa.oss.bio.garden.model.Plant;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCreateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantPage;
import com.github.paulinagazwa.oss.bio.garden.model.PlantUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantWithCompanionsPage;
import org.springframework.data.domain.Pageable;


public interface PlantService {

	PlantPage findAllPlants(Pageable pageable);

	PlantWithCompanionsPage findAllPlantsWithCompanions();

	Plant findPlantById(Long id);

	Plant createPlant(PlantCreateRequest plantCreateRequest);

	Plant updatePlant(Long id, PlantUpdateRequest plantUpdateRequest);

	void deletePlant(Long id);
}
