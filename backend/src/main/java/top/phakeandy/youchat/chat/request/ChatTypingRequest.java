package top.phakeandy.youchat.chat.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/** 正在输入状态请求模型 */
@Schema(description = "正在输入状态请求")
public record ChatTypingRequest(
    @Schema(description = "接收者用户ID", example = "67890")
        @NotBlank(message = "接收者用户ID不能为空")
        @Size(min = 1, max = 50, message = "接收者用户ID长度必须在1-50个字符之间")
        String receiverId,
    @Schema(description = "是否正在输入", example = "true") @NotNull(message = "输入状态不能为空")
        Boolean isTyping,
    @Schema(description = "会话ID（可选，用于区分不同的聊天会话）", example = "conv_12345")
        @Size(max = 50, message = "会话ID长度不能超过50个字符")
        String conversationId,
    @Schema(description = "是否为群聊", example = "false") Boolean isGroupMessage) {
  public ChatTypingRequest {
    if (isTyping == null) {
      isTyping = true;
    }
    if (isGroupMessage == null) {
      isGroupMessage = false;
    }
  }
}
