package org.univartois.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class ForgotPasswordRequestDto {
    private String email;
}
