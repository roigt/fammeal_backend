package org.univartois.service;

import org.univartois.dto.request.UserRegisterRequestDto;
import org.univartois.dto.response.UserRegisterResponseDto;

public interface UserAuthService {
    UserRegisterResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto);


}
