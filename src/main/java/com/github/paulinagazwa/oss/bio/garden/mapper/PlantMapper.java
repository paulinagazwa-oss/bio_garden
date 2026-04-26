package com.github.paulinagazwa.oss.bio.garden.mapper;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.model.Plant;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCompanion;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCreateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantWithCompanions;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Mapper(
		componentModel = "spring",
		unmappedTargetPolicy = ReportingPolicy.ERROR,
		uses = { PlantCompanionMapper.class})
public interface PlantMapper {

	String ID = "id";

	String SOW_TO = "sowTo";

	String SOW_FROM = "sowFrom";

	String PERIOD_SOW_FROM = "sowPeriod.sowFrom";

	String PERIOD_SOW_TO = "sowPeriod.sowTo";

	String LAST_UPDATE_DATE = "lastUpdateDate";

	String CREATION_DATE = "creationDate";

	String COMPANIONS = "companions";

	String COMPANION_FOR = "companionFor";

	@Mapping(target = PERIOD_SOW_FROM, source = SOW_FROM)
	@Mapping(target = PERIOD_SOW_TO, source = SOW_TO)
	Plant toModel(PlantEntity plantEntity);

	@Mapping(target = SOW_FROM, source = PERIOD_SOW_FROM)
	@Mapping(target = SOW_TO, source = PERIOD_SOW_TO)
	@Mapping(target = COMPANIONS, ignore = true)
	@Mapping(target = COMPANION_FOR, ignore = true)
	PlantEntity toEntity(Plant plant);

	@Mapping(target = ID, ignore = true)
	@Mapping(target = CREATION_DATE, ignore = true)
	@Mapping(target = LAST_UPDATE_DATE, ignore = true)
	@Mapping(target = SOW_FROM, source = PERIOD_SOW_FROM)
	@Mapping(target = SOW_TO, source = PERIOD_SOW_TO)
	@Mapping(target = COMPANIONS, ignore = true)
	@Mapping(target = COMPANION_FOR, ignore = true)
	PlantEntity fromCreateRequest(PlantCreateRequest plantCreateRequest);

	@Mapping(target = ID, ignore = true)
	@Mapping(target = CREATION_DATE, ignore = true)
	@Mapping(target = LAST_UPDATE_DATE, ignore = true)
	@Mapping(target = SOW_FROM, source = PERIOD_SOW_FROM)
	@Mapping(target = SOW_TO, source = PERIOD_SOW_TO)
	@Mapping(target = COMPANIONS, ignore = true)
	@Mapping(target = COMPANION_FOR, ignore = true)
	void updateEntityFromRequest(PlantUpdateRequest request, @MappingTarget PlantEntity entity);

	@Mapping(target = PERIOD_SOW_FROM, source = SOW_FROM)
	@Mapping(target = PERIOD_SOW_TO, source = SOW_TO)
	PlantWithCompanions toModelWithCompanions(PlantEntity plantEntity);

	@AfterMapping
	default void filterCompanionForBidirectional(
			PlantEntity entity,
			@MappingTarget PlantWithCompanions target
	) {
		if (target.getCompanionFor() == null) {
			return;
		}

		List<PlantCompanion> filtered =
				target.getCompanionFor().stream()
						.filter(Objects::nonNull)
						.filter(pc -> Boolean.FALSE.equals(pc.getBidirectional()))
						.toList();

		target.setCompanionFor(filtered);
	}

	default OffsetDateTime map(LocalDateTime value) {

		return value == null ? null : value.atOffset(ZoneOffset.UTC);
	}

	default LocalDateTime map(OffsetDateTime value) {

		return value == null ? null : value.toLocalDateTime();
	}
}
