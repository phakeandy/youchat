package top.phakeandy.youchat.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户注册成功响应")
public record RegisterResponse(
    @Schema(description = "注册成功消息", example = "用户注册成功") String message,
    @Schema(description = "注册用户ID", example = "1") Long userId,
    @Schema(description = "注册用户名", example = "newuser") String username,
    @Schema(description = "注册用户昵称", example = "新用户") String nickname) {}