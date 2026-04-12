package com.github.paulinagazwa.oss.bio.garden.controller;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantCompanionEntity;
import com.github.paulinagazwa.oss.bio.garden.entity.RelationshipType;
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
	public ResponseEntity<PlantCompanionEntity> createCompanionRelationship(@RequestBody CompanionRequest request) {

		PlantCompanionEntity companion = plantCompanionService.createCompanionRelationship(
				request.plantId,
				request.companionPlantId,
				request.relationshipType,
				request.recommendedDistanceCm,
				request.bidirectional
		);
		return ResponseEntity.status(HttpStatus.CREATED).body(companion);
	}

	@GetMapping("/plant/{plantId}")
	public ResponseEntity<List<PlantCompanionEntity>> getCompanionsForPlant(@PathVariable Long plantId) {

		List<PlantCompanionEntity> companions = plantCompanionService.getCompanionsForPlant(plantId);
		return ResponseEntity.ok(companions);
	}

	@GetMapping("/plant/{plantId}/type/{relationshipType}")
	public ResponseEntity<List<PlantCompanionEntity>> getCompanionsByType(@PathVariable Long plantId, @PathVariable RelationshipType relationshipType) {

		List<PlantCompanionEntity> companions = plantCompanionService.getCompanionsByType(plantId, relationshipType);
		return ResponseEntity.ok(companions);
	}

	@GetMapping("/plant/{plantId}/good")
	public ResponseEntity<List<PlantCompanionEntity>> getGoodCompanions(
			@PathVariable Long plantId
	) {

		List<PlantCompanionEntity> companions = plantCompanionService.getGoodCompanions(plantId);
		return ResponseEntity.ok(companions);
	}

	@GetMapping("/plant/{plantId}/bad")
	public ResponseEntity<List<PlantCompanionEntity>> getBadCompanions(
			@PathVariable Long plantId
	) {

		List<PlantCompanionEntity> companions = plantCompanionService.getBadCompanions(plantId);
		return ResponseEntity.ok(companions);
	}

	@GetMapping("/plant/{plantId}/companion-row")
	public ResponseEntity<List<PlantCompanionEntity>> getCompanionRowPlants(@PathVariable Long plantId) {

		List<PlantCompanionEntity> companions = plantCompanionService.getCompanionRowPlants(plantId);
		return ResponseEntity.ok(companions);
	}

	@GetMapping("/plant/{plantId}/all")
	public ResponseEntity<List<PlantCompanionEntity>> getAllRelationshipsForPlant(@PathVariable Long plantId) {

		List<PlantCompanionEntity> companions = plantCompanionService.getAllRelationshipsForPlant(plantId);
		return ResponseEntity.ok(companions);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PlantCompanionEntity> updateCompanionRelationship(@PathVariable Long id, @RequestBody CompanionUpdateRequest request) {

		PlantCompanionEntity updated = plantCompanionService.updateCompanionRelationship(
				id,
				request.effectDescription,
				request.recommendedDistanceCm,
				request.bidirectional
		);
		return ResponseEntity.ok(updated);
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCompanionRelationship(@PathVariable Long id) {

		plantCompanionService.deleteCompanionRelationship(id);
		return ResponseEntity.noContent().build();
	}

	public record CompanionRequest(
			Long plantId,
			Long companionPlantId,
			RelationshipType relationshipType,
			String effectDescription,
			Integer recommendedDistanceCm,
			Boolean bidirectional
	) {

	}

	public record CompanionUpdateRequest(
			String effectDescription,
			Integer recommendedDistanceCm,
			Boolean bidirectional
	) {

	}
}

