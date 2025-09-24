package top.phakeandy.youchat.chat.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/** 聊天消息响应模型 */
@Schema(description = "聊天消息响应")
public record ChatMessageResponse(
    @Schema(description = "消息ID", example = "msg_12345") String messageId,
    @Schema(description = "发送者用户ID", example = "12345") String senderId,
    @Schema(description = "发送者用户名", example = "张三") String senderUsername,
    @Schema(description = "接收者用户ID", example = "67890") String receiverId,
    @Schema(description = "消息内容", example = "你好，最近怎么样？") String content,
    @Schema(
            description = "消息类型",
            example = "TEXT",
            allowableValues = {"TEXT", "IMAGE", "FILE", "EMOJI"})
        String messageType,
    @Schema(description = "发送时间", example = "2024-01-15T10:30:00") LocalDateTime sendTime,
    @Schema(
            description = "消息状态",
            example = "DELIVERED",
            allowableValues = {"SENT", "DELIVERED", "READ"})
        String messageStatus,
    @Schema(description = "回复的消息ID（可选）", example = "msg_67890") String replyToMessageId,
    @Schema(description = "是否为群聊消息", example = "false") Boolean isGroupMessage,
    @Schema(description = "群聊ID（如果是群聊消息）", example = "group_12345") String groupId) {
  public ChatMessageResponse {
    if (isGroupMessage == null) {
      isGroupMessage = false;
    }
  }
}
