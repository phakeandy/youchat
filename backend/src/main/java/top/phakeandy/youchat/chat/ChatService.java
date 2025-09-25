package top.phakeandy.youchat.chat;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static top.phakeandy.youchat.mapper.ChatGroupsDynamicSqlSupport.id;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.phakeandy.youchat.chat.request.CreateChatRequest;
import top.phakeandy.youchat.chat.request.UpdateChatRequest;
import top.phakeandy.youchat.exception.ResourceNotFoundException;
import top.phakeandy.youchat.mapper.ChatGroupsMapper;
import top.phakeandy.youchat.model.ChatGroups;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final ChatGroupsMapper chatGroupsMapper;
  private final ChatConverter chatConverter;

  @Transactional
  public ChatResponse createChat(CreateChatRequest request) {
    ChatGroups chatGroup = chatConverter.fromCreateRequest(request);
    chatGroup.setCreatedAt(OffsetDateTime.now());
    chatGroup.setUpdatedAt(OffsetDateTime.now());

    chatGroupsMapper.insert(chatGroup);

    return chatConverter.toChatResponse(chatGroup);
  }

  public Page<ChatResponse> getChats(Pageable pageable) {
    var count = chatGroupsMapper.count(c -> c);

    List<ChatGroups> chatGroups =
        chatGroupsMapper.select(
            c -> c.limit(pageable.getPageSize()).offset((int) pageable.getOffset()));
    List<ChatResponse> responses = chatGroups.stream().map(chatConverter::toChatResponse).toList();

    return new PageImpl<>(responses, pageable, count);
  }

  public ChatResponse getChat(Long chatId) {
    var chatGroup =
        chatGroupsMapper
            .selectOne(c -> c.where(id, isEqualTo(chatId)))
            .orElseThrow(() -> new ResourceNotFoundException("群聊不存在"));

    return chatConverter.toChatResponse(chatGroup);
  }

  @Transactional
  public ChatResponse updateChat(Long chatId, UpdateChatRequest request) {
    var chatGroup =
        chatGroupsMapper
            .selectOne(c -> c.where(id, isEqualTo(chatId)))
            .orElseThrow(() -> new ResourceNotFoundException("群聊不存在"));

    chatConverter.updateFromRequest(chatGroup, request);
    chatGroup.setUpdatedAt(OffsetDateTime.now());

    chatGroupsMapper.updateByPrimaryKey(chatGroup);

    return chatConverter.toChatResponse(chatGroup);
  }

  @Transactional
  public void deleteChat(Long chatId) {
    int deleted = chatGroupsMapper.deleteByPrimaryKey(chatId);
    if (deleted == 0) {
      throw new ResourceNotFoundException("群聊不存在");
    }
  }
}
