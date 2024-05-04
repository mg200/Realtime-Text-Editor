<<<<<<< Updated upstream:src/main/java/com/envn8/app/config/CorsConfig.java
package com.envn8.app.config;

import org.springframework.context.annotation.Bean;
=======
package com.envn8.app.configuration;

>>>>>>> Stashed changes:src/main/java/com/envn8/app/configuration/CorsConfig.java
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") 
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}