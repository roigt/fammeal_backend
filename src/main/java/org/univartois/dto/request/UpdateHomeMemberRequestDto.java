package org.univartois.dto.request;

import lombok.*;
import org.univartois.annotation.validation.HomeRoleTypeSubset;
import org.univartois.enums.HomeRoleType;
import org.univartois.utils.Constants;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class UpdateHomeMemberRequestDto {
    
    @HomeRoleTypeSubset(
            anyOf = {HomeRoleType.ADMIN, HomeRoleType.CHEF_REPAS, HomeRoleType.GARDE_MANGER, HomeRoleType.PROPOSITION_REPAS, HomeRoleType.MEMBER},
            message = Constants.HOMEROLETYPE_SUBSET_MSG
    )
    private HomeRoleType role;
}
