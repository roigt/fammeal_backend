package org.univartois.service;

import org.univartois.dto.request.UserRegisterRequestDto;
import org.univartois.dto.response.UserRegisterResponseDto;
import org.univartois.dto.response.VerificationAccountResponseDto;

public interface UserAuthService {
    UserRegisterResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto);
    VerificationAccountResponseDto verifyAccount(String token);


}
