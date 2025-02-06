package org.univartois.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRegisterRequestDto {

    @Email(message = "Adresse mail n'est pas valide")
    @NotBlank(message = "Adresse mail ne doit pas etre vide")
    private String email;

    @NotBlank(message = "Username ne doit pas etre vide")
    private String username;

    @NotBlank(message = "Password ne doit pas etre vide")
    private String password;

    @NotBlank(message = "Nom ne doit pas etre vide")
    private String firstname;

    @NotBlank(message = "Prénom ne doit pas etre vide")
    private String lastname;

}
