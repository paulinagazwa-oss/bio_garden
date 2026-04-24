package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.exception.PlantNotFoundException;
import com.github.paulinagazwa.oss.bio.garden.mapper.PlantMapper;
import com.github.paulinagazwa.oss.bio.garden.model.Plant;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCreateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantPage;
import com.github.paulinagazwa.oss.bio.garden.model.PlantUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.repository.PlantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlantServiceImplTest {

	@Mock
	private PlantRepository plantRepository;

	@Mock
	private PlantMapper plantMapper;

	@InjectMocks
	private PlantServiceImpl plantService;

	@Test
	void findAllPlants_returnsAllMappedPlants() {

		PlantEntity entity1 = new PlantEntity();
		PlantEntity entity2 = new PlantEntity();
		Plant plant1 = new Plant();
		Plant plant2 = new Plant();

		Pageable pageable = PageRequest.of(0, 20);
		Page<PlantEntity> page = new PageImpl<>(List.of(entity1, entity2), pageable, 2);

		when(plantRepository.findAll(pageable)).thenReturn(page);
		when(plantMapper.toModel(entity1)).thenReturn(plant1);
		when(plantMapper.toModel(entity2)).thenReturn(plant2);

		PlantPage result = plantService.findAllPlants(pageable);

		assertThat(result.getContent()).containsExactly(plant1, plant2);
		assertThat(result.getPage().getTotalElements()).isEqualTo(2);
		assertThat(result.getPage().getTotalPages()).isEqualTo(1);
	}

	@Test
	void findAllPlants_returnsEmptyList_whenNoPlantsExist() {

		Pageable pageable = PageRequest.of(0, 20);
		Page<PlantEntity> page = new PageImpl<>(List.of(), pageable, 0);

		when(plantRepository.findAll(pageable)).thenReturn(page);

		PlantPage result = plantService.findAllPlants(pageable);

		assertThat(result.getContent()).isEmpty();
		assertThat(result.getPage().getTotalElements()).isEqualTo(0);
	}

	@Test
	void findPlantById_returnsPlant_whenExists() {

		PlantEntity entity = new PlantEntity();
		Plant plant = new Plant();

		when(plantRepository.findById(1L)).thenReturn(Optional.of(entity));
		when(plantMapper.toModel(entity)).thenReturn(plant);

		Plant result = plantService.findPlantById(1L);

		assertThat(result).isEqualTo(plant);
	}

	@Test
	void findPlantById_returnsNull_whenNotFound() {

		when(plantRepository.findById(99L)).thenReturn(Optional.empty());

		Plant result = plantService.findPlantById(99L);

		assertThat(result).isNull();
	}

	@Test
	void createPlant_savesEntityWithCreationDate() {

		PlantCreateRequest request = new PlantCreateRequest();
		PlantEntity entity = new PlantEntity();
		Plant plant = new Plant();

		when(plantMapper.fromCreateRequest(request)).thenReturn(entity);
		when(plantRepository.save(entity)).thenReturn(entity);
		when(plantMapper.toModel(entity)).thenReturn(plant);

		Plant result = plantService.createPlant(request);

		assertThat(result).isEqualTo(plant);
		assertThat(entity.getCreationDate()).isNotNull();
	}

	@Test
	void createPlant_setsCreationDateBeforeSaving() {

		PlantCreateRequest request = new PlantCreateRequest();
		PlantEntity entity = new PlantEntity();

		when(plantMapper.fromCreateRequest(request)).thenReturn(entity);
		when(plantRepository.save(any())).thenReturn(entity);
		when(plantMapper.toModel(entity)).thenReturn(new Plant());

		plantService.createPlant(request);

		ArgumentCaptor<PlantEntity> captor = ArgumentCaptor.forClass(PlantEntity.class);
		verify(plantRepository).save(captor.capture());
		assertThat(captor.getValue().getCreationDate()).isNotNull();
	}

	@Test
	void updatePlant_savesEntityWithLastUpdateDate() {

		PlantUpdateRequest request = new PlantUpdateRequest();
		PlantEntity entity = new PlantEntity();
		Plant plant = new Plant();

		when(plantRepository.findById(1L)).thenReturn(Optional.of(entity));
		doNothing().when(plantMapper).updateEntityFromRequest(eq(request), any(PlantEntity.class));
		when(plantRepository.save(entity)).thenReturn(entity);
		when(plantMapper.toModel(entity)).thenReturn(plant);

		Plant result = plantService.updatePlant(1L, request);

		assertThat(result).isEqualTo(plant);
		assertThat(entity.getLastUpdateDate()).isNotNull();
	}

	@Test
	void updatePlant_setsLastUpdateDateBeforeSaving() {

		PlantUpdateRequest request = new PlantUpdateRequest();
		PlantEntity entity = new PlantEntity();

		when(plantRepository.findById(1L)).thenReturn(Optional.of(entity));
		doNothing().when(plantMapper).updateEntityFromRequest(eq(request), any(PlantEntity.class));
		when(plantRepository.save(any())).thenReturn(entity);
		when(plantMapper.toModel(entity)).thenReturn(new Plant());

		plantService.updatePlant(1L, request);

		ArgumentCaptor<PlantEntity> captor = ArgumentCaptor.forClass(PlantEntity.class);
		verify(plantRepository).save(captor.capture());
		assertThat(captor.getValue().getLastUpdateDate()).isNotNull();
	}

	@Test
	void updatePlant_throwsException_whenNotFound() {

		when(plantRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> plantService.updatePlant(99L, new PlantUpdateRequest()))
				.isInstanceOf(PlantNotFoundException.class)
				.hasMessageContaining("99");
	}

	@Test
	void deletePlant_deletesEntity_whenFound() {

		PlantEntity entity = new PlantEntity();

		when(plantRepository.findById(1L)).thenReturn(Optional.of(entity));

		plantService.deletePlant(1L);

		verify(plantRepository).delete(entity);
	}

	@Test
	void deletePlant_throwsException_whenNotFound() {

		when(plantRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> plantService.deletePlant(99L))
				.isInstanceOf(PlantNotFoundException.class)
				.hasMessageContaining("99");
	}
}
