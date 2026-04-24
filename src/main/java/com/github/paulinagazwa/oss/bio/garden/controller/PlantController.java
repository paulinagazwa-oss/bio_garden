package com.github.paulinagazwa.oss.bio.garden.controller;

import com.github.paulinagazwa.oss.bio.garden.model.Plant;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCreateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantPage;
import com.github.paulinagazwa.oss.bio.garden.model.PlantUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantWithCompanionsPage;
import com.github.paulinagazwa.oss.bio.garden.service.PlantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/plants")
public class PlantController {

	@Autowired
	private PlantService plantService;

	// TODO: add response entities with proper status codes
	@GetMapping()
	public PlantPage getAllPlants(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size,
			@RequestParam(defaultValue = "name,asc") String sort) {

		String[] sortParams = sort.split(",");
		String sortField = sortParams[0];
		Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
			? Sort.Direction.DESC
			: Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
		return plantService.findAllPlants(pageable);
	}

	@GetMapping("/with-companions")
	public PlantWithCompanionsPage getAllPlantsWithCompanions() {

		return plantService.findAllPlantsWithCompanions();
	}

	@GetMapping("/{id}")
	public Plant getPlantById(@PathVariable Long id) {

		return plantService.findPlantById(id);
	}

	@PostMapping()
	public Plant createPlant(@Valid @RequestBody PlantCreateRequest plantCreateRequest) {

		return plantService.createPlant(plantCreateRequest);
	}

	@PutMapping("/{id}")
	public Plant updatePlant(@PathVariable Long id, @Valid @RequestBody PlantUpdateRequest plantUpdateRequest) {

		return plantService.updatePlant(id, plantUpdateRequest);
	}

	@DeleteMapping("/{id}")
	public void deletePlant(@PathVariable Long id) {

		plantService.deletePlant(id);
	}
}
