package org.univartois.event.handler;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.quarkus.vertx.ConsumeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.univartois.event.UserCreatedEvent;
import org.univartois.utils.Constants;


@Slf4j
@ApplicationScoped
public class UserCreatedEventHandler {


    @Inject
    ReactiveMailer reactiveMailer;

    @ConfigProperty(name = "api.base-path")
    String apiBasePath;

    @ConsumeEvent(value = Constants.USER_CREATED_EVENT)
    public void sendVerificationMail(UserCreatedEvent userCreatedEvent) {
        String verificationLink = String.format("%s/api/users/verify?token=%s", apiBasePath, userCreatedEvent.getVerificationToken());

        String htmlContent = "<h1>Bienvenue " + userCreatedEvent.getFirstname() + " " + userCreatedEvent.getLastname() + " au FamMeal !</h1>"
                + "<p>Merci de vous être inscrit. Veuillez cliquer sur le lien ci-dessous pour activer votre compte :</p>"
                + "<p><a href=\"" + verificationLink + "\" style=\"background-color:#4CAF50;color:white;padding:10px 20px;text-decoration:none;"
                + "display:inline-block;font-size:16px;border-radius:5px;\">Activer mon compte</a></p>"
                + "<p>Si vous n'avez pas demandé cette inscription, ignorez simplement cet e-mail.</p>";

        reactiveMailer.send(
                Mail.withHtml(userCreatedEvent.getEmail(), "FamMeal: Activation de votre compte", htmlContent)
        ).subscribe().with(
                success -> log.info("Verification email sent successfully to {}", userCreatedEvent.getEmail()),
                failure -> log.error("Failed to send verification email to {}", userCreatedEvent.getEmail(), failure)
        );
    }
}
