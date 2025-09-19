package top.phakeandy.youchat.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import top.phakeandy.youchat.user.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/**").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll().requestMatchers("/v3/api-docs/**")
            .permitAll().requestMatchers("/api/public/**").permitAll().anyRequest().authenticated())
        .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
            .logoutSuccessUrl("/api/v1/auth/login?logout=true").invalidateHttpSession(true)
            .clearAuthentication(true).permitAll())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).sessionFixation()
            .migrateSession().maximumSessions(1).maxSessionsPreventsLogin(false));

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
    auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    return auth.build();
  }

  @Bean
  public SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

  @Bean
  public SecurityContextHolderStrategy securityContextHolderStrategy() {
    return SecurityContextHolder.getContextHolderStrategy();
  }
}
