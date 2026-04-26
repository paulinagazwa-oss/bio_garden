package com.github.paulinagazwa.oss.bio.garden.controller;

import com.github.paulinagazwa.oss.bio.garden.model.CropType;
import com.github.paulinagazwa.oss.bio.garden.model.PageInfo;
import com.github.paulinagazwa.oss.bio.garden.model.Plant;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCreateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantPage;
import com.github.paulinagazwa.oss.bio.garden.model.PlantUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantWithCompanionsPage;
import com.github.paulinagazwa.oss.bio.garden.service.PlantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;

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

	public static final String NAME_ASC = "name,asc";

	public static final String GET_PAGEABLE = "getPageable";

	public static final String NAME = "name";

	public static final String NAME_DESC = "name,desc";

	public static final String NAME_UP = "name,up";

	public static final String CROP_DESC = "crop,desc";

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

		PlantPage result = plantController.getAllPlants(0, 20, NAME_ASC).getBody();

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

		PlantPage result = plantController.getAllPlants(0, 20, NAME_ASC).getBody();

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

	@Test
	void shouldReturnAllPlantsWithCompanions() {

		PlantWithCompanionsPage page = new PlantWithCompanionsPage();
		when(plantService.findAllPlantsWithCompanions(any(Pageable.class))).thenReturn(page);

		PlantWithCompanionsPage result = plantController.getAllPlantsWithCompanions(0, 20, NAME_ASC).getBody();

		assertEquals(page, result);
		verify(plantService).findAllPlantsWithCompanions(any(Pageable.class));
	}

	@Test
	void shouldUseAscendingSortWhenSortIsNameAsc() throws Exception {

		var method = PlantController.class.getDeclaredMethod(GET_PAGEABLE, int.class, int.class, String.class);
		method.setAccessible(true);

		Pageable pageable = (Pageable) method.invoke(plantController, 2, 50, NAME_ASC);

		assertEquals(2, pageable.getPageNumber());
		assertEquals(50, pageable.getPageSize());
		assertNotNull(pageable.getSort().getOrderFor(NAME));
		assertEquals(Sort.Direction.ASC, Objects.requireNonNull(pageable.getSort().getOrderFor(NAME)).getDirection());
	}

	@Test
	void shouldUseDescendingSortWhenSortIsNameDesc() throws Exception {

		var method = PlantController.class.getDeclaredMethod(GET_PAGEABLE, int.class, int.class, String.class);
		method.setAccessible(true);

		Pageable pageable = (Pageable) method.invoke(plantController, 0, 20, NAME_DESC);

		assertEquals(0, pageable.getPageNumber());
		assertEquals(20, pageable.getPageSize());
		assertNotNull(pageable.getSort().getOrderFor(NAME));
		assertEquals(Sort.Direction.DESC, Objects.requireNonNull(pageable.getSort().getOrderFor(NAME)).getDirection());
	}

	@Test
	void shouldDefaultToAscendingWhenDirectionIsMissing() throws Exception {

		var method = PlantController.class.getDeclaredMethod(GET_PAGEABLE, int.class, int.class, String.class);
		method.setAccessible(true);

		Pageable pageable = (Pageable) method.invoke(plantController, 1, 10, NAME);

		assertEquals(1, pageable.getPageNumber());
		assertEquals(10, pageable.getPageSize());
		assertNotNull(pageable.getSort().getOrderFor(NAME));
		assertEquals(Sort.Direction.ASC, Objects.requireNonNull(pageable.getSort().getOrderFor(NAME)).getDirection());
	}

	@Test
	void shouldDefaultToAscendingWhenDirectionIsNotDesc() throws Exception {

		var method = PlantController.class.getDeclaredMethod(GET_PAGEABLE, int.class, int.class, String.class);
		method.setAccessible(true);

		Pageable pageable = (Pageable) method.invoke(plantController, 1, 10, NAME_UP);

		assertEquals(1, pageable.getPageNumber());
		assertEquals(10, pageable.getPageSize());
		assertNotNull(pageable.getSort().getOrderFor(NAME));
		assertEquals(Sort.Direction.ASC, Objects.requireNonNull(pageable.getSort().getOrderFor(NAME)).getDirection());
	}

	@Test
	void shouldDefaultToNameAscWhenSortIsBlank() throws Exception {

		var method = PlantController.class.getDeclaredMethod(GET_PAGEABLE, int.class, int.class, String.class);
		method.setAccessible(true);

		Pageable pageable = (Pageable) method.invoke(plantController, 3, 5, "   ");

		assertEquals(3, pageable.getPageNumber());
		assertEquals(5, pageable.getPageSize());
		assertNotNull(pageable.getSort().getOrderFor(NAME));
		assertEquals(Sort.Direction.ASC, Objects.requireNonNull(pageable.getSort().getOrderFor(NAME)).getDirection());
	}

	@Test
	void shouldUseProvidedSortFieldWhenSortIsOtherField() throws Exception {

		var method = PlantController.class.getDeclaredMethod(GET_PAGEABLE, int.class, int.class, String.class);
		method.setAccessible(true);

		Pageable pageable = (Pageable) method.invoke(plantController, 0, 25, CROP_DESC);

		assertEquals(0, pageable.getPageNumber());
		assertEquals(25, pageable.getPageSize());
		assertNotNull(pageable.getSort().getOrderFor("crop"));
		assertEquals(Sort.Direction.DESC, Objects.requireNonNull(pageable.getSort().getOrderFor("crop")).getDirection());
	}

	private PlantCreateRequest getPlantCreateRequest() {

		PlantCreateRequest request = new PlantCreateRequest();
		request.setCrop(CropType.FRUIT);
		request.setName("Tomato");
		return request;
	}
}
