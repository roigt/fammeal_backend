package org.univartois.service;

import org.univartois.dto.request.*;
import org.univartois.dto.response.DietaryConstraintsResponseDto;
import org.univartois.dto.response.HomeMemberResponseDto;
import org.univartois.dto.response.HomeResponseDto;

import java.util.List;
import java.util.UUID;

public interface HomeService {

    HomeResponseDto createHome(CreateHomeRequestDto createHomeRequestDto);

    HomeResponseDto getHomeById(UUID homeId);

    List<HomeResponseDto> getMyHomes();

    void leaveHome(UUID homeId);

    HomeMemberResponseDto addHomeMember(UUID homeId, AddHomeMemberRequestDto addHomeMemberRequestDto);

    List<HomeMemberResponseDto> getHomeMembers(UUID homeId);

    HomeMemberResponseDto getHomeMember(UUID homeId, UUID memberId);

    HomeMemberResponseDto updateHomeMember(UUID homeId, UUID memberId, UpdateHomeMemberRequestDto updateHomeMemberRequestDto);

    void deleteHomeMember(UUID homeId, UUID memberId);

    DietaryConstraintsResponseDto updateDietaryConstraints(UUID homeId, UpdateDietaryConstraintsRequestDto updateDietaryConstraintsRequestDto);

    DietaryConstraintsResponseDto getDietaryConstraints(UUID homeId);

    HomeResponseDto updateHome(UUID homeId, UpdateHomeRequestDto updateHomeRequestDto);

    void toggleMealGeneration(UUID homeId, boolean lunch);

    void deleteHome(UUID homeId);
}
