package org.univartois.config;

import com.cloudinary.Cloudinary;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.HashMap;
import java.util.Map;

public class CloudinaryConfig {

    private final String CLOUD_NAME;
    private final String API_KEY;
    private final String API_SECRET;

    public CloudinaryConfig(
            @ConfigProperty(name = "cloudinary.cloud_name") String cloudName,
            @ConfigProperty(name = "cloudinary.api_key") String apiKey,
            @ConfigProperty(name = "cloudinary.api_secret") String apiSecret
    ) {
        CLOUD_NAME = cloudName;
        API_KEY = apiKey;
        API_SECRET = apiSecret;
    }

    @Produces
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        return new Cloudinary(config);
    }
}
