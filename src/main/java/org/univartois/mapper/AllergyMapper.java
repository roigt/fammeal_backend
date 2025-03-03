package org.univartois.mapper;

import org.mapstruct.Mapper;
import org.univartois.dto.response.AllergyResponseDto;
import org.univartois.entity.AllergyEntity;

@Mapper(componentModel = "jakarta")
public interface AllergyMapper {

    AllergyResponseDto toAllergyResponseDto(AllergyEntity allergy);
}
