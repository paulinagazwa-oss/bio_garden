package com.github.paulinagazwa.oss.bio.garden.mapper;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantCompanionEntity;
import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.model.CompanionRequest;
import com.github.paulinagazwa.oss.bio.garden.model.CompanionUpdateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCompanion;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PlantCompanionMapper {

	String PLANT_ID = "plantId";

	String COMPANION_PLANT_ID = "companionPlantId";

	String PLANT_ID_MODEL = "plant.id";

	String COMPANION_PLANT_ID_MODEL = "companionPlant.id";

	String PLANT = "plant";

	String COMPANION_PLANT = "companionPlant";

	String ID = "id";

	@Mapping(target = PLANT_ID, source = PLANT_ID_MODEL)
	@Mapping(target = COMPANION_PLANT_ID, source = COMPANION_PLANT_ID_MODEL)
	PlantCompanion toModel(PlantCompanionEntity plantCompanionEntity);

	@Mapping(target = PLANT, ignore = true)
	@Mapping(target = COMPANION_PLANT, ignore = true)
	PlantCompanionEntity toEntity(PlantCompanion plantCompanion);

	@Mapping(target = ID, ignore = true)
	@Mapping(target = PLANT, source = PLANT)
	@Mapping(target = COMPANION_PLANT, source = COMPANION_PLANT)
	PlantCompanionEntity fromCompanionRequest(CompanionRequest plantCompanion, PlantEntity plant, PlantEntity companionPlant);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = ID, ignore = true)
	@Mapping(target = PLANT, ignore = true)
	@Mapping(target = COMPANION_PLANT, ignore = true)
	void updateEntityFromRequest(CompanionUpdateRequest request, @MappingTarget PlantCompanionEntity entity);
}
