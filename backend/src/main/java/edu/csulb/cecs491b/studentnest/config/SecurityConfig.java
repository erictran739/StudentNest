package edu.csulb.cecs491b.studentnest.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain security(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.cors(Customizer.withDefaults());
    http.authorizeHttpRequests(auth -> auth
          .requestMatchers("/auth/**").permitAll()
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
  //
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration cfg = new CorsConfiguration();
      //this is for local (React on 3000, Vite on 5173)
      cfg.setAllowedOriginPatterns(List.of(
//              "http://localhost:[*]",
//              "https://localhost:[*]",
              "http://127.0.0.1:[*]" // This is all I needed to test locally
//              "https://127.0.0.1:[*]"
//              "http://[::1]:[*]",
//              "https://[::1]:[*]",
//              "http://192.168.*.*:[*]",
//              "https://192.168.*.*:[*]"
      )); // allow device in local network
//      cfg.setAllowedOriginPatterns(List.of("*"));
      cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
      cfg.setAllowedHeaders(List.of("*"));
      cfg.setAllowCredentials(true);
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", cfg);
      return source;
  }
  
}
