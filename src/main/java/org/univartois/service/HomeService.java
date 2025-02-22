package org.univartois.service;

import org.univartois.dto.request.AddHomeMemberRequestDto;
import org.univartois.dto.request.CreateHomeRequestDto;
import org.univartois.dto.request.UpdateHomeMemberRequestDto;
import org.univartois.dto.response.HomeMemberResponseDto;
import org.univartois.dto.response.HomeResponseDto;

import java.util.List;
import java.util.UUID;

public interface HomeService {

    HomeResponseDto createHome(CreateHomeRequestDto createHomeRequestDto);
    HomeResponseDto getHomeById(UUID homeId);

    List<HomeResponseDto> getUserHomes(UUID userId);

    void leaveHome(UUID homeId);

    void addHomeMember(UUID homeId, AddHomeMemberRequestDto addHomeMemberRequestDto);

    List<HomeMemberResponseDto> getHomeMembers(UUID homeId);

    HomeMemberResponseDto getHomeMember(UUID homeId, UUID userId);

    HomeMemberResponseDto updateHomeMember(UUID homeId, UUID userId, UpdateHomeMemberRequestDto updateHomeMemberRequestDto);

    void deleteHomeMember(UUID homeId, UUID userId);
}
