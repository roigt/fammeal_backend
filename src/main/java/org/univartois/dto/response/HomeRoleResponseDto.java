package org.univartois.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class HomeRoleResponseDto {
    private String name;
    private String value;
}
