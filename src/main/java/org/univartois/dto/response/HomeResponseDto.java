package org.univartois.dto.response;

import lombok.*;
import org.univartois.enums.HomeRoleType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeResponseDto {

    private UUID id;

    private String name;

    private boolean vegetarian;

    private boolean lunchAutomaticGeneration;

    private boolean dinerAutomaticGeneration;

    private HomeRoleTypeResponseDto role;

}
