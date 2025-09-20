package top.phakeandy.youchat.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(
    description = "用户注册请求参数",
    requiredProperties = {"username", "password", "nickname"})
public record CreateUserRequest(
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "newuser")
        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
        String username,
    @Schema(
            description = "用户密码",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Password123!")
        @NotBlank(message = "密码不能为空")
        @Size(min = 8, max = 100, message = "密码长度必须在8-100个字符之间")
        @Pattern(
            regexp =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$",
            message = "密码必须包含至少一个大写字母、一个小写字母、一个数字和一个特殊字符")
        String password,
    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "新用户")
        @NotBlank(message = "昵称不能为空")
        @Size(min = 1, max = 100, message = "昵称长度必须在1-100个字符之间")
        String nickname,
    @Schema(
            description = "确认密码",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Password123!")
        @NotBlank(message = "确认密码不能为空")
        String confirmPassword) {}
