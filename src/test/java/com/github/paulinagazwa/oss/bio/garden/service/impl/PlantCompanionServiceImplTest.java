package com.github.paulinagazwa.oss.bio.garden.service.impl;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantCompanionEntity;
import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.exception.PlantCompanionException;
import com.github.paulinagazwa.oss.bio.garden.exception.PlantNotFoundException;
import com.github.paulinagazwa.oss.bio.garden.mapper.PlantCompanionMapper;
import com.github.paulinagazwa.oss.bio.garden.model.CompanionRequest;
import com.github.paulinagazwa.oss.bio.garden.model.CompanionUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCompanion;
import com.github.paulinagazwa.oss.bio.garden.model.RelationshipType;
import com.github.paulinagazwa.oss.bio.garden.repository.PlantCompanionRepository;
import com.github.paulinagazwa.oss.bio.garden.repository.PlantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlantCompanionServiceImplTest {

	@Mock
	private PlantCompanionRepository plantCompanionRepository;

	@Mock
	private PlantRepository plantRepository;

	@Mock
	private PlantCompanionMapper plantCompanionMapper;

	private PlantCompanionServiceImpl service;

	@BeforeEach
	void setUp() {

		service = new PlantCompanionServiceImpl(plantCompanionRepository, plantRepository, plantCompanionMapper);
	}

	@Test
	void createCompanionRelationship_createsSingleRelationship_whenBidirectionalIsFalse() {

		Long plantId = 1L;
		Long companionId = 2L;
		RelationshipType type = RelationshipType.values()[0];

		PlantEntity plant = mock(PlantEntity.class);
		PlantEntity companionPlant = mock(PlantEntity.class);

		CompanionRequest request = mock(CompanionRequest.class);
		when(request.getPlantId()).thenReturn(plantId);
		when(request.getCompanionPlantId()).thenReturn(companionId);
		when(request.getRelationshipType()).thenReturn(type);
		when(request.getBidirectional()).thenReturn(false);

		when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));
		when(plantRepository.findById(companionId)).thenReturn(Optional.of(companionPlant));
		when(plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(plantId, companionId, type))
				.thenReturn(false);

		PlantCompanionEntity savedEntity = PlantCompanionEntity.builder()
				.id(100L)
				.plant(plant)
				.companionPlant(companionPlant)
				.relationshipType(type)
				.recommendedDistanceCm(10)
				.bidirectional(false)
				.build();

		when(plantCompanionMapper.fromCompanionRequest(eq(request), eq(plant), eq(companionPlant))).thenReturn(savedEntity);
		when(plantCompanionRepository.save(any(PlantCompanionEntity.class))).thenAnswer(i -> i.getArgument(0));

		PlantCompanion mapped = mock(PlantCompanion.class);
		when(plantCompanionMapper.toModel(savedEntity)).thenReturn(mapped);

		PlantCompanion result = service.createCompanionRelationship(request);

		assertSame(mapped, result);

		ArgumentCaptor<PlantCompanionEntity> captor = ArgumentCaptor.forClass(PlantCompanionEntity.class);
		verify(plantCompanionRepository, times(1)).save(captor.capture());
		PlantCompanionEntity toSave = captor.getValue();
		assertSame(plant, toSave.getPlant());
		assertSame(companionPlant, toSave.getCompanionPlant());
		assertEquals(type, toSave.getRelationshipType());
		assertEquals(10, toSave.getRecommendedDistanceCm());
		assertEquals(false, toSave.getBidirectional());

		verify(plantCompanionRepository, never())
				.existsByPlantIdAndCompanionPlantIdAndRelationshipType(companionId, plantId, type);
	}

	@Test
	void createCompanionRelationship_createsBidirectionalRelationshipByDefault_whenBidirectionalIsNull() {

		Long plantId = 1L;
		Long companionId = 2L;
		RelationshipType type = RelationshipType.values()[0];

		PlantEntity plant = mock(PlantEntity.class);
		when(plant.getId()).thenReturn(plantId);
		PlantEntity companionPlant = mock(PlantEntity.class);
		when(companionPlant.getId()).thenReturn(companionId);

		CompanionRequest request = mock(CompanionRequest.class);
		when(request.getPlantId()).thenReturn(plantId);
		when(request.getCompanionPlantId()).thenReturn(companionId);
		when(request.getRelationshipType()).thenReturn(type);
		when(request.getBidirectional()).thenReturn(null);

		when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));
		when(plantRepository.findById(companionId)).thenReturn(Optional.of(companionPlant));
		when(plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(plantId, companionId, type))
				.thenReturn(false);
		when(plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(companionId, plantId, type))
				.thenReturn(false);

		PlantCompanionEntity forwardEntity = PlantCompanionEntity.builder()
				.id(101L)
				.plant(plant)
				.companionPlant(companionPlant)
				.relationshipType(type)
				.recommendedDistanceCm(25)
				.bidirectional(true)
				.build();

		PlantCompanionEntity reverseEntity = PlantCompanionEntity.builder()
				.id(102L)
				.plant(companionPlant)
				.companionPlant(plant)
				.relationshipType(type)
				.recommendedDistanceCm(25)
				.bidirectional(true)
				.build();

		when(plantCompanionMapper.fromCompanionRequest(eq(request), eq(plant), eq(companionPlant))).thenReturn(forwardEntity);
		when(plantCompanionMapper.fromCompanionRequest(eq(request), eq(companionPlant), eq(plant))).thenReturn(reverseEntity);
		when(plantCompanionRepository.save(any(PlantCompanionEntity.class))).thenAnswer(i -> i.getArgument(0));

		PlantCompanion mapped = mock(PlantCompanion.class);
		when(plantCompanionMapper.toModel(forwardEntity)).thenReturn(mapped);

		PlantCompanion result = service.createCompanionRelationship(request);

		assertSame(mapped, result);

		ArgumentCaptor<PlantCompanionEntity> captor = ArgumentCaptor.forClass(PlantCompanionEntity.class);
		verify(plantCompanionRepository, times(2)).save(captor.capture());
		List<PlantCompanionEntity> saved = captor.getAllValues();

		PlantCompanionEntity forward = saved.get(0);
		assertSame(plant, forward.getPlant());
		assertSame(companionPlant, forward.getCompanionPlant());
		assertEquals(type, forward.getRelationshipType());
		assertEquals(25, forward.getRecommendedDistanceCm());
		assertEquals(true, forward.getBidirectional());

		PlantCompanionEntity reverse = saved.get(1);
		assertSame(companionPlant, reverse.getPlant());
		assertSame(plant, reverse.getCompanionPlant());
		assertEquals(type, reverse.getRelationshipType());
		assertEquals(25, reverse.getRecommendedDistanceCm());
		assertEquals(true, reverse.getBidirectional());
	}


	@Test
	void createCompanionRelationship_doesNotCreateReverse_whenReverseAlreadyExists() {

		Long plantId = 1L;
		Long companionId = 2L;
		RelationshipType type = RelationshipType.values()[0];

		PlantEntity plant = mock(PlantEntity.class);
		when(plant.getId()).thenReturn(plantId);
		PlantEntity companionPlant = mock(PlantEntity.class);
		when(companionPlant.getId()).thenReturn(companionId);

		CompanionRequest request = mock(CompanionRequest.class);
		when(request.getPlantId()).thenReturn(plantId);
		when(request.getCompanionPlantId()).thenReturn(companionId);
		when(request.getRelationshipType()).thenReturn(type);
		when(request.getBidirectional()).thenReturn(true);

		when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));
		when(plantRepository.findById(companionId)).thenReturn(Optional.of(companionPlant));

		when(plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(plantId, companionId, type))
				.thenReturn(false);
		when(plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(companionId, plantId, type))
				.thenReturn(true);

		PlantCompanionEntity savedEntity = PlantCompanionEntity.builder().id(102L).build();
		when(plantCompanionMapper.fromCompanionRequest(eq(request), any(), any())).thenReturn(savedEntity);
		when(plantCompanionRepository.save(any(PlantCompanionEntity.class))).thenReturn(savedEntity);

		PlantCompanion mapped = mock(PlantCompanion.class);
		when(plantCompanionMapper.toModel(savedEntity)).thenReturn(mapped);

		PlantCompanion result = service.createCompanionRelationship(request);

		assertSame(mapped, result);
		verify(plantCompanionRepository, times(1)).save(any(PlantCompanionEntity.class));
	}

	@Test
	void createCompanionRelationship_throwsException_whenPlantEqualsCompanionPlant() {

		Long plantId = 1L;

		CompanionRequest request = mock(CompanionRequest.class);
		when(request.getPlantId()).thenReturn(plantId);
		when(request.getCompanionPlantId()).thenReturn(plantId);
		when(request.getRelationshipType()).thenReturn(RelationshipType.values()[0]);

		assertThrows(PlantCompanionException.class, () -> service.createCompanionRelationship(request));

		verifyNoInteractions(plantRepository);
		verifyNoInteractions(plantCompanionRepository);
		verifyNoInteractions(plantCompanionMapper);
	}

	@Test
	void createCompanionRelationship_throwsException_whenPlantDoesNotExist() {

		Long plantId = 1L;
		Long companionId = 2L;

		CompanionRequest request = mock(CompanionRequest.class);
		when(request.getPlantId()).thenReturn(plantId);
		when(request.getCompanionPlantId()).thenReturn(companionId);
		when(request.getRelationshipType()).thenReturn(RelationshipType.values()[0]);

		when(plantRepository.findById(plantId)).thenReturn(Optional.empty());

		assertThrows(PlantNotFoundException.class, () -> service.createCompanionRelationship(request));

		verify(plantRepository).findById(plantId);
		verify(plantRepository, never()).findById(companionId);
		verifyNoInteractions(plantCompanionRepository);
		verifyNoInteractions(plantCompanionMapper);
	}

	@Test
	void createCompanionRelationship_throwsException_whenCompanionPlantDoesNotExist() {

		Long plantId = 1L;
		Long companionId = 2L;

		CompanionRequest request = mock(CompanionRequest.class);
		when(request.getPlantId()).thenReturn(plantId);
		when(request.getCompanionPlantId()).thenReturn(companionId);
		when(request.getRelationshipType()).thenReturn(RelationshipType.values()[0]);

		PlantEntity plant = mock(PlantEntity.class);
		when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));
		when(plantRepository.findById(companionId)).thenReturn(Optional.empty());

		assertThrows(PlantNotFoundException.class, () -> service.createCompanionRelationship(request));

		verify(plantRepository).findById(plantId);
		verify(plantRepository).findById(companionId);
		verifyNoInteractions(plantCompanionRepository);
		verifyNoInteractions(plantCompanionMapper);
	}

	@Test
	void createCompanionRelationship_throwsException_whenRelationshipAlreadyExists() {

		Long plantId = 1L;
		Long companionId = 2L;
		RelationshipType type = RelationshipType.values()[0];

		CompanionRequest request = mock(CompanionRequest.class);
		when(request.getPlantId()).thenReturn(plantId);
		when(request.getCompanionPlantId()).thenReturn(companionId);
		when(request.getRelationshipType()).thenReturn(type);

		PlantEntity plant = mock(PlantEntity.class);
		PlantEntity companionPlant = mock(PlantEntity.class);
		when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));
		when(plantRepository.findById(companionId)).thenReturn(Optional.of(companionPlant));

		when(plantCompanionRepository.existsByPlantIdAndCompanionPlantIdAndRelationshipType(plantId, companionId, type))
				.thenReturn(true);

		assertThrows(PlantCompanionException.class, () -> service.createCompanionRelationship(request));

		verify(plantCompanionRepository, never()).save(any());
		verifyNoInteractions(plantCompanionMapper);
	}

	@Test
	void getCompanionsForPlant_returnsMappedCompanions() {

		Long plantId = 1L;

		PlantEntity plant = mock(PlantEntity.class);
		when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));

		PlantCompanionEntity e1 = mock(PlantCompanionEntity.class);
		PlantCompanionEntity e2 = mock(PlantCompanionEntity.class);
		when(plantCompanionRepository.findByPlant(plant)).thenReturn(List.of(e1, e2));

		PlantCompanion m1 = mock(PlantCompanion.class);
		PlantCompanion m2 = mock(PlantCompanion.class);
		when(plantCompanionMapper.toModel(e1)).thenReturn(m1);
		when(plantCompanionMapper.toModel(e2)).thenReturn(m2);

		List<PlantCompanion> result = service.getCompanionsForPlant(plantId);

		assertEquals(List.of(m1, m2), result);
	}

	@Test
	void getCompanionsForPlant_throwsException_whenPlantDoesNotExist() {

		Long plantId = 1L;
		when(plantRepository.findById(plantId)).thenReturn(Optional.empty());

		assertThrows(PlantNotFoundException.class, () -> service.getCompanionsForPlant(plantId));

		verifyNoInteractions(plantCompanionRepository);
		verifyNoInteractions(plantCompanionMapper);
	}

	@Test
	void getCompanionsByType_returnsMappedCompanions() {

		Long plantId = 1L;
		RelationshipType type = RelationshipType.values()[0];

		PlantEntity plant = mock(PlantEntity.class);
		when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));

		PlantCompanionEntity e1 = mock(PlantCompanionEntity.class);
		when(plantCompanionRepository.findByPlantAndRelationshipType(plant, type)).thenReturn(List.of(e1));

		PlantCompanion m1 = mock(PlantCompanion.class);
		when(plantCompanionMapper.toModel(e1)).thenReturn(m1);

		List<PlantCompanion> result = service.getCompanionsByType(plantId, type);

		assertEquals(List.of(m1), result);
	}

	@Test
	void getCompanionsByType_throwsException_whenPlantDoesNotExist() {

		Long plantId = 1L;
		RelationshipType type = RelationshipType.values()[0];
		when(plantRepository.findById(plantId)).thenReturn(Optional.empty());

		assertThrows(PlantNotFoundException.class, () -> service.getCompanionsByType(plantId, type));

		verifyNoInteractions(plantCompanionRepository);
		verifyNoInteractions(plantCompanionMapper);
	}

	@Test
	void deleteCompanionRelationship_deletesBothSides_whenBidirectionalIsTrueAndReverseExists() {

		Long relationshipId = 10L;
		Long plantId = 1L;
		Long companionId = 2L;
		RelationshipType type = RelationshipType.values()[0];

		PlantEntity plant = mock(PlantEntity.class);
		PlantEntity companionPlant = mock(PlantEntity.class);
		when(plant.getId()).thenReturn(plantId);
		when(companionPlant.getId()).thenReturn(companionId);

		PlantCompanionEntity companion = mock(PlantCompanionEntity.class);
		when(companion.getBidirectional()).thenReturn(true);
		when(companion.getPlant()).thenReturn(plant);
		when(companion.getCompanionPlant()).thenReturn(companionPlant);
		when(companion.getRelationshipType()).thenReturn(type);

		PlantCompanionEntity reverse = mock(PlantCompanionEntity.class);

		when(plantCompanionRepository.findById(relationshipId)).thenReturn(Optional.of(companion));
		when(plantCompanionRepository.findByPlantIdAndCompanionPlantIdAndRelationshipType(companionId, plantId, type))
				.thenReturn(reverse);

		service.deleteCompanionRelationship(relationshipId);

		verify(plantCompanionRepository).delete(reverse);
		verify(plantCompanionRepository).delete(companion);
	}

	@Test
	void deleteCompanionRelationship_deletesOnlyForward_whenBidirectionalIsFalse() {

		Long relationshipId = 10L;

		PlantCompanionEntity companion = mock(PlantCompanionEntity.class);
		when(companion.getBidirectional()).thenReturn(false);

		when(plantCompanionRepository.findById(relationshipId)).thenReturn(Optional.of(companion));

		service.deleteCompanionRelationship(relationshipId);

		verify(plantCompanionRepository, never()).findByPlantIdAndCompanionPlantIdAndRelationshipType(anyLong(), anyLong(), any());
		verify(plantCompanionRepository).delete(companion);
	}

	@Test
	void deleteCompanionRelationship_deletesOnlyForward_whenReverseDoesNotExist() {

		Long relationshipId = 10L;
		Long plantId = 1L;
		Long companionId = 2L;
		RelationshipType type = RelationshipType.GOOD;

		PlantEntity plant = mock(PlantEntity.class);
		PlantEntity companionPlant = mock(PlantEntity.class);
		when(plant.getId()).thenReturn(plantId);
		when(companionPlant.getId()).thenReturn(companionId);

		PlantCompanionEntity companion = mock(PlantCompanionEntity.class);
		when(companion.getBidirectional()).thenReturn(true);
		when(companion.getPlant()).thenReturn(plant);
		when(companion.getCompanionPlant()).thenReturn(companionPlant);
		when(companion.getRelationshipType()).thenReturn(type);

		when(plantCompanionRepository.findById(relationshipId)).thenReturn(Optional.of(companion));
		when(plantCompanionRepository.findByPlantIdAndCompanionPlantIdAndRelationshipType(companionId, plantId, type))
				.thenReturn(null);

		service.deleteCompanionRelationship(relationshipId);

		verify(plantCompanionRepository, never()).delete(isNull());
		verify(plantCompanionRepository).delete(companion);
	}

	@Test
	void deleteCompanionRelationship_throwsException_whenRelationshipDoesNotExist() {

		Long relationshipId = 10L;
		when(plantCompanionRepository.findById(relationshipId)).thenReturn(Optional.empty());

		assertThrows(PlantCompanionException.class, () -> service.deleteCompanionRelationship(relationshipId));

		verify(plantCompanionRepository, never()).delete(any());
	}

	@Test
	void updateCompanionRelationship_updatesProvidedFieldsOnly() {

		Long id = 1L;

		CompanionUpdateRequest updateRequest = mock(CompanionUpdateRequest.class);
		when(updateRequest.getRecommendedDistanceCm()).thenReturn(30);

		PlantCompanionEntity existing = PlantCompanionEntity.builder()
				.id(id)
				.recommendedDistanceCm(10)
				.bidirectional(true)
				.build();

		when(plantCompanionRepository.findById(id)).thenReturn(Optional.of(existing));
		
		doAnswer(invocation -> {
			CompanionUpdateRequest req = invocation.getArgument(0);
			PlantCompanionEntity entity = invocation.getArgument(1);
			if (req.getRecommendedDistanceCm() != null) {
				entity.setRecommendedDistanceCm(req.getRecommendedDistanceCm());
			}
			return null;
		}).when(plantCompanionMapper).updateEntityFromRequest(eq(updateRequest), eq(existing));

		when(plantCompanionRepository.save(existing)).thenReturn(existing);

		PlantCompanion mapped = mock(PlantCompanion.class);
		when(plantCompanionMapper.toModel(existing)).thenReturn(mapped);

		PlantCompanion result = service.updateCompanionRelationship(id, updateRequest);

		assertSame(mapped, result);

		ArgumentCaptor<PlantCompanionEntity> captor = ArgumentCaptor.forClass(PlantCompanionEntity.class);
		verify(plantCompanionRepository).save(captor.capture());
		PlantCompanionEntity saved = captor.getValue();

		assertEquals(30, saved.getRecommendedDistanceCm());
		assertEquals(true, saved.getBidirectional()); // niezmienione
	}

	@Test
	void updateCompanionRelationship_throwsException_whenRelationshipDoesNotExist() {

		Long relationshipId = 10L;
		when(plantCompanionRepository.findById(relationshipId)).thenReturn(Optional.empty());

		CompanionUpdateRequest updateRequest = mock(CompanionUpdateRequest.class);

		assertThrows(PlantCompanionException.class, () -> service.updateCompanionRelationship(relationshipId, updateRequest));

		verify(plantCompanionRepository, never()).save(any());
		verifyNoInteractions(plantCompanionMapper);
	}

	@Test
	void getAllRelationshipsForPlant_returnsMappedRelationships() {

		Long plantId = 1L;

		PlantCompanionEntity e1 = mock(PlantCompanionEntity.class);
		PlantCompanionEntity e2 = mock(PlantCompanionEntity.class);
		when(plantCompanionRepository.findByPlantIdOrCompanionPlantId(plantId, plantId)).thenReturn(List.of(e1, e2));

		PlantCompanion m1 = mock(PlantCompanion.class);
		PlantCompanion m2 = mock(PlantCompanion.class);
		when(plantCompanionMapper.toModel(e1)).thenReturn(m1);
		when(plantCompanionMapper.toModel(e2)).thenReturn(m2);

		List<PlantCompanion> result = service.getAllRelationshipsForPlant(plantId);

		assertEquals(List.of(m1, m2), result);
	}
}
