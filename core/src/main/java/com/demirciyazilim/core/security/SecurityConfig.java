package com.demirciyazilim.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize
                                // Public endpoints
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/login", "/api/v1/auth/refresh", "/api/v1/auth/logout").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/auth/validate").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/users").hasRole("ADMIN")
                                .requestMatchers("/api/v1/users/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers("/api/v1/files/**").permitAll()
                                // Contact messages - public create, admin listing
                                .requestMatchers(HttpMethod.POST, "/api/v1/contact-messages").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/contact-messages/**").hasAnyRole("ADMIN","EDITOR")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/contact-messages/**").hasAnyRole("ADMIN","EDITOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/contact-messages/**").hasRole("ADMIN")
                                // Menu endpoints - GET operations are public, others require authentication
                                .requestMatchers(HttpMethod.GET, "/api/v1/menus/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/menus/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/menus/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/menus/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/menus/**").hasAnyRole("ADMIN", "EDITOR")
                                // Menu Categories endpoints - GET operations are public, others require authentication
                                .requestMatchers(HttpMethod.GET, "/api/v1/menu-categories/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/menu-categories/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/menu-categories/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/menu-categories/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/menu-categories/**").hasAnyRole("ADMIN", "EDITOR")
                                // Reservation endpoints - POST is public (customers can create), others require authentication
                                .requestMatchers(HttpMethod.POST, "/api/v1/reservations").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/reservations/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/reservations/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/reservations/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/reservations/**").hasRole("ADMIN")
                                // Jobs - public listing, admin management
                                .requestMatchers(HttpMethod.GET, "/api/v1/jobs", "/api/v1/jobs/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/jobs/all").hasAnyRole("ADMIN","EDITOR")
                                .requestMatchers(HttpMethod.POST, "/api/v1/jobs/**").hasAnyRole("ADMIN","EDITOR")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/jobs/**").hasAnyRole("ADMIN","EDITOR")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/jobs/**").hasAnyRole("ADMIN","EDITOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/jobs/**").hasRole("ADMIN")
                                // Job Applications - public apply, admin management
                                .requestMatchers(HttpMethod.POST, "/api/v1/job-applications/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/job-applications/**").hasAnyRole("ADMIN","EDITOR")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/job-applications/**").hasAnyRole("ADMIN","EDITOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/job-applications/**").hasRole("ADMIN")
                                // Protected endpoints
                                .anyRequest().authenticated()
                );

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "https://emreokur.av.tr", 
            "https://www.emreokur.av.tr",
            "http://localhost:3000",
            "http://localhost:8080",
            "http://localhost:8082",
            "http://localhost:5173",
            "http://127.0.0.1:5173"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization", 
            "Content-Type", 
            "X-Requested-With", 
            "Accept", 
            "Origin", 
            "Access-Control-Request-Method", 
            "Access-Control-Request-Headers"
        ));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Disposition"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
} 
