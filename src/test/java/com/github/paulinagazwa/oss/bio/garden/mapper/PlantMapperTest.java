package com.github.paulinagazwa.oss.bio.garden.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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
}
