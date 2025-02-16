package org.univartois.service;

import jakarta.validation.constraints.NotBlank;
import org.univartois.dto.request.ForgotPasswordRequestDto;
import org.univartois.dto.request.UserAuthRequestDto;
import org.univartois.dto.request.UserRegisterRequestDto;
import org.univartois.dto.request.UserVerificationRequestDto;
import org.univartois.dto.response.*;

import java.util.UUID;

public interface UserAuthService {
    UserRegisterResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto);
    VerificationAccountResponseDto verifyAccount(String token);

    UserAuthResponseDto auth(UserAuthRequestDto userAuthRequestDto);

    ForgotPasswordResponseDto forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto);

    UserVerificationResponseDto sendVerificationToken(UserVerificationRequestDto userVerificationRequestDto);

    void resetPassword(@NotBlank String token);

    UserAuthResponseDto getUserById(UUID userId);
}
