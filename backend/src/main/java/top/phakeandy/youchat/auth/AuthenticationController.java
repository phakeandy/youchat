package top.phakeandy.youchat.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
    String username = credentials.get("username");
    String password = credentials.get("password");

    return ResponseEntity.ok(Map.of("message", "Login successful for user: " + username,
        "timestamp", System.currentTimeMillis()));
  }

  @PostMapping("/logout")
  public ResponseEntity<Map<String, Object>> logout() {
    return ResponseEntity
        .ok(Map.of("message", "Logout successful", "timestamp", System.currentTimeMillis()));
  }

  @GetMapping("/me")
  public ResponseEntity<Map<String, Object>> getCurrentUser(
      @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return ResponseEntity.ok(Map.of("authenticated", false, "message", "Not authenticated"));
    }

    return ResponseEntity.ok(Map.of("authenticated", true, "username", userDetails.getUsername(),
        "authorities", userDetails.getAuthorities()));
  }
}
