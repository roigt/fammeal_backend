package org.univartois.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.univartois.dto.request.*;
import org.univartois.dto.response.*;

public interface UserService {
    UserRegisterResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto);

    VerificationAccountResponseDto verifyAccount(String token);

    UserAuthResponseDto auth(UserAuthRequestDto userAuthRequestDto);

    ForgotPasswordResponseDto forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto);

    UserVerificationResponseDto userVerification(UserVerificationRequestDto userVerificationRequestDto);

    void resetPassword(@NotBlank String token);

    UserAuthResponseDto getProfile();

    UpdateProfilePictureResponseDto updateProfilePicture(byte[] image);

    void deleteProfilePicture();

    UserAuthResponseDto updateProfile(UpdateAuthenticatedUserRequestDto updateAuthenticatedUserRequestDto);

    void deleteProfile();

    void updatePassword(@Valid UpdatePasswordRequestDto updatePasswordRequestDto);


    DietaryConstraintsResponseDto updateDietaryConstraints(UpdateDietaryConstraintsRequestDto updateDietaryConstraintsRequestDto);

    DietaryConstraintsResponseDto getDietaryConstraints();
}
