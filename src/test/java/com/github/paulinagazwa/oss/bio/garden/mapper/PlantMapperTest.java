package com.github.paulinagazwa.oss.bio.garden.mapper;

import com.github.paulinagazwa.oss.bio.garden.model.PlantCompanion;
import com.github.paulinagazwa.oss.bio.garden.model.PlantWithCompanions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PlantMapperTest {

	private final PlantMapper plantMapper = Mappers.getMapper(PlantMapper.class);

	@Test
	void map_convertsMonthDayToStringInMmDdFormat() {

		MonthDay monthDay = MonthDay.of(3, 20);

		String result = plantMapper.map(monthDay);

		assertThat(result).isEqualTo("03-20");
	}

	@Test
	void map_returnsNull_whenMonthDayIsNull() {

		assertThat(plantMapper.map((MonthDay) null)).isNull();
	}

	@Test
	void map_convertsStringInMmDdFormatToMonthDay() {

		MonthDay result = plantMapper.map("03-20");

		assertThat(result).isEqualTo(MonthDay.of(3, 20));
	}

	@Test
	void map_returnsNull_whenStringIsNull() {

		assertThat(plantMapper.map((String) null)).isNull();
	}

	@Test
	void map_returnsNull_whenStringIsBlank() {

		assertThat(plantMapper.map(" ")).isNull();
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
