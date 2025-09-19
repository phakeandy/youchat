package top.phakeandy.youchat.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/v1/auth/**").permitAll()
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()
            .requestMatchers("/api/public/**").permitAll()
            .anyRequest().authenticated())
        .logout(logout -> logout
            .logoutUrl("/api/v1/auth/logout")
            .logoutSuccessUrl("/api/v1/auth/login?logout=true")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .permitAll())
        .sessionManagement(session -> session
            .sessionFixation().migrateSession()
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false));

    http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
