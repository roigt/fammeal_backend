package org.univartois.dto.response;

import lombok.*;

import java.util.HashSet;
import java.util.Set;
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

    private String lastname;

    private boolean vegetarian;

    private boolean verified;

    @Builder.Default
    private Set<String> roles = new HashSet<>();
}
