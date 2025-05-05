package com.example.Demohs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Dynamically fetch allowed origins from environment or properties
        List<String> allowedOrigins = List.of(
                "http://localhost:5173",
                "http://localhost:3000",
                "http://localhost:3001",
                "https://adminshhs.vercel.app",
                "https://shhsgdk.in",
                "https://shhsadmin-master-production.up.railway.app",
                "https://shhsadmin-production.up.railway.app",
                "https://shhsadmin-flax.vercel.app"
        );
        config.setAllowedOrigins(allowedOrigins);

        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept")); // Specify needed headers
        config.setAllowedMethods(Collections.singletonList("*"));// Explicitly list methods
        config.setAllowCredentials(true); // Allow sending cookies
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        // Register the CORS configuration
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source; // Return the CorsConfigurationSource
    }
}
