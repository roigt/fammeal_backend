package org.univartois.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class AllergyResponseDto {
    private long id;
    private String name;
}
