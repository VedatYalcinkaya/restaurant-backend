package com.demirciyazilim.core.security;

import com.demirciyazilim.core.config.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter authenticationFilter;
    private final AppProperties appProperties;

    public SecurityConfig(
            JwtAuthenticationEntryPoint authenticationEntryPoint,
            JwtAuthenticationFilter authenticationFilter,
            AppProperties appProperties
    ) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
        this.appProperties = appProperties;
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
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/login", "/api/v1/auth/refresh", "/api/v1/auth/logout").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/auth/validate").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/users").hasRole("ADMIN")
                                .requestMatchers("/api/v1/users/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers("/api/v1/files/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/contact-messages").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/contact-messages/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/contact-messages/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/contact-messages/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/menus/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/menus/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/menus/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/menus/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/menus/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.GET, "/api/v1/menu-categories/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/menu-categories/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/menu-categories/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/menu-categories/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/menu-categories/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.POST, "/api/v1/reservations").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/reservations/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/reservations/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/reservations/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/reservations/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/jobs", "/api/v1/jobs/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/jobs/all").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.POST, "/api/v1/jobs/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/jobs/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/jobs/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/jobs/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/job-applications/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/job-applications/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/job-applications/**").hasAnyRole("ADMIN", "EDITOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/job-applications/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                );

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(appProperties.getCors().getAllowedOrigins().stream()
                .filter(StringUtils::hasText)
                .toList());
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
