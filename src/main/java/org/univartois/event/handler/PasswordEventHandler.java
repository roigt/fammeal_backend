package org.univartois.event.handler;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.quarkus.vertx.ConsumeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.univartois.event.ForgotPasswordEvent;
import org.univartois.event.PasswordResetEvent;
import org.univartois.utils.Constants;

@Slf4j
@ApplicationScoped
public class PasswordEventHandler {

    @Inject
    ReactiveMailer reactiveMailer;

    @ConfigProperty(name = "api.base-path")
    String apiBasePath;

    @ConsumeEvent(value = Constants.FORGOT_PASSWORD_EVENT)
    public void sendForgotPasswordMail(ForgotPasswordEvent forgotPasswordEvent) {
        String resetPasswordLink = String.format("%s/api/users/resetPassword?token=%s", apiBasePath, forgotPasswordEvent.getResetPasswordToken());

        String htmlContent = "<h1>Demande de réinitialisation de mot de passe</h1>"
                + "<p>Bonjour " + forgotPasswordEvent.getFirstname() + " " + forgotPasswordEvent.getLastname() + ",</p>"
                + "<p>Nous avons reçu une demande de réinitialisation de votre mot de passe.</p>"
                + "<p>Pour réinitialiser votre mot de passe, cliquez sur le lien suivant :</p>"
                + "<p><a href=\"" + resetPasswordLink + "\" style=\"background-color:#4CAF50;color:white;padding:10px 20px;text-decoration:none;"
                + "display:inline-block;font-size:16px;border-radius:5px;\">Réinitialiser mon mot de passe</a></p>"
                + "<p>Si vous n'avez pas demandé cette réinitialisation, vous pouvez ignorer cet e-mail.</p>";

        reactiveMailer.send(
                Mail.withHtml(
                        forgotPasswordEvent.getEmail(),
                        "FamMeal: Demande de réinitialisation de mot de passe",
                        htmlContent
                )
        ).subscribe().with(
                success -> log.info("Forgot password email sent successfully to {}", forgotPasswordEvent.getEmail()),
                failure -> log.error("Failed to send forgot password email to {}", forgotPasswordEvent.getEmail(), failure)
        );
    }


    @ConsumeEvent(value = Constants.RESET_PASSWORD_EVENT)
    public void sendResetPasswordMail(PasswordResetEvent passwordResetEvent) {
        String htmlContent = "<h1>Votre mot de passe a été réinitialisé</h1>"
                + "<p>Bonjour " + passwordResetEvent.getFirstname() + " " + passwordResetEvent.getLastname() + ",</p>"
                + "<p>Votre mot de passe a bien été réinitialisé avec succès.</p>"
                + "<p>Voici votre nouveau mot de passe :</p>"
                + "<p style=\"font-size:18px;color:#4CAF50;font-weight:bold;\">" + passwordResetEvent.getNewPassword() + "</p>"
                + "<p>Nous vous recommandons de le modifier dès que possible pour plus de sécurité.</p>"
                + "<p>Si vous n'êtes pas à l'origine de cette modification, veuillez nous contacter immédiatement.</p>"
                + "<p>Merci de faire confiance à FamMeal.</p>";

        reactiveMailer.send(
                Mail.withHtml(
                        passwordResetEvent.getEmail(),
                        "FamMeal: Confirmation de réinitialisation du mot de passe",
                        htmlContent
                )
        ).subscribe().with(
                success -> log.info("Password reset confirmation email sent successfully to {}", passwordResetEvent.getEmail()),
                failure -> log.error("Failed to send password reset confirmation email to {}", passwordResetEvent.getEmail(), failure)
        );
    }
}