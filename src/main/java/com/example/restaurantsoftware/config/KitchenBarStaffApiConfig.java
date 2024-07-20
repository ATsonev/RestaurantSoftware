package com.example.restaurantsoftware.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "staff.api")
public class KitchenBarStaffApiConfig {
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public KitchenBarStaffApiConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
}
