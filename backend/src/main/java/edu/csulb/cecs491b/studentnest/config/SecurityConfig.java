package edu.csulb.cecs491b.studentnest.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import edu.csulb.cecs491b.studentnest.repository.UserRepository;


//this is
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	 @Bean
	  public SecurityFilterChain security(HttpSecurity http) throws Exception {
	    http
	      .csrf(csrf -> csrf.disable())
	      .cors(Customizer.withDefaults())
	      .authorizeHttpRequests(auth -> auth
	          // public/dev endpoints
	          .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
	          .requestMatchers("/auth/**", "/auth-test.html", "/admin-tools.html",
	                           "/css/**", "/js/**", "/images/**").permitAll()

	          // Admin-only API
	          .requestMatchers("/api/admin/**").hasRole("ADMIN")
	          .requestMatchers("/api/departments/**").hasRole("ADMIN")

	          // Open the users API for our tester HTML (dev only)
	          .requestMatchers("/api/users/**").permitAll()

//	          .anyRequest().permitAll()
	          .anyRequest().authenticated()
	      )
	      .httpBasic(Customizer.withDefaults())   // keep ENABLED
	      .formLogin(f -> f.disable());

	    return http.build();
	  }

  
//In-memory admin for demo/dev
// @Bean
// public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//   UserDetails admin = User.builder()
//       .username("admin")
//       .password(encoder.encode("admin123")) // change for your demo
//       .roles("ADMIN")
//       .build();
//   return new InMemoryUserDetailsManager(admin);
// }
  
  @Bean
  public PasswordEncoder passwordEncoder() {
	  return new BCryptPasswordEncoder();
  }
  //
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration cfg = new CorsConfiguration();
      //this is for local (React on 3000, Vite on 5173)
      cfg.setAllowedOriginPatterns(List.of("http://localhost:5173", "http://localhost:*", "https://*.ngrok-free.app","https://*.ngrok.app", "http://127.0.0.1:*", "http://192.168.*.*:*")); // allow device in local network
      cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
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
