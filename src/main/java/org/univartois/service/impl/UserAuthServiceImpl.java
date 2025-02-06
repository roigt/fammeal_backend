package org.univartois.service.impl;

import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.univartois.dto.request.UserRegisterRequestDto;
import org.univartois.dto.response.TokenRepository;
import org.univartois.dto.response.UserRegisterResponseDto;
import org.univartois.dto.response.VerificationAccountResponseDto;
import org.univartois.entity.TokenEntity;
import org.univartois.entity.UserEntity;
import org.univartois.enums.TokenType;
import org.univartois.event.UserCreatedEvent;
import org.univartois.exception.TokenInvalidException;
import org.univartois.exception.UserAlreadyExistsException;
import org.univartois.mapper.UserMapper;
import org.univartois.repository.UserRepository;
import org.univartois.service.UserAuthService;
import org.univartois.utils.Constants;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class UserAuthServiceImpl implements UserAuthService {

    @Inject
    UserRepository userRepository;

    @Inject
    TokenRepository tokenRepository;

    @Inject
    EventBus eventBus;


    @Override
    @Transactional
    public UserRegisterResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        userRepository.findByEmailOrUsername(userRegisterRequestDto.getEmail(), userRegisterRequestDto.getUsername()).ifPresent((user) -> {
            throw new UserAlreadyExistsException("Un utilisateur avec cet email ou nom d'utilisateur existe déjà");
        });

        final UserEntity user = UserMapper.INSTANCE.toEntity(userRegisterRequestDto);
        final TokenEntity verificationToken = generateVerificationToken();
        user.addToken(verificationToken);
        userRepository.persist(user);

//        publish user created event
        final UserCreatedEvent userCreatedEvent = UserCreatedEvent.builder()
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .verificationToken(verificationToken.getToken())
                .build();
        eventBus.publish(Constants.USER_CREATED_EVENT, userCreatedEvent);


        return UserRegisterResponseDto.builder().message("Votre compte a été créé avec succès. Veuillez vérifier votre e-mail pour activer votre compte.").build();
    }

    private TokenEntity generateVerificationToken() {
        return TokenEntity.builder()
                .token(UUID.randomUUID().toString())
                .used(false)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .tokenType(TokenType.VERIFICATION_TOKEN)
                .build();
    }


    @Override
    @Transactional
    public VerificationAccountResponseDto verifyAccount(String token) {
        TokenEntity tokenEntity = tokenRepository.findByTokenAndNotUsedAndNotExpired(token).orElseThrow(() -> new TokenInvalidException("Token invalide, expiiré ou déjà utilisé"));

        final UserEntity user = tokenEntity.getUser();
        user.setVerified(true);
        tokenEntity.setUsed(true);
        return VerificationAccountResponseDto.builder().message("votre compte est désormais activé !").build();
    }
}
