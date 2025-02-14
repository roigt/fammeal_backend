package org.univartois.service.impl;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.univartois.dto.request.CreateHomeRequestDto;
import org.univartois.dto.response.HomeResponseDto;
import org.univartois.entity.HomeEntity;
import org.univartois.entity.HomeRoleEntity;
import org.univartois.entity.UserEntity;
import org.univartois.enums.HomeRoleType;
import org.univartois.exception.ResourceNotFoundException;
import org.univartois.mapper.HomeMapper;
import org.univartois.repository.HomeRepository;
import org.univartois.repository.HomeRoleRepository;
import org.univartois.repository.UserRepository;
import org.univartois.service.HomeService;

import java.util.UUID;

@ApplicationScoped
public class HomeServiceImpl implements HomeService {
    @Inject
    HomeRepository homeRepository;

    @Inject
    HomeMapper homeMapper;

    @Inject
    JsonWebToken jsonWebToken;
    @Inject
    UserRepository userRepository;
    @Inject
    HomeRoleRepository homeRoleRepository;

    @Override
    @Transactional
    public HomeResponseDto createHome(CreateHomeRequestDto createHomeRequestDto) {
        UUID userId = UUID.fromString(jsonWebToken.getSubject());
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("utilisateur avec cet id n'existe pas."));
        HomeEntity home = homeMapper.toEntity(createHomeRequestDto);
        homeRepository.persist(home);
        HomeRoleEntity homeRole = HomeRoleEntity.builder().home(home).user(user).role(HomeRoleType.ADMIN).build();
        homeRoleRepository.persist(homeRole);

        return homeMapper.toHomeResponseDto(home);
    }

    @Override
    public HomeResponseDto getHomeById(UUID homeId) {
        final HomeEntity home = homeRepository.findHomeById(homeId).orElseThrow(() -> new ResourceNotFoundException("Une maison avec cet id n'existe pas."));
        return homeMapper.toHomeResponseDto(home);
    }
}
