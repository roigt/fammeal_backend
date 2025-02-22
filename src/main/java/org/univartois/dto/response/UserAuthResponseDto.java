package org.univartois.dto.response;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.univartois.entity.HomeRoleEntity;
import org.univartois.enums.HomeRoleType;

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
    private Map<String, Set<String>> roles = new HashMap<>();

}
