package org.univartois.event;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PasswordResetEvent {
    private String email;
    private String newPassword;
    private String firstname;
    private String lastname;
}
