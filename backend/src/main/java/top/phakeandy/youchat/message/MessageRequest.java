package top.phakeandy.youchat.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "聊天消息请求")
public record MessageRequest(
    @Schema(description = "接收者用户ID", example = "12345")
        @NotBlank(message = "接收者用户ID不能为空")
        @Size(min = 1, max = 50, message = "接收者用户ID长度必须在1-50个字符之间")
        Integer receiverId,
    @Schema(description = "消息内容", example = "你好，最近怎么样？")
        @NotBlank(message = "消息内容不能为空")
        @Size(min = 1, max = 1000, message = "消息内容长度必须在1-1000个字符之间")
        String content,
    @Schema(description = "消息类型", example = "TEXT") @NotNull(message = "消息类型不能为空")
        MessageType messageType,
    @Schema(description = "回复的消息ID（可选）", example = "msg_67890")
        @Size(max = 50, message = "回复消息ID长度不能超过50个字符")
        String replyToMessageId) {}
