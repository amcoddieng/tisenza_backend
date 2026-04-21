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

    @Value("${CLOUDINARY_URL}")
    private String cloudinaryUrl;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    /**
     * Crée et configure le bean Cloudinary
     */
    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
        
        // Configuration alternative si l'URL n'est pas disponible
        if (cloudinary.config == null || cloudinary.config.cloudName == null) {
            cloudinary = new Cloudinary(
                java.util.Map.of(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret
                )
            );
        }
        
        return cloudinary;
    }
}
