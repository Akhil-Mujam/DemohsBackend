package com.example.Demohs.Config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dyt9liis2",
                "api_key", "557793414684643",
                "api_secret", "GD0UJ_42a0yldIrBaEQmU75-52Q"
        ));
    }
}
