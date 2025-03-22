package org.univartois.event.handler;

import io.quarkus.mailer.MailTemplate;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.vertx.ConsumeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.univartois.event.UserCreatedEvent;
import org.univartois.utils.Constants;

import java.io.IOException;
import java.io.InputStream;


@Slf4j
@ApplicationScoped
public class UserCreatedEventHandler {

    @CheckedTemplate
    public static class Templates {
        public static native MailTemplate.MailTemplateInstance verifyUserMail(String apiBasePath, String firstname, String lastname, String verificationLink);

    }

    @Inject
    String apiBasePath;

    @ConsumeEvent(value = Constants.USER_CREATED_EVENT)
    public void sendVerificationMail(UserCreatedEvent userCreatedEvent) {
        String verificationLink = String.format("%s/api/users/verify?token=%s", apiBasePath, userCreatedEvent.getVerificationToken());

        try (InputStream logoStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("META-INF/resources/logo.png")) {

            if (logoStream != null) {
                Templates.verifyUserMail(
                                apiBasePath,
                                userCreatedEvent.getFirstname(),
                                userCreatedEvent.getLastname(),
                                verificationLink
                        )
                        .addInlineAttachment("logo.png", logoStream.readAllBytes(), "image/png", "<logo@fammeal.fr>")
                        .to(userCreatedEvent.getEmail())
                        .subject("FamMeal: Activation de votre compte")
                        .send()
                        .subscribe().with(
                                success -> log.info("verification email sent successfully to {}", userCreatedEvent.getEmail()),
                                failure -> log.error("Failed to send verification email to {}", userCreatedEvent.getEmail(), failure)
                        );
            } else {
                log.error("Failed to load logo.png from resources");
            }

        } catch (IOException e) {
            log.error("Error reading logo.png", e);
        }
    }
}
