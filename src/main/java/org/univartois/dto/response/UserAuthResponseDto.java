package org.univartois.dto.response;

import lombok.*;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class UserAuthResponseDto {

    private UUID id;

    private String accessToken;

    private String username;

    private String email;

    private String firstname;

    private String profilePictureUrl;

    private String lastname;

    private boolean vegetarian = false;

    private boolean verified = false;

    @Builder.Default
    private Map<String, String> roles = new HashMap<>();

}
