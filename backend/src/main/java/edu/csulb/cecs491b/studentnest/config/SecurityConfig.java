package edu.csulb.cecs491b.studentnest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

//this is
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(Customizer.withDefaults());
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/api/users/**",
                                "/api/courses/**",
                                "/api/sections/**",
                                "/api/students/**",
                                // Web controller endpoints
                                "/",
                                "/login",
                                "/auth-test",
                                "/profile",
                                // Explicit for now
                                // I think  I have to move .html files to resources/private
                                // so I don't have to use expose them as endpoints
                                "/index.html",
                                "/login.html",
                                "/auth-test.html",
                                "/profile.html",
                                "/landing.html",
                                "/css/**",
                                "/js/**",
                                "/html/**",
                                "/images/**",

                                "/react",
                                "/assets/**"
                        ).permitAll()
//                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                        .requestMatchers("/auth/**").authenticated()
                        .anyRequest().authenticated()

                )
                .httpBasic(b -> b.disable())
                .formLogin(f -> f.disable());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of(
                "https://puggu.dev:*",
                "https://puggu.dev"
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setExposedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);

        return source;
    }

}
