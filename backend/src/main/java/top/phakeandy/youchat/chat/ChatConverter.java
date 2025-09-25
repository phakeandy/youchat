package top.phakeandy.youchat.chat;

import org.springframework.stereotype.Component;
import top.phakeandy.youchat.chat.request.CreateChatRequest;
import top.phakeandy.youchat.chat.request.UpdateChatRequest;
import top.phakeandy.youchat.model.ChatGroups;

@Component
public class ChatConverter {

  public ChatResponse toChatResponse(ChatGroups chatGroup) {
    return new ChatResponse(
        chatGroup.getId(),
        chatGroup.getName(),
        chatGroup.getDescription(),
        chatGroup.getAvatarUrl(),
        chatGroup.getOwnerId(),
        chatGroup.getSettings(),
        chatGroup.getAnnouncement(),
        chatGroup.getCreatedAt(),
        chatGroup.getUpdatedAt());
  }

  public ChatGroups fromCreateRequest(CreateChatRequest request) {
    ChatGroups chatGroup = new ChatGroups();
    chatGroup.setName(request.name());
    chatGroup.setDescription(request.description());
    chatGroup.setAvatarUrl(request.avatarUrl());
    chatGroup.setOwnerId(request.ownerId());
    chatGroup.setSettings("{}");
    chatGroup.setAnnouncement("{}");
    return chatGroup;
  }

  public void updateFromRequest(ChatGroups chatGroup, UpdateChatRequest request) {
    chatGroup.setName(request.name());
    chatGroup.setDescription(request.description());
    chatGroup.setAvatarUrl(request.avatarUrl());
  }
}
