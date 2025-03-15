package org.univartois.event.handler;

import io.quarkus.mailer.MailTemplate;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.vertx.ConsumeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.univartois.event.ForgotPasswordEvent;
import org.univartois.event.PasswordResetEvent;
import org.univartois.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@ApplicationScoped
public class PasswordEventHandler {

    @CheckedTemplate
    public static class Templates {
        public static native MailTemplate.MailTemplateInstance forgotPasswordMail(String apiBasePath, String firstname, String lastname, String resetPasswordLink);

        public static native MailTemplate.MailTemplateInstance passwordResetMail(String apiBasePath, String firstname, String lastname, String newPassword);

    }


    @Inject
    String apiBasePath;

    @ConsumeEvent(value = Constants.FORGOT_PASSWORD_EVENT)
    public void sendForgotPasswordMail(ForgotPasswordEvent forgotPasswordEvent) {
        String resetPasswordLink = String.format("%s/api/users/resetPassword?token=%s",
                apiBasePath, forgotPasswordEvent.getResetPasswordToken());

        try (InputStream logoStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("META-INF/resources/logo.png")) {

            if (logoStream != null) {
                Templates.forgotPasswordMail(
                                apiBasePath,
                                forgotPasswordEvent.getFirstname(),
                                forgotPasswordEvent.getLastname(),
                                resetPasswordLink
                        )
                        .addInlineAttachment("logo.png", logoStream.readAllBytes(), "image/png", "<logo@fammeal.fr>")
                        .to(forgotPasswordEvent.getEmail())
                        .subject("FamMeal: Demande de réinitialisation de mot de passe")
                        .send()
                        .subscribe().with(
                                success -> log.info("Forgot password email sent successfully to {}", forgotPasswordEvent.getEmail()),
                                failure -> log.error("Failed to send forgot password email to {}", forgotPasswordEvent.getEmail(), failure)
                        );
            } else {
                log.error("Failed to load logo.png from resources");
            }

        } catch (IOException e) {
            log.error("Error reading logo.png", e);
        }
    }


    @ConsumeEvent(value = Constants.RESET_PASSWORD_EVENT)
    public void sendResetPasswordMail(PasswordResetEvent passwordResetEvent) {

        try (InputStream logoStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("META-INF/resources/logo.png")) {

            if (logoStream != null) {
                Templates.passwordResetMail(
                                apiBasePath,
                                passwordResetEvent.getFirstname(),
                                passwordResetEvent.getLastname(),
                                passwordResetEvent.getNewPassword()
                        )
                        .to(passwordResetEvent.getEmail())
                        .subject("FamMeal: Confirmation de réinitialisation du mot de passe")
                        .addInlineAttachment("logo.png", logoStream.readAllBytes(), "image/png", "<logo@fammeal.fr>")
                        .send()
                        .subscribe().with(
                                success -> log.info("Password reset confirmation email sent successfully to {}", passwordResetEvent.getEmail()),
                                failure -> log.error("Failed to send password reset confirmation email to {}", passwordResetEvent.getEmail(), failure)
                        );
            } else {
                log.error("Failed to load logo.png from resources");
            }

        } catch (IOException e) {
            log.error("Error reading logo.png", e);
        }
    }
}