// package com.demirciyazilim.webapi.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.filter.CorsFilter;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// import java.util.Arrays;

// @Configuration
// public class CorsConfig implements WebMvcConfigurer {

//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//                 .allowedOrigins("https://emreokur.av.tr", "https://www.emreokur.av.tr")
//                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                 .allowedHeaders("*")
//                 .allowCredentials(true)
//                 .maxAge(3600);
//     }

//     @Bean
//     public CorsFilter corsFilter() {
//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         CorsConfiguration config = new CorsConfiguration();
        
//         // Sadece emreokur.av.tr domainini kabul et
//         config.setAllowedOrigins(Arrays.asList("https://emreokur.av.tr", "https://www.emreokur.av.tr"));
        
//         // Tüm HTTP metodlarını kabul et
//         config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
//         // Tüm başlıkları kabul et
//         config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        
//         // Credentials bilgilerini kabul et (örn. cookies)
//         config.setAllowCredentials(true);
        
//         source.registerCorsConfiguration("/**", config);
//         return new CorsFilter(source);
//     }
// } 