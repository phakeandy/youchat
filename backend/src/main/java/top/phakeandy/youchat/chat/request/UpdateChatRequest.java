package top.phakeandy.youchat.chat.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "更新群聊请求参数")
public record UpdateChatRequest(
    @Schema(description = "群聊名称", example = "前端开发交流群")
        @NotBlank(message = "群聊名称不能为空")
        @Size(min = 1, max = 100, message = "群聊名称长度必须在1-100个字符之间")
        String name,
    @Schema(description = "群聊描述", example = "讨论前端开发相关技术问题")
        @Size(max = 500, message = "群聊描述长度不能超过500个字符")
        String description,
    @Schema(description = "群聊头像URL", example = "https://example.com/avatar.jpg")
        @Size(max = 500, message = "头像URL长度不能超过500个字符")
        String avatarUrl) {}
