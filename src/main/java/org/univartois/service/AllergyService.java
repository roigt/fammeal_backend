package org.univartois.service;

import org.univartois.dto.response.AllergyResponseDto;

import java.util.List;

public interface AllergyService {
    List<AllergyResponseDto> getAllergies();
}
