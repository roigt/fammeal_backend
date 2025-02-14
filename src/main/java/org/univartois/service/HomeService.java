package org.univartois.service;

import org.univartois.dto.request.CreateHomeRequestDto;
import org.univartois.dto.response.HomeResponseDto;

import java.util.UUID;

public interface HomeService {

    HomeResponseDto createHome(CreateHomeRequestDto createHomeRequestDto);
    HomeResponseDto getHomeById(UUID homeId);
}
