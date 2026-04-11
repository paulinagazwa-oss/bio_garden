package com.github.paulinagazwa.oss.bio.garden.mapper;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.model.Plant;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCreateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PlantMapper {

	String ID = "id";

	String SOW_TO = "sowTo";

	String SOW_FROM = "sowFrom";

	String PERIOD_SOW_FROM = "sowPeriod.sowFrom";

	String PERIOD_SOW_TO = "sowPeriod.sowTo";

	String LAST_UPDATE_DATE = "lastUpdateDate";

	String CREATION_DATE = "creationDate";

	@Mapping(target = PERIOD_SOW_FROM, source = SOW_FROM)
	@Mapping(target = PERIOD_SOW_TO, source = SOW_TO)
	Plant toModel(PlantEntity plantEntity);

	@Mapping(target = SOW_FROM, source = PERIOD_SOW_FROM)
	@Mapping(target = SOW_TO, source = PERIOD_SOW_TO)
	PlantEntity toEntity(Plant plant);

	@Mapping(target = ID, ignore = true)
	@Mapping(target = CREATION_DATE, ignore = true)
	@Mapping(target = LAST_UPDATE_DATE, ignore = true)
	@Mapping(target = SOW_FROM, source = PERIOD_SOW_FROM)
	@Mapping(target = SOW_TO, source = PERIOD_SOW_TO)
	PlantEntity fromCreateRequest(PlantCreateRequest plantCreateRequest);

	@Mapping(target = ID, ignore = true)
	@Mapping(target = CREATION_DATE, ignore = true)
	@Mapping(target = LAST_UPDATE_DATE, ignore = true)
	@Mapping(target = SOW_FROM, source = PERIOD_SOW_FROM)
	@Mapping(target = SOW_TO, source = PERIOD_SOW_TO)
	void updateEntityFromRequest(PlantUpdateRequest request, @MappingTarget PlantEntity entity);

	default OffsetDateTime map(LocalDateTime value) {

		return value == null ? null : value.atOffset(ZoneOffset.UTC);
	}

	default LocalDateTime map(OffsetDateTime value) {

		return value == null ? null : value.toLocalDateTime();
	}
}
