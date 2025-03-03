package org.univartois.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.univartois.utils.Constants;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateAuthenticatedUserRequestDto {

    @NotBlank(message = Constants.FIRSTNAME_NOT_BLANK)
    @Size(min = 2, max = 16, message = Constants.FIRSTNAME_SIZE)
    private String firstname;

    @NotBlank(message = Constants.LASTNAME_NOT_BLANK)
    @Size(min = 2, max = 16, message = Constants.LASTNAME_SIZE)
    private String lastname;

    @NotBlank(message = Constants.USERNAME_NOT_BLANK)
    @Pattern(regexp = "^\\S{5,40}$", message = Constants.USERNAME_FORMAT_INVALID)
    private String username;
}
