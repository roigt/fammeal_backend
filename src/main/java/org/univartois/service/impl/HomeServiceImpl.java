package org.univartois.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.univartois.dto.request.*;
import org.univartois.dto.response.DietaryConstraintsResponseDto;
import org.univartois.dto.response.HomeMemberResponseDto;
import org.univartois.dto.response.HomeResponseDto;
import org.univartois.entity.*;
import org.univartois.enums.HomeRoleType;
import org.univartois.exception.AdminRoleModificationException;
import org.univartois.exception.CannotLeaveHomeException;
import org.univartois.exception.ResourceNotFoundException;
import org.univartois.exception.UserAlreadyInHomeException;
import org.univartois.mapper.DietaryConstraintsMapper;
import org.univartois.mapper.HomeMapper;
import org.univartois.mapper.UserMapper;
import org.univartois.repository.AllergyRepository;
import org.univartois.repository.HomeRepository;
import org.univartois.repository.HomeRoleRepository;
import org.univartois.repository.UserRepository;
import org.univartois.service.HomeService;
import org.univartois.service.RoleService;
import org.univartois.utils.Constants;

import java.util.*;

@ApplicationScoped
public class HomeServiceImpl implements HomeService {
    @Inject
    HomeRepository homeRepository;

    @Inject
    HomeMapper homeMapper;
    @Inject
    UserMapper userMapper;

    @Inject
    DietaryConstraintsMapper dietaryConstraintsMapper;

    @Inject
    JsonWebToken jsonWebToken;
    @Inject
    UserRepository userRepository;
    @Inject
    HomeRoleRepository homeRoleRepository;
    @Inject
    RoleService roleService;
    @Inject
    AllergyRepository allergyRepository;

    @Override
    @Transactional
    public HomeResponseDto createHome(CreateHomeRequestDto createHomeRequestDto) {
        UUID userId = UUID.fromString(jsonWebToken.getSubject());
        UserEntity user = userRepository.findByIdOptional(userId).orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND_MSG));
        HomeEntity home = homeMapper.toEntity(createHomeRequestDto);
        homeRepository.persist(home);
        HomeRoleEntity homeRole = HomeRoleEntity.builder().home(home).user(user).role(HomeRoleType.ADMIN).build();
        homeRoleRepository.persist(homeRole);

        return homeMapper.toHomeResponseDto(home, HomeRoleType.ADMIN.name());
    }

    @Transactional
    @Override
    public HomeResponseDto updateHome(UUID homeId, UpdateHomeRequestDto updateHomeRequestDto) {
        HomeEntity home = homeRepository.findByIdOptional(homeId).orElseThrow(() -> new ResourceNotFoundException(Constants.HOME_NOT_FOUND_MSG));

        home.setName(updateHomeRequestDto.getName());

        return homeMapper.toHomeResponseDto(home, HomeRoleType.ADMIN.name());
    }

    @Override
    public HomeResponseDto getHomeById(UUID homeId) {
        final HomeEntity home = homeRepository.findByIdOptional(homeId).orElseThrow(() -> new ResourceNotFoundException(Constants.HOME_NOT_FOUND_MSG));
        UUID userId = UUID.fromString(jsonWebToken.getSubject());
        String roleInHome = roleService.getRolesByUserId(userId).getOrDefault(homeId.toString(), null);
        return homeMapper.toHomeResponseDto(home, roleInHome);
    }

    @Override
    public List<HomeResponseDto> getUserHomes(UUID userId) {
        Map<String, String> roles = roleService.getRolesByUserId(userId);
        return homeRepository.findHomesByUserId(userId).stream().map((home) -> homeMapper.toHomeResponseDto(home, roles.getOrDefault(home.getId().toString(), null))).toList();
    }

    @Transactional
    @Override
    public void leaveHome(UUID homeId) {
        UUID userId = UUID.fromString(jsonWebToken.getSubject());
        String roleInHome = roleService.getRolesByUserId(userId).getOrDefault(homeId.toString(), null);

        if (roleInHome != null && HomeRoleType.valueOf(roleInHome).equals(HomeRoleType.ADMIN) && homeRoleRepository.countAdminRolesByHomeId(homeId) <= 1) {
            throw new CannotLeaveHomeException(Constants.HOME_ADMIN_LEAVE_CONSTRAINT_MSG);
        }

        homeRoleRepository.deleteById(new HomeRoleId(homeId, userId));
    }

    @Transactional
    @Override
    public void addHomeMember(UUID homeId, AddHomeMemberRequestDto addHomeMemberRequestDto) {
        UserEntity user = userRepository.findByEmail(addHomeMemberRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND_MSG));

        HomeEntity home = homeRepository.findByIdOptional(homeId)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.HOME_NOT_FOUND_MSG));

        if (homeRoleRepository.existsByUserIdAndHomeId(user.getId(), homeId)) {
            throw new UserAlreadyInHomeException(Constants.USER_ALREADY_IN_HOME_MSG);
        }

        HomeRoleEntity homeRole = HomeRoleEntity.builder().user(user).home(home).role(addHomeMemberRequestDto.getRole()).build();
        homeRoleRepository.persist(homeRole);
    }

    //    @TODO: optimize db queries later
    @Override
    public List<HomeMemberResponseDto> getHomeMembers(UUID homeId) {
        return userRepository.findUsersByHomeId(homeId).stream()
                .map(user -> userMapper.toHomeMemberResponseDto(
                        user,
                        user.getRoles().stream()
                                .filter(role -> role.getId().getHomeId().equals(homeId))
                                .map(role -> role.getRole().toString())
                                .findFirst().orElse(null)
                ))
                .toList();
    }

    //    @TODO: optimize db queries
    @Override
    public HomeMemberResponseDto getHomeMember(UUID homeId, UUID userId) {
        UserEntity user = userRepository.findUserByHomeIdAndUserId(homeId, userId).orElseThrow(() -> new ResourceNotFoundException(Constants.HOME_MEMBER_NOT_FOUND_MSG));

        return userMapper.toHomeMemberResponseDto(
                user,
                user.getRoles().stream().filter(homeRoleEntity -> homeRoleEntity.getId().getHomeId().equals(homeId)).map(homeRoleEntity -> homeRoleEntity.getRole().toString()).findFirst().orElse(null)
        );
    }

    @Transactional
    @Override
    public HomeMemberResponseDto updateHomeMember(UUID homeId, UUID userId, UpdateHomeMemberRequestDto updateHomeMemberRequestDto) {
        UUID currentAuthUserId = UUID.fromString(jsonWebToken.getSubject());
        if (userId.equals(currentAuthUserId)) {
            throw new AdminRoleModificationException(Constants.HOME_ADMIN_SELF_UPDATE_ROLE_CONSTRAINT_MSG);
        }
        UserEntity user = userRepository.findUserByHomeIdAndUserId(homeId, userId).orElseThrow(() -> new ResourceNotFoundException(Constants.HOME_MEMBER_NOT_FOUND_MSG));

        user.getRoles().forEach(homeRoleEntity -> {
            if (homeRoleEntity.getId().getHomeId().equals(homeId) && homeRoleEntity.getId().getUserId().equals(userId)) {
                homeRoleEntity.setRole(updateHomeMemberRequestDto.getRole());
            }
        });
        return userMapper.toHomeMemberResponseDto(
                user,
                user.getRoles().stream().filter(homeRoleEntity -> homeRoleEntity.getId().getUserId().equals(userId) && homeRoleEntity.getId().getHomeId().equals(homeId)).map(homeRoleEntity -> homeRoleEntity.getRole().toString()).findFirst().orElse(null)
        );
    }

    @Transactional
    @Override
    public void deleteHomeMember(UUID homeId, UUID userId) {
        UUID currentAuthUserId = UUID.fromString(jsonWebToken.getSubject());
        if (currentAuthUserId.equals(userId)) {
            throw new CannotLeaveHomeException(Constants.HOME_ADMIN_SELF_DELETE_FROM_HOME_CONSTRAINT_MSG);
        }
        homeRoleRepository.deleteById(new HomeRoleId(homeId, userId));
    }

//    @TODO: optimize db queries
    @Transactional
    @Override
    public DietaryConstraintsResponseDto updateDietaryConstraints(UUID homeId, UpdateDietaryConstraintsRequestDto updateDietaryConstraintsRequestDto) {
        HomeEntity home = homeRepository.findByIdOptional(homeId).orElseThrow(() -> new ResourceNotFoundException(Constants.HOME_NOT_FOUND_MSG));

        Set<AllergyEntity> allergies = new HashSet<>(allergyRepository.findByIds(updateDietaryConstraintsRequestDto.getAllergies()));
        home.setAllergies(allergies);
        home.setVegetarian(updateDietaryConstraintsRequestDto.isVegetarian());

        return dietaryConstraintsMapper.toDietaryConstraintsResponseDto(home.isVegetarian(), allergies);
    }

//    @TODO: optimize db queries
    @Override
    public DietaryConstraintsResponseDto getDietaryConstraints(UUID homeId) {
        HomeEntity home = homeRepository.findByIdOptional(homeId).orElseThrow(() -> new ResourceNotFoundException(Constants.HOME_NOT_FOUND_MSG));

        return dietaryConstraintsMapper.toDietaryConstraintsResponseDto(home.isVegetarian(), home.getAllergies());
    }


    @Transactional
    @Override
    public void toggleMealGeneration(UUID homeId, boolean lunch) {
        HomeEntity home = homeRepository.findByIdOptional(homeId).orElseThrow(() -> new ResourceNotFoundException(Constants.HOME_NOT_FOUND_MSG));

        if (lunch) home.setLunchAutomaticGeneration(!home.isLunchAutomaticGeneration());
        else home.setDinerAutomaticGeneration(!home.isDinerAutomaticGeneration());
    }
}
