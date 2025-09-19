package top.phakeandy.youchat.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository;
  private final SecurityContextHolderStrategy securityContextHolderStrategy;

  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> login(
      @RequestBody Map<String, String> credentials,
      HttpServletRequest request,
      HttpServletResponse response) {
    try {
      // Validate input
      String username = credentials.get("username");
      String password = credentials.get("password");

      if (username == null
          || username.trim().isEmpty()
          || password == null
          || password.trim().isEmpty()) {
        return ResponseEntity.badRequest()
            .body(
                Map.of(
                    "message",
                    "Username and password are required",
                    "authenticated",
                    "timestamp",
                    System.currentTimeMillis()));
      }

      // Create authentication token
      UsernamePasswordAuthenticationToken authenticationToken =
          UsernamePasswordAuthenticationToken.unauthenticated(username, password);

      // Authenticate
      Authentication authentication = authenticationManager.authenticate(authenticationToken);

      // Create security context
      SecurityContext context = securityContextHolderStrategy.createEmptyContext();
      context.setAuthentication(authentication);

      // Set security context
      securityContextHolderStrategy.setContext(context);

      // Save security context to repository (session)
      securityContextRepository.saveContext(context, request, response);

      // Get authenticated user details
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();

      log.info("User {} logged in successfully", username);

      return ResponseEntity.ok(
          Map.of(
              "message",
              "Login successful",
              "authenticated",
              true,
              "username",
              userDetails.getUsername(),
              "authorities",
              userDetails.getAuthorities(),
              "timestamp",
              System.currentTimeMillis()));

    } catch (BadCredentialsException e) {
      log.warn("Login failed for user {}: {}", credentials.get("username"), e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(
              Map.of(
                  "message",
                  "Authentication failed: " + e.getMessage(),
                  "authenticated",
                  false,
                  "timestamp",
                  System.currentTimeMillis()));
    } catch (Exception e) {
      log.error("Unexpected error during login for user {}", credentials.get("username"), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              Map.of(
                  "message",
                  "An unexpected error occurred during authentication",
                  "authenticated",
                  false,
                  "timestamp",
                  System.currentTimeMillis()));
    }
  }

  // @PostMapping("/logout")
  // public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request,
  // HttpServletResponse response) {
  // try {
  // // Clear security context
  // SecurityContext context = securityContextHolderStrategy.getContext();
  // if (context != null && context.getAuthentication() != null) {
  // String username = context.getAuthentication().getName();
  // log.info("User {} logging out", username);
  // }

  // // Clear the security context
  // securityContextHolderStrategy.clearContext();

  // // Save empty context to repository (clears session)
  // securityContextRepository.saveContext(securityContextHolderStrategy.createEmptyContext(),
  // request, response);

  // return ResponseEntity
  // .ok(Map.of("message", "Logout successful", "timestamp",
  // System.currentTimeMillis()));

  // } catch (Exception e) {
  // log.error("Error during logout", e);
  // return
  // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message",
  // "An error occurred during logout", "timestamp", System.currentTimeMillis()));
  // }
  // }

  @GetMapping("/me")
  public ResponseEntity<Map<String, Object>> getCurrentUser(
      @AuthenticationPrincipal Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.ok(Map.of("authenticated", false, "message", "Not authenticated"));
    }

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    return ResponseEntity.ok(
        Map.of(
            "authenticated",
            true,
            "username",
            userDetails.getUsername(),
            "authorities",
            userDetails.getAuthorities()));
  }
}
