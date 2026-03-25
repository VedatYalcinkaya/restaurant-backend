package com.demirciyazilim.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String restaurantName = "Ala Söğüş";
    private final Contact contact = new Contact();
    private final Cors cors = new Cors();
    private final Openapi openapi = new Openapi();

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Contact getContact() {
        return contact;
    }

    public Cors getCors() {
        return cors;
    }

    public Openapi getOpenapi() {
        return openapi;
    }

    public static class Contact {
        private String displayName = "Ala Söğüş";
        private String notificationEmail = "";
        private String websiteUrl = "";
        private String publicEmail = "";

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getNotificationEmail() {
            return notificationEmail;
        }

        public void setNotificationEmail(String notificationEmail) {
            this.notificationEmail = notificationEmail;
        }

        public String getWebsiteUrl() {
            return websiteUrl;
        }

        public void setWebsiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
        }

        public String getPublicEmail() {
            return publicEmail;
        }

        public void setPublicEmail(String publicEmail) {
            this.publicEmail = publicEmail;
        }
    }

    public static class Cors {
        private List<String> allowedOrigins = new ArrayList<>(List.of(
                "http://localhost:3000",
                "http://localhost:8080",
                "http://localhost:8082",
                "http://localhost:5173",
                "http://127.0.0.1:5173"
        ));

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }
    }

    public static class Openapi {
        private String productionUrl = "";

        public String getProductionUrl() {
            return productionUrl;
        }

        public void setProductionUrl(String productionUrl) {
            this.productionUrl = productionUrl;
        }
    }
}
