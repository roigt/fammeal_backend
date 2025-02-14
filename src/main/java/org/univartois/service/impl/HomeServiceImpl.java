package org.univartois.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.univartois.dto.request.CreateHomeRequestDto;
import org.univartois.dto.response.HomeResponseDto;
import org.univartois.entity.HomeEntity;
import org.univartois.exception.ResourceNotFoundException;
import org.univartois.mapper.HomeMapper;
import org.univartois.repository.HomeRepository;
import org.univartois.repository.HomeRoleRepository;
import org.univartois.service.HomeService;

import java.util.UUID;

@ApplicationScoped
public class HomeServiceImpl implements HomeService {
    @Inject
    HomeRepository homeRepository;

    @Inject
    HomeMapper homeMapper;

    @Override
    public HomeResponseDto createHome(CreateHomeRequestDto createHomeRequestDto) {
        return HomeResponseDto.builder().build();
    }

    @Override
    public HomeResponseDto getHomeById(UUID homeId) {
        final HomeEntity home = homeRepository.findHomeById(homeId).orElseThrow(() -> new ResourceNotFoundException("Une maison avec cet id n'existe pas."));
        return homeMapper.toHomeResponseDto(home);
    }
}
