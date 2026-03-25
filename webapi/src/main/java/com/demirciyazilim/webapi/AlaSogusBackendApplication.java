package com.demirciyazilim.webapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.demirciyazilim"})
@EntityScan(basePackages = {"com.demirciyazilim.entities"})
@EnableJpaRepositories(basePackages = {"com.demirciyazilim.repositories"})
@EnableScheduling
public class AlaSogusBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlaSogusBackendApplication.class, args);
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            connector.setMaxPostSize(20 * 1024 * 1024);
            connector.setMaxParameterCount(1000);
        });
        return factory;
    }
}
