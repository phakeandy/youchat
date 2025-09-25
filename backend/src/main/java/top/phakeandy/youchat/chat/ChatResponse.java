package top.phakeandy.youchat.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;

@Schema(description = "群聊响应对象")
public record ChatResponse(
    @Schema(description = "群聊ID", example = "1") Long id,
    @Schema(description = "群聊名称", example = "前端开发交流群") String name,
    @Schema(description = "群聊描述", example = "讨论前端开发相关技术问题") String description,
    @Schema(description = "群聊头像URL", example = "https://example.com/avatar.jpg") String avatarUrl,
    @Schema(description = "群主ID", example = "1") Long ownerId,
    @Schema(description = "群聊设置", example = "{}") Object settings,
    @Schema(description = "群聊公告", example = "{}") Object announcement,
    @Schema(description = "创建时间", example = "2024-01-01T12:00:00") OffsetDateTime createdAt,
    @Schema(description = "更新时间", example = "2024-01-01T12:00:00") OffsetDateTime updatedAt) {}
