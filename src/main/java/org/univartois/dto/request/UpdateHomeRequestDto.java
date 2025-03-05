package org.univartois.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.univartois.utils.Constants;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class UpdateHomeRequestDto {

    @NotBlank(message = Constants.HOME_NAME_NOT_BLANK)
    @Size(min = 2,max =30 , message = Constants.HOME_NAME_SIZE)
    private String name;
}
