
package com.github.paulinagazwa.oss.bio.garden.controller;

import com.github.paulinagazwa.oss.bio.garden.model.CompanionRequest;
import com.github.paulinagazwa.oss.bio.garden.model.CompanionUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCompanion;
import com.github.paulinagazwa.oss.bio.garden.model.RelationshipType;
import com.github.paulinagazwa.oss.bio.garden.service.PlantCompanionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlantCompanionControllerTest {

	@Mock
	private PlantCompanionService plantCompanionService;

	@InjectMocks
	private PlantCompanionController plantCompanionController;

	@Test
	void shouldCreateCompanionRelationship() {

		CompanionRequest request = new CompanionRequest();
		PlantCompanion response = new PlantCompanion();

		when(plantCompanionService.createCompanionRelationship(request)).thenReturn(response);

		var result = plantCompanionController.createCompanionRelationship(request);

		assertEquals(HttpStatus.CREATED, result.getStatusCode());
		assertEquals(response, result.getBody());
		verify(plantCompanionService).createCompanionRelationship(request);
	}

	@Test
	void shouldReturnNullBodyWhenCreateCompanionRelationshipReturnsNull() {

		CompanionRequest request = new CompanionRequest();

		when(plantCompanionService.createCompanionRelationship(request)).thenReturn(null);

		var result = plantCompanionController.createCompanionRelationship(request);

		assertEquals(HttpStatus.CREATED, result.getStatusCode());
		assertNull(result.getBody());
		verify(plantCompanionService).createCompanionRelationship(request);
	}

	@Test
	void shouldReturnCompanionsForPlant() {

		Long plantId = 1L;
		List<PlantCompanion> companions = List.of(new PlantCompanion(), new PlantCompanion());

		when(plantCompanionService.getCompanionsForPlant(plantId)).thenReturn(companions);

		var result = plantCompanionController.getCompanionsForPlant(plantId);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertNotNull(result.getBody());
		assertEquals(companions, result.getBody());
		verify(plantCompanionService).getCompanionsForPlant(plantId);
	}

	@Test
	void shouldReturnEmptyListWhenNoCompanionsForPlant() {

		Long plantId = 1L;
		List<PlantCompanion> companions = List.of();

		when(plantCompanionService.getCompanionsForPlant(plantId)).thenReturn(companions);

		var result = plantCompanionController.getCompanionsForPlant(plantId);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertNotNull(result.getBody());
		assertEquals(List.of(), result.getBody());
		verify(plantCompanionService).getCompanionsForPlant(plantId);
	}

	@Test
	void shouldReturnNullBodyWhenServiceReturnsNullForGetCompanionsForPlant() {

		Long plantId = 1L;

		when(plantCompanionService.getCompanionsForPlant(plantId)).thenReturn(null);

		var result = plantCompanionController.getCompanionsForPlant(plantId);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertNull(result.getBody());
		verify(plantCompanionService).getCompanionsForPlant(plantId);
	}

	@Test
	void shouldReturnCompanionsByType() {

		Long plantId = 1L;
		RelationshipType relationshipType = RelationshipType.GOOD;
		List<PlantCompanion> companions = List.of(new PlantCompanion());

		when(plantCompanionService.getCompanionsByType(plantId, relationshipType)).thenReturn(companions);

		var result = plantCompanionController.getCompanionsByType(plantId, relationshipType);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertNotNull(result.getBody());
		assertEquals(companions, result.getBody());
		verify(plantCompanionService).getCompanionsByType(plantId, relationshipType);
	}

	@Test
	void shouldReturnNullBodyWhenServiceReturnsNullForGetCompanionsByType() {

		Long plantId = 1L;
		RelationshipType relationshipType = RelationshipType.BAD;

		when(plantCompanionService.getCompanionsByType(plantId, relationshipType)).thenReturn(null);

		var result = plantCompanionController.getCompanionsByType(plantId, relationshipType);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertNull(result.getBody());
		verify(plantCompanionService).getCompanionsByType(plantId, relationshipType);
	}

	@Test
	void shouldUpdateCompanionRelationship() {

		Long id = 10L;
		CompanionUpdateRequest request = new CompanionUpdateRequest();
		PlantCompanion updated = new PlantCompanion();

		when(plantCompanionService.updateCompanionRelationship(id, request)).thenReturn(updated);

		var result = plantCompanionController.updateCompanionRelationship(id, request);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(updated, result.getBody());
		verify(plantCompanionService).updateCompanionRelationship(id, request);
	}

	@Test
	void shouldReturnNullBodyWhenUpdateCompanionRelationshipReturnsNull() {

		Long id = 10L;
		CompanionUpdateRequest request = new CompanionUpdateRequest();

		when(plantCompanionService.updateCompanionRelationship(id, request)).thenReturn(null);

		var result = plantCompanionController.updateCompanionRelationship(id, request);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertNull(result.getBody());
		verify(plantCompanionService).updateCompanionRelationship(id, request);
	}

	@Test
	void shouldDeleteCompanionRelationship() {

		Long id = 10L;

		var result = plantCompanionController.deleteCompanionRelationship(id);

		assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
		verify(plantCompanionService).deleteCompanionRelationship(id);
	}
}
