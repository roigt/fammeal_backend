package org.univartois.mapper;

import org.mapstruct.Mapper;
import org.univartois.dto.response.DietaryConstraintsResponseDto;
import org.univartois.entity.AllergyEntity;

import java.util.Set;

@Mapper(componentModel = "jakarta", uses = {AllergyMapper.class})
public interface DietaryConstraintsMapper {

    DietaryConstraintsResponseDto toDietaryConstraintsResponseDto(boolean vegetarian, Set<AllergyEntity> allergies);

}
