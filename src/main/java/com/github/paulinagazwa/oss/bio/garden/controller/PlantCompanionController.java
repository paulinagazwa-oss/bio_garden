package com.github.paulinagazwa.oss.bio.garden.controller;

import com.github.paulinagazwa.oss.bio.garden.model.CompanionRequest;
import com.github.paulinagazwa.oss.bio.garden.model.CompanionUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCompanion;
import com.github.paulinagazwa.oss.bio.garden.model.RelationshipType;
import com.github.paulinagazwa.oss.bio.garden.service.PlantCompanionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1/plant-companions")
@RequiredArgsConstructor
public class PlantCompanionController {

	private final PlantCompanionService plantCompanionService;

	@PostMapping
	public ResponseEntity<PlantCompanion> createCompanionRelationship(@RequestBody CompanionRequest request) {

		PlantCompanion companion = plantCompanionService.createCompanionRelationship(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(companion);
	}

	@GetMapping("/plant/{plantId}")
	public ResponseEntity<List<PlantCompanion>> getCompanionsForPlant(@PathVariable Long plantId) {

		List<PlantCompanion> companions = plantCompanionService.getCompanionsForPlant(plantId);
		return ResponseEntity.ok(companions);
	}

	@GetMapping("/plant/{plantId}/type/{relationshipType}")
	public ResponseEntity<List<PlantCompanion>> getCompanionsByType(@PathVariable Long plantId, @PathVariable RelationshipType relationshipType) {

		List<PlantCompanion> companions = plantCompanionService.getCompanionsByType(plantId, relationshipType);
		return ResponseEntity.ok(companions);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PlantCompanion> updateCompanionRelationship(@PathVariable Long id, @RequestBody CompanionUpdateRequest request) {

		PlantCompanion updated = plantCompanionService.updateCompanionRelationship(id, request);
		return ResponseEntity.ok(updated);
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCompanionRelationship(@PathVariable Long id) {

		plantCompanionService.deleteCompanionRelationship(id);
		return ResponseEntity.noContent().build();
	}
}

