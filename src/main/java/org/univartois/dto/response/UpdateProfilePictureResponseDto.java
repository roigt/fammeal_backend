package org.univartois.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class UpdateProfilePictureResponseDto {
    private String profilePicture;
}
