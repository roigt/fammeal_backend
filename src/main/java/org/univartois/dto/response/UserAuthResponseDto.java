package org.univartois.dto.response;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.univartois.entity.HomeRoleEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    private String lastname;

    private boolean isVegetarian = false;

    private boolean isVerified = false;

}
