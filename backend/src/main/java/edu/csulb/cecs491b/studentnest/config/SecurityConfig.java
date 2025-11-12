package edu.csulb.cecs491b.studentnest.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import edu.csulb.cecs491b.studentnest.repository.UserRepository;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        http.authorizeHttpRequests(auth -> auth
                        // public/dev endpoints
                        .requestMatchers(
                                "/auth/**",
                                "/api/users/**",
                                "/api/courses/**",
                                "/api/sections/**",
                                "/api/students/**",
                                "/api/professors/**",
                                "/api/departments/**",
                                // Web controller endpoints
                                "/",
                                "/login",
                                "/auth-test",
                                "/profile",
                                // Explicit for now
                                // I think  I have to move .html files to resources/private
                                // so I don't have to use expose them as endpoints
                                "/index.html",
                                "/auth-test.html",
                                "/landing.html",
                                "/css/**",
                                "/js/**",
                                "/html/**",
                                "/images/**",
                                // For React
                                "/react",
                                "/assets/**",
                                // Admin endpoints
                                "/api/admin/**"
                        ).permitAll()
                        // Admin-only API
//                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);
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
                "https://puggu.dev",
                "http://localhost:*"
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    //This bean tells Spring Security to use your UserRepository
    //to find users by email when someone logs in via Basic Auth
    @Bean
    public UserDetailsService dbUserDetailsService(UserRepository users) {
        return username -> users.findByEmail(username)
                .map(u -> {
                    // Determine the role based on the entity type
                    String role = switch (u.getClass().getSimpleName()) {
                        case "SystemAdmin", "Admin", "DepartmentChair" -> "ADMIN";
                        case "Professor" -> "PROFESSOR";
                        default -> "STUDENT";
                    };

                    return org.springframework.security.core.userdetails.User
                            .withUsername(u.getEmail())
                            .password(u.getPassword())   // already BCrypt-hashed in DB
                            .roles(role)
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
