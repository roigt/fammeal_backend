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
import org.univartois.dto.request.UserVerificationRequestDto;
import org.univartois.dto.response.*;
import org.univartois.entity.TokenEntity;
import org.univartois.entity.UserEntity;
import org.univartois.enums.TokenType;
import org.univartois.event.ForgotPasswordEvent;
import org.univartois.event.PasswordResetEvent;
import org.univartois.event.UserCreatedEvent;
import org.univartois.exception.*;
import org.univartois.mapper.UserMapper;
import org.univartois.repository.TokenRepository;
import org.univartois.repository.UserRepository;
import org.univartois.service.RoleService;
import org.univartois.service.UserAuthService;
import org.univartois.utils.Constants;
import org.univartois.utils.JwtTokenUtil;
import org.univartois.utils.PasswordGenerator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class UserAuthServiceImpl implements UserAuthService {

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

    @Inject
    RoleService roleService;

    @Override
    @Transactional
    public UserRegisterResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        final Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(userRegisterRequestDto.getEmail());

        if (optionalUserEntity.isPresent()) {
            final UserEntity user = optionalUserEntity.get();
            if (user.isVerified()) {
                throw new UserAlreadyExistsException(Constants.EMAIL_ALREADY_EXISTS_MSG);
            } else {
                throw new UserNotVerifiedException(Constants.ACCOUNT_NOT_VERIFIED_MSG);
            }
        }

        final UserEntity user = userMapper.toEntity(userRegisterRequestDto);
        user.setUsername(generateUsername(user.getFirstname(), user.getLastname()));
        userRepository.persist(user);

        return UserRegisterResponseDto.builder().message(Constants.ACCOUNT_CREATED_MSG).build();
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
        TokenEntity tokenEntity = tokenRepository.findValidToken(token, TokenType.VERIFICATION_TOKEN).orElseThrow(() -> new TokenInvalidException(Constants.TOKEN_INVALID_MSG));

        final UserEntity user = tokenEntity.getUser();
        user.setVerified(true);
        tokenRepository.markUserTokensAsUsed(user.getId(), TokenType.VERIFICATION_TOKEN);
        return VerificationAccountResponseDto.builder().message(Constants.ACCOUNT_ACTIVATED_MSG).build();
    }

    @Override
    @Transactional
    public UserAuthResponseDto auth(UserAuthRequestDto userAuthRequestDto) {
        UserEntity user = userRepository.findByEmail(userAuthRequestDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException(Constants.EMAIL_INVALID_MSG));

        if (!BcryptUtil.matches(userAuthRequestDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(Constants.PASSWORD_INVALID_MSG);
        }

        else if (!user.isVerified()) {
            throw new UserNotVerifiedException(Constants.ACCOUNT_NOT_VERIFIED_MSG);
        }

        String accessToken = jwtTokenUtil.generateJwtToken(user);
        Map<String, Set<String>> roles = roleService.getRolesByUserId(user.getId());

         return userMapper.toAuthResponseDto(user, accessToken, roles);
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
        final UserEntity user = userRepository.findByEmail(forgotPasswordRequestDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException(Constants.EMAIL_INVALID_MSG));

        final TokenEntity resetPasswordToken = createToken(TokenType.RESET_PASSWORD_TOKEN);
        user.addToken(resetPasswordToken);
        publishForgotPasswordEvent(user, resetPasswordToken);
        return ForgotPasswordResponseDto.builder().message(Constants.RESET_PASSWORD_EMAIL_SENT_MSG).build();
    }

    @Override
    @Transactional
    public UserVerificationResponseDto userVerification(UserVerificationRequestDto userVerificationRequestDto) {
        final UserEntity user = userRepository.findByEmail(userVerificationRequestDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException(Constants.EMAIL_INVALID_MSG));
        if(user.isVerified()) {
            throw new UserAlreadyVerifiedException(Constants.ACCOUNT_ALREADY_VERIFIED_MSG);
        }
        final TokenEntity verificationToken = createToken(TokenType.VERIFICATION_TOKEN);
        user.addToken(verificationToken);
        publishUserCreatedEvent(user, verificationToken);
        return UserVerificationResponseDto.builder().message(Constants.EMAIL_VERIFICATION_MSG).build();
    }


    @Transactional
    @Override
    public void resetPassword(String token) {
        TokenEntity tokenEntity = tokenRepository.findValidToken(token, TokenType.RESET_PASSWORD_TOKEN).orElseThrow(() -> new TokenInvalidException(Constants.TOKEN_INVALID_MSG));
        final UserEntity user = tokenEntity.getUser();
        String newPassword = PasswordGenerator.generateRandomPassword(10);
        user.setPassword(BcryptUtil.bcryptHash(newPassword));
        tokenRepository.markUserTokensAsUsed(user.getId(), TokenType.RESET_PASSWORD_TOKEN);
        publishResetPasswordEvent(user, newPassword);
    }


    @Override
    public UserAuthResponseDto getUserById(UUID userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND_MSG));
        return userMapper.toAuthResponseDto(user, null, roleService.getRolesByUserId(user.getId()));
    }
}
