package org.univartois.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.univartois.utils.Constants;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRegisterRequestDto {

    @Email(message = Constants.EMAIL_FORMAT_INVALID)
    @NotBlank(message = Constants.EMAIL_NOT_BLANK)
    private String email;

    @NotBlank(message = Constants.PASSWORD_NOT_BLANK)
    @Size(min = 8, message = Constants.PASSWORD_SIZE)
    private String password;

    @NotBlank(message = Constants.FIRSTNAME_NOT_BLANK)
    @Size(min = 2, max = 16, message = Constants.FIRSTNAME_SIZE)
    private String firstname;

    @NotBlank(message = Constants.LASTNAME_NOT_BLANK)
    @Size(min = 2, max = 16, message = Constants.LASTNAME_SIZE)
    private String lastname;

    public void setFirstname(String firstname) {
        this.firstname = firstname.trim();
    }

    public void setLastname(String lastname) {
        this.lastname = lastname.trim();
    }


    public static class UserRegisterRequestDtoBuilder {
        private String firstname;
        private String lastname;

        public UserRegisterRequestDtoBuilder firstname(String firstname){
            this.firstname = firstname.trim();
            return this;
        }
        public UserRegisterRequestDtoBuilder lastname(String lastname){
            this.lastname = lastname.trim();
            return this;
        }
    }
}
