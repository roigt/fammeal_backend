package org.univartois.dto.request;

import lombok.*;
import org.univartois.enums.HomeRoleType;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class UpdateHomeMemberRequestDto {
    private HomeRoleType role;
}
