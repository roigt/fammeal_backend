package org.univartois.service.impl;

import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.univartois.dto.request.UserRegisterRequestDto;
import org.univartois.dto.response.UserRegisterResponseDto;
import org.univartois.dto.response.VerificationAccountResponseDto;
import org.univartois.entity.TokenEntity;
import org.univartois.entity.UserEntity;
import org.univartois.enums.TokenType;
import org.univartois.event.UserCreatedEvent;
import org.univartois.exception.TokenInvalidException;
import org.univartois.exception.UserAlreadyExistsException;
import org.univartois.mapper.UserMapper;
import org.univartois.repository.TokenRepository;
import org.univartois.repository.UserRepository;
import org.univartois.service.UserAuthService;
import org.univartois.utils.Constants;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserAuthServiceImpl implements UserAuthService {

    @Inject
    UserRepository userRepository;

    @Inject
    TokenRepository tokenRepository;

    @Inject
    EventBus eventBus;

    @Inject
    UserMapper userMapper;


    @Override
    @Transactional
    public UserRegisterResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        final Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(userRegisterRequestDto.getEmail());

        if (optionalUserEntity.isPresent()) {
            final UserEntity user = optionalUserEntity.get();
            if (
                    user.isVerified()
            ) {
                throw new UserAlreadyExistsException("Un utilisateur avec cet email ou nom d'utilisateur existe déjà.");
            } else {
//                send verification mail again
                final TokenEntity verificationToken = generateVerificationToken();
                user.addToken(verificationToken);
                final UserCreatedEvent userCreatedEvent = UserCreatedEvent.builder()
                        .email(user.getEmail())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .verificationToken(verificationToken.getToken())
                        .build();
                eventBus.publish(Constants.USER_CREATED_EVENT, userCreatedEvent);
                return UserRegisterResponseDto.builder().message("Votre compte n'a pas été encore vérifié. Veuillez vérifier votre e-mail pour activer votre compte.").build();
            }
        }

        final UserEntity user = userMapper.toEntity(userRegisterRequestDto);
        user.setUsername(generateUsername(user.getFirstname(), user.getLastname()));
        final TokenEntity verificationToken = generateVerificationToken();
        user.addToken(verificationToken);
        userRepository.persist(user);

//        publish user created event so that the listener send verification mail
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
                .expiresAt(LocalDateTime.now().plusHours(24))
                .tokenType(TokenType.VERIFICATION_TOKEN)
                .build();
    }

    private String generateUsername(String firstname, String lastname){
        return String.format("%s_%s_%s", firstname, lastname, UUID.randomUUID().toString());
    }


    @Override
    @Transactional
    public VerificationAccountResponseDto verifyAccount(String token) {
        TokenEntity tokenEntity = tokenRepository.findByTokenAndNotUsedAndNotExpired(token).orElseThrow(() -> new TokenInvalidException("Token invalide, expiré ou déjà utilisé"));

        final UserEntity user = tokenEntity.getUser();
        user.setVerified(true);
        tokenRepository.markUserTokensAsUsed(user.getId(), TokenType.VERIFICATION_TOKEN);
        return VerificationAccountResponseDto.builder().message("votre compte est désormais activé !").build();
    }
}
