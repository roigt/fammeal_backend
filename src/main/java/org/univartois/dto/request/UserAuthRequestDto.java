package org.univartois.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class UserAuthRequestDto {

    @Email(message = "Adresse mail n'est pas valide")
    @NotBlank(message = "Adresse mail ne doit pas etre vide")
    private String email;

    @NotBlank(message = "Password ne doit pas etre vide")
    private String password;
}
