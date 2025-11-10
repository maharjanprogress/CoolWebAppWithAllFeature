package com.progress.coolProject.Configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {
    private List<String> allowedOrigins;

    public String[] getAllowedOriginsArray() {
        return allowedOrigins.toArray(new String[0]);
    }
}
