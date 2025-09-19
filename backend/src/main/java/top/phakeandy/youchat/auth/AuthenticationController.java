package top.phakeandy.youchat.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
@Tag(name = "Authentication", description = "认证接口")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository;
  private final SecurityContextHolderStrategy securityContextHolderStrategy;

  @PostMapping("/login")
  @Operation(summary = "用户登录")
  public ResponseEntity<LoginResponse> login(
      @Valid @RequestBody LoginRequest loginRequest,
      HttpServletRequest request,
      HttpServletResponse response) {

    // Create authentication token
    UsernamePasswordAuthenticationToken authenticationToken =
        UsernamePasswordAuthenticationToken.unauthenticated(
            loginRequest.username(), loginRequest.password());

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

    log.info("User {} logged in successfully", loginRequest.username());

    return ResponseEntity.ok(
        new LoginResponse(userDetails.getUsername(), userDetails.getAuthorities()));
  }

  @GetMapping("/me")
  @Operation(summary = "获取当前用户信息")
  public ResponseEntity<UserResponse> getCurrentUser(
      @AuthenticationPrincipal Authentication authentication) {

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new org.springframework.security.authentication
          .AuthenticationCredentialsNotFoundException("用户未认证");
    }

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    return ResponseEntity.ok(
        new UserResponse(userDetails.getUsername(), userDetails.getAuthorities()));
  }
}
