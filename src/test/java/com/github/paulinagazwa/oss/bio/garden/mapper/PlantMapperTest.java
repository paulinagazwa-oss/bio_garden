package com.github.paulinagazwa.oss.bio.garden.mapper;

import com.github.paulinagazwa.oss.bio.garden.model.PlantCompanion;
import com.github.paulinagazwa.oss.bio.garden.model.PlantWithCompanions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PlantMapperTest {

	@Autowired
	private PlantMapper plantMapper;

	@Test
	void map_convertsLocalDateTimeToOffsetDateTimeWithUtcOffset() {

		LocalDateTime localDateTime = LocalDateTime.of(2024, 3, 20, 8, 30);

		OffsetDateTime result = plantMapper.map(localDateTime);

		assertThat(result.getOffset()).isEqualTo(ZoneOffset.UTC);
		assertThat(result.toLocalDateTime()).isEqualTo(localDateTime);
	}

	@Test
	void map_returnsNull_whenLocalDateTimeIsNull() {

		assertThat(plantMapper.map((LocalDateTime) null)).isNull();
	}

	@Test
	void map_convertsOffsetDateTimeToLocalDateTime() {

		OffsetDateTime offsetDateTime = OffsetDateTime.of(2024, 3, 20, 8, 30, 0, 0, ZoneOffset.UTC);

		LocalDateTime result = plantMapper.map(offsetDateTime);

		assertThat(result).isEqualTo(LocalDateTime.of(2024, 3, 20, 8, 30));
	}

	@Test
	void map_returnsNull_whenOffsetDateTimeIsNull() {

		assertThat(plantMapper.map((OffsetDateTime) null)).isNull();
	}

	@Test
	void filterCompanionForBidirectional_shouldKeepOnlyUnidirectionalRelationships() {

		PlantCompanion bidirectional = new PlantCompanion();
		bidirectional.setBidirectional(true);

		PlantCompanion unidirectional = new PlantCompanion();
		unidirectional.setBidirectional(false);

		PlantCompanion nullBidirectional = new PlantCompanion();
		nullBidirectional.setBidirectional(null);

		PlantWithCompanions target = new PlantWithCompanions();
		target.setCompanionFor(List.of(bidirectional, unidirectional, nullBidirectional));

		plantMapper.filterCompanionForBidirectional(null, target);

		assertThat(target.getCompanionFor()).containsExactly(unidirectional);
	}

	@Test
	void filterCompanionForBidirectional_shouldNotFailWhenCompanionForIsNull() {

		PlantWithCompanions target = new PlantWithCompanions();
		target.setCompanionFor(null);

		plantMapper.filterCompanionForBidirectional(null, target);

		assertThat(target.getCompanionFor()).isNull();
	}

	@Test
	void filterCompanionForBidirectional_shouldIgnoreNullElementsInCompanionForList() {

		PlantCompanion unidirectional = new PlantCompanion();
		unidirectional.setBidirectional(false);

		PlantWithCompanions target = new PlantWithCompanions();
		target.setCompanionFor(new ArrayList<>(Arrays.asList(null, unidirectional, null)));

		plantMapper.filterCompanionForBidirectional(null, target);

		assertThat(target.getCompanionFor()).containsExactly(unidirectional);
	}
}
