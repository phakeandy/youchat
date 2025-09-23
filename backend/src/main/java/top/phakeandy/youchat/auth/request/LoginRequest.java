package top.phakeandy.youchat.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
    description = "用户登录请求参数",
    requiredProperties = {"username", "password"})
public record LoginRequest(
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "testuser")
        @NotBlank(message = "用户名不能为空")
        String username,
    @Schema(
            description = "用户密码",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "password123")
        @NotBlank(message = "密码不能为空")
        String password) {}
