package org.univartois.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.univartois.dto.response.AllergyResponseDto;
import org.univartois.mapper.AllergyMapper;
import org.univartois.repository.AllergyRepository;
import org.univartois.service.AllergyService;

import java.util.List;

@ApplicationScoped
public class AllergyServiceImpl implements AllergyService {

    @Inject
    AllergyRepository allergyRepository;

    @Inject
    AllergyMapper allergyMapper;

    @Override
    public List<AllergyResponseDto> getAllergies() {

        return allergyRepository.findAll().stream().map(allergyMapper::toAllergyResponseDto).toList();
    }
}
