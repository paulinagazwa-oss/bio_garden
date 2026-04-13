package com.github.paulinagazwa.oss.bio.garden.mapper;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantCompanionEntity;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCompanion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PlantCompanionMapper {

	@Mapping(target = "plantId", source = "plant.id")
	@Mapping(target = "companionPlantId", source = "companionPlant.id")
	PlantCompanion toModel(PlantCompanionEntity plantCompanionEntity);

	@Mapping(target = "plant", ignore = true)
	@Mapping(target = "companionPlant", ignore = true)
	PlantCompanionEntity toEntity(PlantCompanion plantCompanion);
}
