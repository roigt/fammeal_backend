package org.univartois.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.univartois.dto.request.UserRegisterRequestDto;
import org.univartois.dto.response.UserRegisterResponseDto;
import org.univartois.repository.UserRepository;
import org.univartois.service.UserAuthService;

@ApplicationScoped
public class UserAuthServiceImpl implements UserAuthService {

    @Inject
    UserRepository userRepository;

    @Override
    public UserRegisterResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto) {

        return null;
    }
}
