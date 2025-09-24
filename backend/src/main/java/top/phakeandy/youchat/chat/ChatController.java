package top.phakeandy.youchat.chat;

import io.github.springwolf.bindings.stomp.annotations.StompAsyncOperationBinding;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import top.phakeandy.youchat.chat.request.ChatMessageRequest;
import top.phakeandy.youchat.chat.request.ChatTypingRequest;
import top.phakeandy.youchat.chat.request.MessageReadRequest;
import top.phakeandy.youchat.chat.response.ChatMessageResponse;

@Controller
public class ChatController {

  /** 发送私聊消息 消息路由：/app/chat.private.send 订阅地址：/user/{userId}/queue/private */
  @MessageMapping("/chat.private.send")
  @SendTo("/user/{receiverId}/queue/private")
  @StompAsyncOperationBinding
  public ChatMessageResponse sendPrivateMessage(
      @Payload ChatMessageRequest request, SimpMessageHeaderAccessor headerAccessor) {

    throw new UnsupportedOperationException("私聊消息发送功能尚未实现");
  }

  /** 接收私聊消息 消息路由：/user/{userId}/queue/private */
  @MessageMapping("/chat.private.receive")
  public void receivePrivateMessage(
      @Payload ChatMessageResponse message, SimpMessageHeaderAccessor headerAccessor) {

    throw new UnsupportedOperationException("私聊消息接收功能尚未实现");
  }

  /** 更新正在输入状态 消息路由：/app/chat.typing 订阅地址：/topic/typing/{userId} */
  @MessageMapping("/chat.typing")
  @SendTo("/topic/typing/{receiverId}")
  public void updateTypingStatus(
      @Payload ChatTypingRequest request, SimpMessageHeaderAccessor headerAccessor) {

    throw new UnsupportedOperationException("正在输入状态更新功能尚未实现");
  }

  /** 接收正在输入状态 消息路由：/topic/typing/{userId} */
  @MessageMapping("/chat.typing.receive")
  public void receiveTypingStatus(
      @Payload ChatTypingRequest status, SimpMessageHeaderAccessor headerAccessor) {

    throw new UnsupportedOperationException("正在输入状态接收功能尚未实现");
  }

  /** 标记消息为已读 消息路由：/app/chat.read 订阅地址：/topic/read/{senderId} */
  @MessageMapping("/chat.read")
  @SendTo("/topic/read/{senderId}")
  public void markMessageAsRead(
      @Payload MessageReadRequest request, SimpMessageHeaderAccessor headerAccessor) {

    throw new UnsupportedOperationException("消息已读标记功能尚未实现");
  }

  /** 接收消息已读状态 消息路由：/topic/read/{userId} */
  @MessageMapping("/chat.read.receive")
  public void receiveReadStatus(
      @Payload MessageReadRequest request, SimpMessageHeaderAccessor headerAccessor) {

    throw new UnsupportedOperationException("消息已读状态接收功能尚未实现");
  }

  // ===== 为群聊功能预留的接口 =====

  /** 发送群聊消息（预留） 消息路由：/app/chat.group.send 订阅地址：/topic/group/{groupId} */
  @MessageMapping("/chat.group.send")
  @SendTo("/topic/group/{groupId}")
  public ChatMessageResponse sendGroupMessage(
      @Payload ChatMessageRequest request, SimpMessageHeaderAccessor headerAccessor) {

    throw new UnsupportedOperationException("群聊消息发送功能尚未实现");
  }

  /** 接收群聊消息（预留） 消息路由：/topic/group/{groupId} */
  @MessageMapping("/chat.group.receive")
  public void receiveGroupMessage(
      @Payload ChatMessageResponse message, SimpMessageHeaderAccessor headerAccessor) {

    throw new UnsupportedOperationException("群聊消息接收功能尚未实现");
  }
}
