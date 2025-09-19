package top.phakeandy.youchat.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "认证接口")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/login")
  @Operation(summary = "用户登录")
  public ResponseEntity<LoginResponse> login(
      @Valid @RequestBody LoginRequest loginRequest,
      HttpServletRequest request,
      HttpServletResponse response) {

    LoginResponse loginResponse =
        authenticationService.authenticate(loginRequest, request, response);
    return ResponseEntity.ok(loginResponse);
  }

  @GetMapping("/me")
  @Operation(summary = "获取当前用户信息")
  public ResponseEntity<UserResponse> getCurrentUser(
      @AuthenticationPrincipal Authentication authentication) {

    UserResponse userResponse = authenticationService.getCurrentUser(authentication);
    return ResponseEntity.ok(userResponse);
  }
}
