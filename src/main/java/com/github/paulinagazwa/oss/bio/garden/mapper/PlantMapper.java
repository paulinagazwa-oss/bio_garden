package com.github.paulinagazwa.oss.bio.garden.mapper;

import com.github.paulinagazwa.oss.bio.garden.entity.PlantEntity;
import com.github.paulinagazwa.oss.bio.garden.model.Plant;
import com.github.paulinagazwa.oss.bio.garden.model.PlantCreateRequest;
import com.github.paulinagazwa.oss.bio.garden.model.PlantUpdateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlantMapper {

	Plant toModel(PlantEntity plantEntity);

	PlantEntity toEntity(Plant plant);

	PlantEntity fromCreateRequest(PlantCreateRequest plantCreateRequest);

	PlantEntity fromUpdateRequest(PlantUpdateRequest plantUpdateRequest);
}
