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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@GetMapping()
	public ResponseEntity<PlantPage> getAllPlants(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size,
			@RequestParam(defaultValue = "name,asc") String sort) {

		String[] sortParams = sort.split(",");
		String sortField = sortParams[0];
		Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
			? Sort.Direction.DESC
			: Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
		return ResponseEntity.ok(plantService.findAllPlants(pageable));
	}

	@GetMapping("/with-companions")
	public ResponseEntity<PlantWithCompanionsPage> getAllPlantsWithCompanions() {

		return ResponseEntity.ok(plantService.findAllPlantsWithCompanions());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Plant> getPlantById(@PathVariable Long id) {

		return ResponseEntity.ok(plantService.findPlantById(id));
	}

	@PostMapping()
	public ResponseEntity<Plant> createPlant(@Valid @RequestBody PlantCreateRequest plantCreateRequest) {

		return ResponseEntity.status(HttpStatus.CREATED).body(plantService.createPlant(plantCreateRequest));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Plant> updatePlant(@PathVariable Long id, @Valid @RequestBody PlantUpdateRequest plantUpdateRequest) {

		return ResponseEntity.ok(plantService.updatePlant(id, plantUpdateRequest));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePlant(@PathVariable Long id) {

		plantService.deletePlant(id);
		return ResponseEntity.noContent().build();
	}
}
