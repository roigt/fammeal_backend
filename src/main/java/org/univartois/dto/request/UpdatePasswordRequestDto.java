package org.univartois.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.univartois.utils.Constants;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class UpdatePasswordRequestDto {
    @NotBlank(message = Constants.OLD_PASSWORD_NOT_BLANK)
    private String oldPassword;

    @NotBlank(message = Constants.NEW_PASSWORD_NOT_BLANK)
    @Size(min = 8, message = Constants.NEW_PASSWORD_SIZE)
    private String newPassword;
}
