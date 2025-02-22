package org.univartois.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.univartois.enums.HomeRoleType;
import org.univartois.utils.Constants;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddHomeMemberRequestDto {

    @Email(message = Constants.EMAIL_FORMAT_INVALID)
    @NotBlank(message = Constants.EMAIL_NOT_BLANK)
    private String email;

    private HomeRoleType role;
}
