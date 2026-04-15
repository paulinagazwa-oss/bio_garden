package com.github.paulinagazwa.oss.bio.garden.controller;

import com.github.paulinagazwa.oss.bio.garden.model.Plant;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCreateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantWithCompanionsPage;
import com.github.paulinagazwa.oss.bio.garden.service.PlantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plants")
public class PlantController {

	@Autowired
	private PlantService plantService;

	// TODO: add pagination and filtering
	// TODO: add response entities with proper status codes
	@GetMapping()
	public List<Plant> getAllPlants() {

		return plantService.findAllPlants();
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
