package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantCompanionEntity;
import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.entity.RelationshipType;
import com.github.paulinagazwa.oss.bio.garden.exception.PlantNotFoundException;
import com.github.paulinagazwa.oss.bio.garden.repository.PlantCompanionRepository;
import com.github.paulinagazwa.oss.bio.garden.repository.PlantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlantCompanionServiceImplTest {

	@Mock
	private PlantCompanionRepository plantCompanionRepository;

	@Mock
	private PlantRepository plantRepository;

	@InjectMocks
	private PlantCompanionServiceImpl plantCompanionService;

	private PlantEntity plant1;

	private PlantEntity plant2;

	private PlantCompanionEntity companion;

	@BeforeEach
	void setUp() {

		plant1 = new PlantEntity();
		plant1.setId(1L);
		plant1.setName("Tomato");

		plant2 = new PlantEntity();
		plant2.setId(2L);
		plant2.setName("Basil");

		companion = PlantCompanionEntity.builder()
				.id(1L)
				.plant(plant1)
				.companionPlant(plant2)
				.relationshipType(RelationshipType.GOOD)
				.recommendedDistanceCm(30)
				.bidirectional(true)
				.build();
	}

	@Test
	void createCompanionRelationship_createsRelationshipAndReverseWhenBidirectionalTrue() {

		when(plantRepository.findById(1L)).thenReturn(Optional.of(plant1));
		when(plantRepository.findById(2L)).thenReturn(Optional.of(plant2));
		when(plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(1L, 2L, RelationshipType.GOOD))
				.thenReturn(false);
		when(plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(2L, 1L, RelationshipType.GOOD))
				.thenReturn(false);

		PlantCompanionEntity saved = PlantCompanionEntity.builder()
				.id(10L)
				.plant(plant1)
				.companionPlant(plant2)
				.relationshipType(RelationshipType.GOOD)
				.recommendedDistanceCm(30)
				.bidirectional(true)
				.build();
		when(plantCompanionRepository.save(any(PlantCompanionEntity.class))).thenReturn(saved);

		PlantCompanionEntity result = plantCompanionService.createCompanionRelationship(
				1L, 2L, RelationshipType.GOOD, 30, true
		);

		assertThat(result).isSameAs(saved);
		verify(plantCompanionRepository, times(2)).save(any(PlantCompanionEntity.class));
	}

	@Test
	void createCompanionRelationship_createsOnlyOneRelationshipWhenBidirectionalFalse() {

		when(plantRepository.findById(1L)).thenReturn(Optional.of(plant1));
		when(plantRepository.findById(2L)).thenReturn(Optional.of(plant2));
		when(plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(1L, 2L, RelationshipType.GOOD))
				.thenReturn(false);

		PlantCompanionEntity saved = PlantCompanionEntity.builder()
				.id(11L)
				.plant(plant1)
				.companionPlant(plant2)
				.relationshipType(RelationshipType.GOOD)
				.recommendedDistanceCm(25)
				.bidirectional(false)
				.build();
		when(plantCompanionRepository.save(any(PlantCompanionEntity.class))).thenReturn(saved);

		PlantCompanionEntity result = plantCompanionService.createCompanionRelationship(
				1L, 2L, RelationshipType.GOOD, 25, false
		);

		assertThat(result).isSameAs(saved);
		verify(plantCompanionRepository, times(1)).save(any(PlantCompanionEntity.class));
	}

	@Test
	void createCompanionRelationship_defaultsBidirectionalToTrueWhenNullAndCreatesReverse() {

		when(plantRepository.findById(1L)).thenReturn(Optional.of(plant1));
		when(plantRepository.findById(2L)).thenReturn(Optional.of(plant2));
		when(plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(1L, 2L, RelationshipType.GOOD))
				.thenReturn(false);

		PlantCompanionEntity saved = PlantCompanionEntity.builder()
				.id(12L)
				.plant(plant1)
				.companionPlant(plant2)
				.relationshipType(RelationshipType.GOOD)
				.recommendedDistanceCm(30)
				.bidirectional(true)
				.build();
		when(plantCompanionRepository.save(any(PlantCompanionEntity.class))).thenReturn(saved);

		PlantCompanionEntity result = plantCompanionService.createCompanionRelationship(
				1L, 2L, RelationshipType.GOOD, 30, null
		);

		assertThat(result).isSameAs(saved);
		verify(plantCompanionRepository, times(2)).save(any(PlantCompanionEntity.class));
	}

	@Test
	void createCompanionRelationship_throwsWhenSamePlantProvided() {

		assertThatThrownBy(() -> plantCompanionService.createCompanionRelationship(
				1L, 1L, RelationshipType.GOOD, 30, true
		)).isInstanceOf(RuntimeException.class);

		verify(plantRepository, times(0)).findById(any());
		verify(plantCompanionRepository, times(0)).save(any());
	}

	@Test
	void createCompanionRelationship_throwsWhenPlantNotFound() {

		when(plantRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> plantCompanionService.createCompanionRelationship(
				1L, 2L, RelationshipType.GOOD, 30, true
		)).isInstanceOf(PlantNotFoundException.class);

		verify(plantCompanionRepository, times(0)).save(any());
	}

	@Test
	void createCompanionRelationship_throwsWhenCompanionPlantNotFound() {

		when(plantRepository.findById(1L)).thenReturn(Optional.of(plant1));
		when(plantRepository.findById(2L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> plantCompanionService.createCompanionRelationship(
				1L, 2L, RelationshipType.GOOD, 30, true
		)).isInstanceOf(PlantNotFoundException.class);

		verify(plantCompanionRepository, times(0)).save(any());
	}

	@Test
	void createCompanionRelationship_throwsWhenRelationshipAlreadyExists() {

		when(plantRepository.findById(1L)).thenReturn(Optional.of(plant1));
		when(plantRepository.findById(2L)).thenReturn(Optional.of(plant2));
		when(plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(1L, 2L, RelationshipType.GOOD))
				.thenReturn(true);

		assertThatThrownBy(() -> plantCompanionService.createCompanionRelationship(
				1L, 2L, RelationshipType.GOOD, 30, true
		)).isInstanceOf(RuntimeException.class);

		verify(plantCompanionRepository, times(0)).save(any());
	}

	@Test
	void createCompanionRelationship_doesNotCreateReverseWhenReverseAlreadyExists() {

		when(plantRepository.findById(1L)).thenReturn(Optional.of(plant1));
		when(plantRepository.findById(2L)).thenReturn(Optional.of(plant2));
		when(plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(1L, 2L, RelationshipType.GOOD))
				.thenReturn(false);
		when(plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(2L, 1L, RelationshipType.GOOD))
				.thenReturn(true);

		PlantCompanionEntity saved = PlantCompanionEntity.builder()
				.id(13L)
				.plant(plant1)
				.companionPlant(plant2)
				.relationshipType(RelationshipType.GOOD)
				.recommendedDistanceCm(30)
				.bidirectional(true)
				.build();
		when(plantCompanionRepository.save(any(PlantCompanionEntity.class))).thenReturn(saved);

		PlantCompanionEntity result = plantCompanionService.createCompanionRelationship(
				1L, 2L, RelationshipType.GOOD, 30, true
		);

		assertThat(result).isSameAs(saved);
		verify(plantCompanionRepository, times(1)).save(any(PlantCompanionEntity.class));
	}

	@Test
	void getCompanionsForPlant_returnsCompanionsForExistingPlant() {

		when(plantRepository.findById(1L)).thenReturn(Optional.of(plant1));
		List<PlantCompanionEntity> companions = Collections.singletonList(companion);
		when(plantCompanionRepository.findByPlant(plant1)).thenReturn(companions);

		List<PlantCompanionEntity> result = plantCompanionService.getCompanionsForPlant(1L);

		assertThat(result).isEqualTo(companions);
	}

	@Test
	void getCompanionsForPlant_throwsWhenPlantNotFound() {

		when(plantRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> plantCompanionService.getCompanionsForPlant(1L))
				.isInstanceOf(PlantNotFoundException.class);
	}

	@Test
	void getCompanionsByType_returnsOnlyRequestedRelationshipType() {

		when(plantRepository.findById(1L)).thenReturn(Optional.of(plant1));
		List<PlantCompanionEntity> companions = Collections.singletonList(companion);
		when(plantCompanionRepository.findByPlantAndRelationshipType(plant1, RelationshipType.GOOD)).thenReturn(companions);

		List<PlantCompanionEntity> result = plantCompanionService.getCompanionsByType(1L, RelationshipType.GOOD);

		assertThat(result).isEqualTo(companions);
	}

	@Test
	void getGoodCompanions_returnsGoodCompanions() {

		List<PlantCompanionEntity> companions = Collections.singletonList(companion);
		when(plantCompanionRepository.findByPlantIdAndRelationshipType(1L, RelationshipType.GOOD)).thenReturn(companions);

		List<PlantCompanionEntity> result = plantCompanionService.getGoodCompanions(1L);

		assertThat(result).isEqualTo(companions);
	}

	@Test
	void getBadCompanions_returnsBadCompanions() {

		when(plantCompanionRepository.findByPlantIdAndRelationshipType(1L, RelationshipType.BAD))
				.thenReturn(List.of());

		List<PlantCompanionEntity> result = plantCompanionService.getBadCompanions(1L);

		assertThat(result).isEmpty();
	}

	@Test
	void getCompanionRowPlants_returnsCompanionRowPlants() {

		when(plantCompanionRepository.findByPlantIdAndRelationshipType(1L, RelationshipType.COMPANION_ROW))
				.thenReturn(List.of(companion));

		List<PlantCompanionEntity> result = plantCompanionService.getCompanionRowPlants(1L);

		assertThat(result).hasSize(1);
	}

	@Test
	void deleteCompanionRelationship_deletesBothSidesWhenBidirectionalTrueAndReverseExists() {

		PlantCompanionEntity reverse = PlantCompanionEntity.builder()
				.id(2L)
				.plant(plant2)
				.companionPlant(plant1)
				.relationshipType(RelationshipType.GOOD)
				.recommendedDistanceCm(30)
				.bidirectional(true)
				.build();

		when(plantCompanionRepository.findById(1L)).thenReturn(Optional.of(companion));
		when(plantCompanionRepository.findByPlantIdAndCompanionPlantIdAndRelationshipType(2L, 1L, RelationshipType.GOOD))
				.thenReturn(reverse);

		plantCompanionService.deleteCompanionRelationship(1L);

		verify(plantCompanionRepository, times(1)).delete(reverse);
		verify(plantCompanionRepository, times(1)).delete(companion);
	}

	@Test
	void deleteCompanionRelationship_deletesOnlyOneSideWhenBidirectionalFalse() {

		companion.setBidirectional(false);
		when(plantCompanionRepository.findById(1L)).thenReturn(Optional.of(companion));

		plantCompanionService.deleteCompanionRelationship(1L);

		verify(plantCompanionRepository, times(0))
				.findByPlantIdAndCompanionPlantIdAndRelationshipType(any(), any(), any());
		verify(plantCompanionRepository, times(1)).delete(companion);
	}

	@Test
	void deleteCompanionRelationship_deletesOnlyOneSideWhenBidirectionalTrueButReverseDoesNotExist() {

		when(plantCompanionRepository.findById(1L)).thenReturn(Optional.of(companion));
		when(plantCompanionRepository.findByPlantIdAndCompanionPlantIdAndRelationshipType(2L, 1L, RelationshipType.GOOD))
				.thenReturn(null);

		plantCompanionService.deleteCompanionRelationship(1L);

		verify(plantCompanionRepository, times(1)).delete(companion);
	}

	@Test
	void deleteCompanionRelationship_throwsWhenRelationshipNotFound() {

		when(plantCompanionRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> plantCompanionService.deleteCompanionRelationship(1L))
				.isInstanceOf(RuntimeException.class);

		verify(plantCompanionRepository, times(0)).delete(any());
	}

	@Test
	void updateCompanionRelationship_updatesProvidedFieldsOnly() {

		PlantCompanionEntity existing = PlantCompanionEntity.builder()
				.id(1L)
				.plant(plant1)
				.companionPlant(plant2)
				.relationshipType(RelationshipType.GOOD)
				.recommendedDistanceCm(30)
				.bidirectional(true)
				.build();

		when(plantCompanionRepository.findById(1L)).thenReturn(Optional.of(existing));
		when(plantCompanionRepository.save(any(PlantCompanionEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

		PlantCompanionEntity updated = plantCompanionService.updateCompanionRelationship(1L, null, 50, false);

		assertThat(updated.getRecommendedDistanceCm()).isEqualTo(50);
		assertThat(updated.getBidirectional()).isFalse();
	}

	@Test
	void updateCompanionRelationship_doesNotChangeFieldsWhenNullValuesProvided() {

		PlantCompanionEntity existing = PlantCompanionEntity.builder()
				.id(1L)
				.plant(plant1)
				.companionPlant(plant2)
				.relationshipType(RelationshipType.GOOD)
				.recommendedDistanceCm(30)
				.bidirectional(true)
				.build();

		when(plantCompanionRepository.findById(1L)).thenReturn(Optional.of(existing));
		when(plantCompanionRepository.save(any(PlantCompanionEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

		PlantCompanionEntity updated = plantCompanionService.updateCompanionRelationship(1L, null, null, null);

		assertThat(updated.getRecommendedDistanceCm()).isEqualTo(30);
		assertThat(updated.getBidirectional()).isTrue();
	}

	@Test
	void updateCompanionRelationship_throwsWhenRelationshipNotFound() {

		when(plantCompanionRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> plantCompanionService.updateCompanionRelationship(1L, null, 40, true))
				.isInstanceOf(RuntimeException.class);

		verify(plantCompanionRepository, times(0)).save(any());
	}

	@Test
	void getAllRelationshipsForPlant_returnsRelationshipsWherePlantOrCompanionMatches() {

		List<PlantCompanionEntity> relationships = Collections.singletonList(companion);
		when(plantCompanionRepository.findByPlantIdOrCompanionPlantId(1L, 1L)).thenReturn(relationships);

		List<PlantCompanionEntity> result = plantCompanionService.getAllRelationshipsForPlant(1L);

		assertThat(result).isEqualTo(relationships);
	}
}

