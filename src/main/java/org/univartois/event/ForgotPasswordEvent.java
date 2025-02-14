package org.univartois.event;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ForgotPasswordEvent {
    private String email;
    private String firstname;
    private String lastname;
    private String resetPasswordToken;
}
