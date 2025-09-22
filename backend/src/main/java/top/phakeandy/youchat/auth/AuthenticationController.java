package top.phakeandy.youchat.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.phakeandy.youchat.auth.request.LoginRequest;
import top.phakeandy.youchat.auth.response.LoginResponse;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "用户认证相关接口")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/login")
  @SecurityRequirements // 登录接口不需要认证
  @Operation(summary = "用户登录", description = "通过用户名和密码进行用户身份验证，成功后创建会话并返回用户信息")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "登录成功，返回用户信息和权限",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "请求参数错误，用户名或密码为空或格式错误",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(
            responseCode = "401",
            description = "身份验证失败，用户名或密码错误",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class))),
      })
  public ResponseEntity<LoginResponse> login(
      @Parameter(description = "登录请求参数，包含用户名和密码", required = true) @Valid @RequestBody
          LoginRequest loginRequest,
      @Parameter(hidden = true) HttpServletRequest request,
      @Parameter(hidden = true) HttpServletResponse response) {

    LoginResponse loginResponse =
        authenticationService.authenticate(loginRequest, request, response);
    return ResponseEntity.ok(loginResponse);
  }
}
