package org.univartois.dto.response;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class DietaryConstraintsResponseDto {
    private boolean vegetarian;

    @Builder.Default
    private Set<AllergyResponseDto> allergies = new HashSet<>();
}
