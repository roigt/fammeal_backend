package org.univartois.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserVerificationRequestDto {
    private String email;
}