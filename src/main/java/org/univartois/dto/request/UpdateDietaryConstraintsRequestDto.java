package org.univartois.dto.request;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class UpdateDietaryConstraintsRequestDto {
    private boolean vegetarian;
    private Set<Long> allergies;
}
