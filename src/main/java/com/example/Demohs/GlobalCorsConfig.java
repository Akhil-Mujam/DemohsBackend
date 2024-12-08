package com.example.Demohs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("https://shhsadmin-flax.vercel.app"); // Add the URL of your React frontend
        config.addAllowedHeader("*"); // Allows all headers
        config.addAllowedMethod("*"); // Allows all HTTP methods (GET, POST, PUT, DELETE, etc.)
        config.setAllowCredentials(true); // Allows cookies and credentials if needed

        // Create a source and register the CORS configuration
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply CORS settings to all endpoints

        return source; // Return the CorsConfigurationSource
    }
}
