package org.univartois.service.impl;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.UnauthorizedException;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.univartois.dto.request.ForgotPasswordRequestDto;
import org.univartois.dto.request.UserAuthRequestDto;
import org.univartois.dto.request.UserRegisterRequestDto;
import org.univartois.dto.response.ForgotPasswordResponseDto;
import org.univartois.dto.response.UserAuthResponseDto;
import org.univartois.dto.response.UserRegisterResponseDto;
import org.univartois.dto.response.VerificationAccountResponseDto;
import org.univartois.entity.TokenEntity;
import org.univartois.entity.UserEntity;
import org.univartois.enums.TokenType;
import org.univartois.event.ForgotPasswordEvent;
import org.univartois.event.PasswordResetEvent;
import org.univartois.event.UserCreatedEvent;
import org.univartois.exception.ResourceNotFoundException;
import org.univartois.exception.TokenInvalidException;
import org.univartois.exception.UserAlreadyExistsException;
import org.univartois.exception.UserNotVerifiedException;
import org.univartois.mapper.UserMapper;
import org.univartois.repository.TokenRepository;
import org.univartois.repository.UserRepository;
import org.univartois.service.UserAuthService;
import org.univartois.utils.Constants;
import org.univartois.utils.JwtTokenUtil;
import org.univartois.utils.PasswordGenerator;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TransferQueue;

@ApplicationScoped
public class UserAuthServiceImpl implements UserAuthService {

    private static final String EMAIL_ALREADY_EXISTS_MSG = "Un utilisateur avec cet email ou nom d'utilisateur existe déjà.";
    private static final String ACCOUNT_NOT_VERIFIED_MSG = "Votre compte n'a pas été encore vérifié. Veuillez vérifier votre e-mail pour activer votre compte.";
    private static final String EMAIL_INVALID_MSG = "adresse mail invalide. Veuillez svp créer un compte.";
    private static final String PASSWORD_INVALID_MSG = "mot de passe invalide !";

    private static final int DEFAULT_TOKEN_EXPIRATION_HOURS = 24;


    @Inject
    UserRepository userRepository;

    @Inject
    TokenRepository tokenRepository;

    @Inject
    EventBus eventBus;

    @Inject
    UserMapper userMapper;

    @Inject
    JwtTokenUtil jwtTokenUtil;


    private void handleUnverifiedUser(UserEntity user) {
//            send verification mail again
        final TokenEntity verificationToken = createToken(TokenType.VERIFICATION_TOKEN);
        user.addToken(verificationToken);
        publishUserCreatedEvent(user, verificationToken);
    }

    @Override
    @Transactional
    public UserRegisterResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        final Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(userRegisterRequestDto.getEmail());

        if (optionalUserEntity.isPresent()) {
            final UserEntity user = optionalUserEntity.get();
            if (user.isVerified()) {
                throw new UserAlreadyExistsException(EMAIL_ALREADY_EXISTS_MSG);
            } else {
//                send verification mail again
                handleUnverifiedUser(user);
                throw new UserNotVerifiedException(ACCOUNT_NOT_VERIFIED_MSG);
            }
        }

        final UserEntity user = userMapper.toEntity(userRegisterRequestDto);
        user.setUsername(generateUsername(user.getFirstname(), user.getLastname()));
        final TokenEntity verificationToken = createToken(TokenType.VERIFICATION_TOKEN);
        user.addToken(verificationToken);
        userRepository.persist(user);

//        publish user created event so that the listener send verification mail
        publishUserCreatedEvent(user, verificationToken);


        return UserRegisterResponseDto.builder().message("Votre compte a été créé avec succès. Veuillez vérifier votre e-mail pour activer votre compte.").build();
    }

    private void publishUserCreatedEvent(UserEntity user, TokenEntity verificationToken) {
        final UserCreatedEvent userCreatedEvent = UserCreatedEvent.builder()
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .verificationToken(verificationToken.getToken())
                .build();
        eventBus.publish(Constants.USER_CREATED_EVENT, userCreatedEvent);
    }

    private TokenEntity createToken(TokenType tokenType) {
        return TokenEntity.builder()
                .token(UUID.randomUUID().toString())
                .used(false)
                .expiresAt(LocalDateTime.now().plusHours(DEFAULT_TOKEN_EXPIRATION_HOURS))
                .tokenType(tokenType)
                .build();
    }

    private String generateUsername(String firstname, String lastname) {
        return String.format("%s_%s_%s", firstname, lastname, UUID.randomUUID().toString());
    }


    @Override
    @Transactional
    public VerificationAccountResponseDto verifyAccount(String token) {
        TokenEntity tokenEntity = tokenRepository.findValidToken(token, TokenType.VERIFICATION_TOKEN).orElseThrow(() -> new TokenInvalidException("Token invalide, expiré ou déjà utilisé"));

        final UserEntity user = tokenEntity.getUser();
        user.setVerified(true);
        tokenRepository.markUserTokensAsUsed(user.getId(), TokenType.VERIFICATION_TOKEN);
        return VerificationAccountResponseDto.builder().message("votre compte est désormais activé !").build();
    }

    @Override
    @Transactional
    public UserAuthResponseDto auth(UserAuthRequestDto userAuthRequestDto) {
        UserEntity user = userRepository.findByEmail(userAuthRequestDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException(EMAIL_INVALID_MSG));
        if (!user.isVerified()) {
            handleUnverifiedUser(user);
            throw new UserNotVerifiedException(ACCOUNT_NOT_VERIFIED_MSG);
        }

        if (!BcryptUtil.matches(userAuthRequestDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(PASSWORD_INVALID_MSG);
        }

        String accessToken = jwtTokenUtil.generateJwtToken(user);
        return userMapper.toAuthResponseDto(user, accessToken);
    }

    private void publishForgotPasswordEvent(UserEntity user, TokenEntity resetPasswordToken) {
        final ForgotPasswordEvent forgotPasswordEvent = ForgotPasswordEvent.builder()
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .resetPasswordToken(resetPasswordToken.getToken())
                .build();
        eventBus.publish(Constants.FORGOT_PASSWORD_EVENT, forgotPasswordEvent);
    }

    private void publishResetPasswordEvent(UserEntity user, String newPassword) {
        final PasswordResetEvent passwordResetEvent = PasswordResetEvent.builder()
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .newPassword(newPassword)
                .build();
        eventBus.publish(Constants.RESET_PASSWORD_EVENT, passwordResetEvent);
    }


    @Transactional
    @Override
    public ForgotPasswordResponseDto forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto) {
        final UserEntity user = userRepository.findByEmail(forgotPasswordRequestDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("adresse mail invalide."));
        if (!user.isVerified()) {
            handleUnverifiedUser(user);
            throw new UserNotVerifiedException(ACCOUNT_NOT_VERIFIED_MSG);
        }
        final TokenEntity resetPasswordToken = createToken(TokenType.RESET_PASSWORD_TOKEN);
        user.addToken(resetPasswordToken);
        publishForgotPasswordEvent(user, resetPasswordToken);
        return ForgotPasswordResponseDto.builder().message("Veuillez svp vérifier votre boite mail pour réinitialiser votre mot de passe.").build();
    }

//     @TODO: add a password generator
    @Transactional
    @Override
    public void resetPassword(String token) {
        TokenEntity tokenEntity = tokenRepository.findValidToken(token, TokenType.RESET_PASSWORD_TOKEN).orElseThrow(() -> new TokenInvalidException("Token invalide, expiré ou déjà utilisé"));
        final UserEntity user = tokenEntity.getUser();
        String newPassword = PasswordGenerator.generateRandomPassword(10);
        user.setPassword(BcryptUtil.bcryptHash(newPassword));
        tokenRepository.markUserTokensAsUsed(user.getId(), TokenType.RESET_PASSWORD_TOKEN);
        publishResetPasswordEvent(user, newPassword);
    }
}
