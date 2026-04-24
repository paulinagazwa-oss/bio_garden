package com.github.paulinagazwa.oss.bio.garden.controller;

import com.github.paulinagazwa.oss.bio.garden.model.CropType;
import com.github.paulinagazwa.oss.bio.garden.model.PageInfo;
import com.github.paulinagazwa.oss.bio.garden.model.Plant;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCreateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantPage;
import com.github.paulinagazwa.oss.bio.garden.model.PlantUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.service.PlantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlantControllerTest {

	public static final Long ID = 1L;

	@Mock
	private PlantService plantService;

	@InjectMocks
	private PlantController plantController;

	private Plant plant;

	@BeforeEach
	void setUp() {

		plant = new Plant();
	}

	@Test
	void shouldReturnAllPlants() {

		List<Plant> plants = List.of(new Plant(), new Plant());
		PageInfo pageInfo = new PageInfo(0, 20, 2, 1);
		PlantPage plantPage = new PlantPage(plants, pageInfo);

		when(plantService.findAllPlants(any(Pageable.class))).thenReturn(plantPage);

		PlantPage result = plantController.getAllPlants(0, 20, "name,asc").getBody();

		assertNotNull(result);
		assertEquals(plants, result.getContent());
		assertEquals(pageInfo, result.getPage());
		verify(plantService).findAllPlants(any(Pageable.class));
	}

	@Test
	void shouldReturnPlantById() {

		when(plantService.findPlantById(ID)).thenReturn(plant);

		Plant result = plantController.getPlantById(ID).getBody();

		assertEquals(plant, result);
		verify(plantService).findPlantById(ID);
	}

	@Test
	void shouldCreatePlant() {

		PlantCreateRequest request = getPlantCreateRequest();
		when(plantService.createPlant(request)).thenReturn(plant);

		Plant result = plantController.createPlant(request).getBody();

		assertEquals(plant, result);
		verify(plantService).createPlant(request);
	}

	@Test
	void shouldUpdatePlant() {

		PlantUpdateRequest request = new PlantUpdateRequest();
		when(plantService.updatePlant(any(), eq(request))).thenReturn(plant);

		Plant result = plantController.updatePlant(1L, request).getBody();

		assertEquals(plant, result);
		verify(plantService).updatePlant(1L, request);
	}

	@Test
	void shouldDeletePlant() {

		plantController.deletePlant(ID);

		verify(plantService).deletePlant(ID);
	}

	@Test
	void shouldReturnNullWhenPlantNotFound() {

		when(plantService.findPlantById(ID)).thenReturn(null);

		Plant result = plantController.getPlantById(ID).getBody();

		assertNull(result);
		verify(plantService).findPlantById(ID);
	}

	@Test
	void shouldReturnNullWhenUpdateReturnsNull() {

		PlantUpdateRequest request = new PlantUpdateRequest();
		when(plantService.updatePlant(any(), eq(request))).thenReturn(null);

		Plant result = plantController.updatePlant(1L, request).getBody();

		assertNull(result);
		verify(plantService).updatePlant(1L, request);
	}

	@Test
	void shouldReturnEmptyListWhenNoPlantsExist() {

		PageInfo pageInfo = new PageInfo(0, 20, 0, 0);
		PlantPage plantPage = new PlantPage(List.of(), pageInfo);

		when(plantService.findAllPlants(any(Pageable.class))).thenReturn(plantPage);

		PlantPage result = plantController.getAllPlants(0, 20, "name,asc").getBody();

		assertNotNull(result);
		assertEquals(List.of(), result.getContent());
		assertEquals(0, result.getPage().getTotalElements());
		verify(plantService).findAllPlants(any(Pageable.class));
	}

	@Test
	void shouldNotCreatePlantWhenServiceReturnsNull() {

		PlantCreateRequest request = getPlantCreateRequest();
		when(plantService.createPlant(request)).thenReturn(null);

		Plant result = plantController.createPlant(request).getBody();

		assertNull(result);
		verify(plantService).createPlant(request);
	}

	@Test
	void shouldNotThrowWhenDeletingNonExistentPlant() {

		Long nonExistentId = 999L;

		assertDoesNotThrow(() -> plantController.deletePlant(nonExistentId));
		verify(plantService).deletePlant(nonExistentId);
	}

	private PlantCreateRequest getPlantCreateRequest() {

		PlantCreateRequest request = new PlantCreateRequest();
		request.setCrop(CropType.FRUIT);
		request.setName("Tomato");
		return request;
	}
}
