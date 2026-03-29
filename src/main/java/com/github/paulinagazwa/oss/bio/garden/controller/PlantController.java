package com.github.paulinagazwa.oss.bio.garden.controller;

import com.github.paulinagazwa.oss.bio.garden.model.Plant;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCreateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.service.PlantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlantController {

	@Autowired
	private PlantService plantService;

	@GetMapping("/api/v1/plants")
	public List<Plant> getAllPlants() {

		return plantService.findAllPlants();
	}

	@GetMapping("/api/v1/plants/{id}")
	public Plant getPlantById(@PathVariable Long id) {

		return plantService.findPlantById(id);
	}

	@PostMapping("/api/v1/plants")
	public Plant createPlant(@Valid PlantCreateRequest plantCreateRequest) {

		return plantService.createPlant(plantCreateRequest);
	}

	@PutMapping("/api/v1/plants/{id}")
	public Plant updatePlant(@Valid PlantUpdateRequest plantUpdateRequest) {

		return plantService.updatePlant(plantUpdateRequest);
	}

	@DeleteMapping("/api/v1/plants/{id}")
	public void deletePlant(@PathVariable Long id) {

		plantService.deletePlant(id);
	}
}
