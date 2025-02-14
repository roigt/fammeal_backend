package org.univartois;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        tags = {
                @Tag(name = "users", description = "Users operations."),
                @Tag(name = "homes", description = "Homes operations")
        },
        info = @Info(
                title = "fammeal API",
                version = "1.0.0-SNAPSHOT",
                contact = @Contact(
                        name = "fammeal API Support",
                        url = "http://fammeal.net/contact",
                        email = "contact@fammeal.com")
        )
)
@QuarkusMain
public class Main {

    public static void main(String[] args) {
        Quarkus.run(args);
    }

}
