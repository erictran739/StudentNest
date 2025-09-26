package edu.csulb.cecs491b.studentnest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())                 // for Postman testing
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()  // allow register/login
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());         // simple 401s for blocked URLs
        return http.build();
    }
}
