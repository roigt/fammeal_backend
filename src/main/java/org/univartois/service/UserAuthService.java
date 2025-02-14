package org.univartois.service;

import jakarta.validation.constraints.NotBlank;
import org.univartois.dto.request.ForgotPasswordRequestDto;
import org.univartois.dto.request.UserAuthRequestDto;
import org.univartois.dto.request.UserRegisterRequestDto;
import org.univartois.dto.response.ForgotPasswordResponseDto;
import org.univartois.dto.response.UserAuthResponseDto;
import org.univartois.dto.response.UserRegisterResponseDto;
import org.univartois.dto.response.VerificationAccountResponseDto;

public interface UserAuthService {
    UserRegisterResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto);
    VerificationAccountResponseDto verifyAccount(String token);

    UserAuthResponseDto auth(UserAuthRequestDto userAuthRequestDto);

    ForgotPasswordResponseDto forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto);

    void resetPassword(@NotBlank String token);
}
