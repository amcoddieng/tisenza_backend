package com.tissenza.tissenza_backend.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Cloudinary pour la gestion des images
 */
@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud_name:}")
    private String cloudName;

    @Value("${cloudinary.api_key:}")
    private String apiKey;

    @Value("${cloudinary.api_secret:}")
    private String apiSecret;

    /**
     * Crée et configure le bean Cloudinary
     */
    @Bean
    public Cloudinary cloudinary() {
        // Configuration individuelle
        return new Cloudinary(
            java.util.Map.of(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
            )
        );
    }
}
