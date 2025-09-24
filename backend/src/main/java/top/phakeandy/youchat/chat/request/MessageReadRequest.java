package top.phakeandy.youchat.chat.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/** 消息已读请求模型 */
@Schema(description = "消息已读请求")
public record MessageReadRequest(
    @Schema(description = "消息ID", example = "msg_12345")
        @NotBlank(message = "消息ID不能为空")
        @Size(min = 1, max = 50, message = "消息ID长度必须在1-50个字符之间")
        String messageId,
    @Schema(description = "发送者用户ID", example = "12345")
        @NotBlank(message = "发送者用户ID不能为空")
        @Size(min = 1, max = 50, message = "发送者用户ID长度必须在1-50个字符之间")
        String senderId,
    @Schema(description = "阅读时间戳", example = "2024-01-15T10:35:00") @NotNull(message = "阅读时间戳不能为空")
        java.time.LocalDateTime readTime,
    @Schema(description = "是否为群聊消息", example = "false") Boolean isGroupMessage,
    @Schema(description = "群聊ID（如果是群聊消息）", example = "group_12345") String groupId) {
  public MessageReadRequest {
    if (isGroupMessage == null) {
      isGroupMessage = false;
    }
  }
}
