package top.phakeandy.youchat.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import top.phakeandy.youchat.auth.CustomUserDetails;
import top.phakeandy.youchat.user.request.CreateUserRequest;
import top.phakeandy.youchat.user.responcse.CreateUserResponse;
import top.phakeandy.youchat.user.responcse.UserResponse;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "用户管理相关接口")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/current")
  @SecurityRequirement(name = "sessionAuth")
  @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息，包括用户名和权限列表。需要用户已登录并持有有效会话。")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "成功获取当前用户信息",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "未认证，用户未登录或会话已过期",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class))),
      })
  public ResponseEntity<UserResponse> getCurrentUser(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    UserResponse response = userService.getCurrentUser(customUserDetails);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/current")
  @SecurityRequirement(name = "sessionAuth")
  @Operation(summary = "删除当前用户账户", description = "删除当前登录用户的账户及其所有相关数据。此操作不可逆。需要用户已登录并持有有效会话。")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "用户账户删除成功"),
        @ApiResponse(
            responseCode = "401",
            description = "未认证，用户未登录或会话已过期",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class))),
      })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCurrentUser(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {
    userService.deleteCurrentUser(customUserDetails);
  }
}
