package org.univartois.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.univartois.annotation.validation.HomeRoleTypeSubset;
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

    @HomeRoleTypeSubset(
            anyOf = {HomeRoleType.ADMIN, HomeRoleType.CHEF_REPAS, HomeRoleType.GARDE_MANGER, HomeRoleType.PROPOSITION_REPAS, HomeRoleType.MEMBER},
            message = Constants.HOMEROLETYPE_SUBSET_MSG
    )
    private HomeRoleType role;
}
