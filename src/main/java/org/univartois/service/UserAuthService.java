package org.univartois.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.univartois.dto.request.*;
import org.univartois.dto.response.*;

public interface UserAuthService {
    UserRegisterResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto);

    VerificationAccountResponseDto verifyAccount(String token);

    UserAuthResponseDto auth(UserAuthRequestDto userAuthRequestDto);

    ForgotPasswordResponseDto forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto);

    UserVerificationResponseDto userVerification(UserVerificationRequestDto userVerificationRequestDto);

    void resetPassword(@NotBlank String token);

    UserAuthResponseDto getCurentAuthenticatedUser();

    UpdateProfilePictureResponseDto updateProfilePicture(byte[] image);

    void deleteProfilePicture();

    UserAuthResponseDto updateCurrentAuthenticatedUser(UpdateAuthenticatedUserRequestDto updateAuthenticatedUserRequestDto);

    void deleteCurrentAuthenticatedUser();

    void updatePassword(@Valid UpdatePasswordRequestDto updatePasswordRequestDto);
}
