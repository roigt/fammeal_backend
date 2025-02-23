package org.univartois.dto.response;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeMemberResponseDto {
    private UUID id;

    private String username;

    private String email;

    private String firstname;

    private String profilePictureUrl;

    private String lastname;

    private boolean vegetarian;

    private boolean verified;

    private String role;
}
