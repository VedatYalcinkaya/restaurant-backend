package com.demirciyazilim.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfig {

    private final AppProperties appProperties;

    public OpenApiConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact().name(resolveContactName());

        if (StringUtils.hasText(appProperties.getContact().getWebsiteUrl())) {
            contact.setUrl(appProperties.getContact().getWebsiteUrl());
        }

        if (StringUtils.hasText(appProperties.getContact().getPublicEmail())) {
            contact.setEmail(appProperties.getContact().getPublicEmail());
        }

        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url("http://localhost:8082").description("Geliştirme Sunucusu"));

        if (StringUtils.hasText(appProperties.getOpenapi().getProductionUrl())) {
            servers.add(new Server()
                    .url(appProperties.getOpenapi().getProductionUrl())
                    .description("Üretim Sunucusu"));
        }

        return new OpenAPI()
                .info(new Info()
                        .title(appProperties.getRestaurantName() + " API")
                        .description(appProperties.getRestaurantName() + " restoranı için REST API")
                        .version("v1.0")
                        .contact(contact)
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(servers)
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    private String resolveContactName() {
        if (StringUtils.hasText(appProperties.getContact().getDisplayName())) {
            return appProperties.getContact().getDisplayName();
        }

        return appProperties.getRestaurantName();
    }
}
