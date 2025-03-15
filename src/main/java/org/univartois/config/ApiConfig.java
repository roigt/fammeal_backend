package org.univartois.config;

import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;

public class ApiConfig {

    @ConfigProperty(name = "api.base-path", defaultValue = "localhost:8080")
    String basePath;


    public String determineApiBasePath(String domain) {
        if (basePath != null) {
            if (basePath.contains("http")) {
                return basePath;
            }
            if (basePath.contains("ngrok.io") || basePath.matches(".*\\..*")) {
                basePath = "https://" + basePath;
            } else {
                basePath = "http://" + basePath;
            }
        } else {
            basePath = "http://localhost:8080";
        }
        return basePath;
    }

    @Produces
    public String apiBasePath() {
        return determineApiBasePath(basePath);
    }

}
